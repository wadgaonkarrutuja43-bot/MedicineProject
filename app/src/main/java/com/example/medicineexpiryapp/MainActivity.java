package com.example.medicineexpiryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {

    private Button btnGenerate, btnScan, btnHistory, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge
        EdgeToEdge.enable(this);

        // Set layout
        setContentView(R.layout.activity_main);

        // Apply system window insets to root view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find buttons by ID
        btnGenerate = findViewById(R.id.btn_generate);
        btnScan = findViewById(R.id.btn_scan);
        btnHistory = findViewById(R.id.btn_history);
        btnAbout = findViewById(R.id.btn_about);

        // Set click listeners
        btnGenerate.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GenerateActivity.class)));
        btnScan.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));
        btnHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
        btnAbout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
    }
}
