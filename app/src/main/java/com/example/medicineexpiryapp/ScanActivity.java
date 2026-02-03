package com.example.medicineexpiryapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.RGBLuminanceSource;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 201;

    private DecoratedBarcodeView barcodeScanner;
    private Button btnStartScan, btnGallery, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        barcodeScanner = findViewById(R.id.barcode_scanner);
        btnStartScan = findViewById(R.id.btn_start_scan);
        btnGallery = findViewById(R.id.btn_gallery);
        btnCancel = findViewById(R.id.btn_cancel);

        btnStartScan.setOnClickListener(v -> checkCameraPermission());

        btnGallery.setOnClickListener(v -> openGallery());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        } else {
            startScanner();
        }
    }

    private void startScanner() {
        btnStartScan.setVisibility(View.GONE);
        btnGallery.setVisibility(View.GONE);
        barcodeScanner.setVisibility(View.VISIBLE);

        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barcodeScanner.pause();

                    saveHistoryToPreferences(result.getText(), "camera");

                    Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
                    intent.putExtra("QR_CONTENT", result.getText());
                    startActivity(intent);
                    finish();
                }
            }
        });

        barcodeScanner.resume();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            decodeQRFromImage(data.getData());
        }
    }

    private void decodeQRFromImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(binaryBitmap);

            saveHistoryToPreferences(result.getText(), "gallery");

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("QR_CONTENT", result.getText());
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "No QR code found in selected image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveHistoryToPreferences(String data, String type) {
        SharedPreferences prefs = getSharedPreferences("QR_HISTORY", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = prefs.getInt("history_size", 0);
        editor.putString("qr_content_" + size, data);
        editor.putString("type_" + size, type);
        editor.putLong("timestamp_" + size, System.currentTimeMillis());
        editor.putInt("history_size", size + 1);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScanner.getVisibility() == View.VISIBLE) {
            barcodeScanner.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeScanner.getVisibility() == View.VISIBLE) {
            barcodeScanner.pause();
        }
    }
}
