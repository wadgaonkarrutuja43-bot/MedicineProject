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
    private HistoryAdapter historyAdapter;
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

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Load all history by default
        loadHistory("all");

        btnScannedHistory.setOnClickListener(v -> loadHistory("scanned"));

        btnGeneratedHistory.setOnClickListener(v -> loadHistory("generated"));

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadHistory(String filterType) {
        historyList = getHistoryFromPreferences();

        if (!filterType.equals("all")) {
            List<History> filteredList = new ArrayList<>();

            for (History history : historyList) {

                // ✅ Scanned = camera OR gallery
                if (filterType.equals("scanned")) {
                    if (history.getType().equalsIgnoreCase("camera")
                            || history.getType().equalsIgnoreCase("gallery")
                            || history.getType().equalsIgnoreCase("scanned")) {
                        filteredList.add(history);
                    }
                }

                // ✅ Generated
                else if (filterType.equals("generated")
                        && history.getType().equalsIgnoreCase("generated")) {
                    filteredList.add(history);
                }
            }

            historyList = filteredList;
        }

        historyAdapter = new HistoryAdapter(this, historyList);
        rvHistory.setAdapter(historyAdapter);

        rvHistory.setVisibility(historyList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private List<History> getHistoryFromPreferences() {
        List<History> list = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("QR_HISTORY", MODE_PRIVATE);
        int historySize = prefs.getInt("history_size", 0);

        for (int i = 0; i < historySize; i++) {
            String qrContent = prefs.getString("qr_content_" + i, "");
            String type = prefs.getString("type_" + i, "");
            long timestamp = prefs.getLong("timestamp_" + i, 0);

            if (!qrContent.isEmpty()) {
                list.add(new History(qrContent, type, timestamp));
            }
        }
        return list;
    }
}
