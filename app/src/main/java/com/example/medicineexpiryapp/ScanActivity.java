package com.example.medicineexpiryapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private DecoratedBarcodeView barcodeScanner;
    private Button btnStartScan, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        barcodeScanner = findViewById(R.id.barcode_scanner);
        btnStartScan = findViewById(R.id.btn_start_scan);
        btnCancel = findViewById(R.id.btn_cancel);

        // Start Scan button
        btnStartScan.setOnClickListener(v -> {
            // Check Camera Permission
            if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                startScanner();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> finish()); // Close activity
    }

    private void startScanner() {
        btnStartScan.setVisibility(View.GONE); // Hide start button
        barcodeScanner.setVisibility(View.VISIBLE); // Show scanner

        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barcodeScanner.pause(); // Stop scanning

                    // Save scanned data to history
                    saveHistoryToPreferences(result.getText(), "scanned");

                    // Send scanned data to ResultActivity
                    Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
                    intent.putExtra("QR_CONTENT", result.getText());
                    startActivity(intent);
                    finish(); // Close ScanActivity
                }
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {}
        });

        barcodeScanner.resume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_LONG).show();
            }
        }
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScanner.getVisibility() == View.VISIBLE)
            barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeScanner.getVisibility() == View.VISIBLE)
            barcodeScanner.pause();
    }
}
