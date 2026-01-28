package com.example.medicineexpiryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    public HistoryAdapter historyAdapter;
    private List<History> historyList;
    private Button btnScannedHistory, btnGeneratedHistory, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvHistory = findViewById(R.id.rv_history);
        btnScannedHistory = findViewById(R.id.btn_scanned_history);
        btnGeneratedHistory = findViewById(R.id.btn_generated_history);
        btnBack = findViewById(R.id.btn_back);

        // Set up RecyclerView
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Load all history by default
        loadHistory("all");

        // Button for showing scanned history
        btnScannedHistory.setOnClickListener(v -> loadHistory("scanned"));

        // Button for showing generated history
        btnGeneratedHistory.setOnClickListener(v -> loadHistory("generated"));

        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadHistory(String type) {
        historyList = getHistoryFromPreferences(); // Retrieve all history

        // Filter the history if needed (scanned or generated)
        if (!type.equals("all")) {
            List<History> filteredHistory = new ArrayList<>();
            for (History history : historyList) {
                if (history.getType().equalsIgnoreCase(type)) {
                    filteredHistory.add(history);
                }
            }
            historyList = filteredHistory;
        }

        // Set adapter for RecyclerView
        historyAdapter = new HistoryAdapter(this, historyList);
        rvHistory.setAdapter(historyAdapter);

        // Show RecyclerView when data is available
        if (!historyList.isEmpty()) {
            rvHistory.setVisibility(View.VISIBLE);
        } else {
            rvHistory.setVisibility(View.GONE);
        }
    }

    private List<History> getHistoryFromPreferences() {
        List<History> historyList = new ArrayList<>();

        // Retrieve the history data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("QR_HISTORY", MODE_PRIVATE);
        int historySize = sharedPreferences.getInt("history_size", 0);  // Get the number of saved history entries

        // Iterate over all saved history entries and load them into the historyList
        for (int i = 0; i < historySize; i++) {
            String qrContent = sharedPreferences.getString("qr_content_" + i, "");
            String type = sharedPreferences.getString("type_" + i, "");
            long timestamp = sharedPreferences.getLong("timestamp_" + i, 0);

            if (!qrContent.isEmpty()) {
                History history = new History(qrContent, type, timestamp);
                historyList.add(history);
            }
        }

        return historyList;
    }
}
