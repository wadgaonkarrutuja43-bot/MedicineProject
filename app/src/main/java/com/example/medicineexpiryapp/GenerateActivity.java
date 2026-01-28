package com.example.medicineexpiryapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateActivity extends AppCompatActivity {

    private EditText inputTabletName, inputBatchNo, inputMfgDate, inputExpiryDate;
    private Button btnGenerate;
    private TextView backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        // Initialize views
        inputTabletName = findViewById(R.id.input_tablet_name);
        inputBatchNo = findViewById(R.id.input_batch_no);
        inputMfgDate = findViewById(R.id.input_mfg_date);
        inputExpiryDate = findViewById(R.id.input_expiry_date);
        btnGenerate = findViewById(R.id.btn_generate_code);
        backToHome = findViewById(R.id.back_to_home);

        // Date pickers for Mfg and Expiry dates
        inputMfgDate.setOnClickListener(v -> showDatePicker(inputMfgDate));
        inputExpiryDate.setOnClickListener(v -> showDatePicker(inputExpiryDate));

        // Back button functionality
        backToHome.setOnClickListener(v -> finish());

        // Generate button click functionality
        btnGenerate.setOnClickListener(v -> {
            if (validateInputs()) {
                generateQRCode();
            }
        });
    }

    // Show Date Picker Dialog for both dates
    private void showDatePicker(final EditText editText) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, y, m, d) -> {
            String dateStr = String.format("%02d/%02d/%04d", d, m + 1, y);
            editText.setText(dateStr);
        }, year, month, day);
        dpd.show();
    }

    // Validation for input fields
    private boolean validateInputs() {
        boolean valid = true;

        if (TextUtils.isEmpty(inputTabletName.getText().toString().trim())) {
            inputTabletName.setError("Enter tablet name");
            valid = false;
        }
        if (TextUtils.isEmpty(inputBatchNo.getText().toString().trim())) {
            inputBatchNo.setError("Enter batch number");
            valid = false;
        }
        if (TextUtils.isEmpty(inputMfgDate.getText().toString().trim())) {
            inputMfgDate.setError("Select manufacturing date");
            valid = false;
        }
        if (TextUtils.isEmpty(inputExpiryDate.getText().toString().trim())) {
            inputExpiryDate.setError("Select expiry date");
            valid = false;
        }

        // Optional: Expiry date must be after manufacturing date
        if (!TextUtils.isEmpty(inputMfgDate.getText().toString().trim()) &&
                !TextUtils.isEmpty(inputExpiryDate.getText().toString().trim())) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date mfgDate = sdf.parse(inputMfgDate.getText().toString());
                Date expDate = sdf.parse(inputExpiryDate.getText().toString());

                // Compare dates
                if (expDate != null && mfgDate != null && !expDate.after(mfgDate)) {
                    inputExpiryDate.setError("Expiry must be after Mfg date");
                    valid = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                valid = false;
                Toast.makeText(this, "Date parsing error", Toast.LENGTH_SHORT).show();
            }
        }

        return valid;
    }

    // Generate the QR Code
    private void generateQRCode() {
        try {
            String data = "Tablet: " + inputTabletName.getText().toString().trim() +
                    "\nBatch: " + inputBatchNo.getText().toString().trim() +
                    "\nMfg: " + inputMfgDate.getText().toString().trim() +
                    "\nExpiry: " + inputExpiryDate.getText().toString().trim();

            // Using ZXing to generate QR code
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400);

            // Save the generated QR in SharedPreferences
            saveHistoryToPreferences(data, "generated");

            // Pass the generated QR code to the next activity
            DisplayQRActivity.bitmapQRCode = bitmap;

            Toast.makeText(this, "QR Code generated successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GenerateActivity.this, DisplayQRActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR", Toast.LENGTH_SHORT).show();
        }
    }

    // Save history to SharedPreferences
    private void saveHistoryToPreferences(String data, String type) {
        SharedPreferences sharedPreferences = getSharedPreferences("QR_HISTORY", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int historySize = sharedPreferences.getInt("history_size", 0);
        editor.putString("qr_content_" + historySize, data);
        editor.putString("type_" + historySize, type);
        editor.putLong("timestamp_" + historySize, System.currentTimeMillis());
        editor.putInt("history_size", historySize + 1);  // Increment the history count
        editor.apply();
    }
}
