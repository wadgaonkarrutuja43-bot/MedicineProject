package com.example.medicineexpiryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvQrContent;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvQrContent = findViewById(R.id.tv_qr_content);
        btnBack = findViewById(R.id.btn_back);

        // Get the QR content from ScanActivity
        String qrContent = getIntent().getStringExtra("QR_CONTENT");
        if (qrContent != null && !qrContent.isEmpty()) {
            tvQrContent.setText(qrContent);
        } else {
            tvQrContent.setText("No data scanned.");
        }

        // Button click: go back to ScanActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
