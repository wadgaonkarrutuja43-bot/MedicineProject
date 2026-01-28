package com.example.medicineexpiryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private TextView tvAboutContent;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAboutContent = findViewById(R.id.tv_about_content);
        btnBack = findViewById(R.id.btn_back);

        // About Content
        String aboutContent = "App Name: Medicine Expiry App\n" +
                "Version: 1.0\n\n" +
                "Purpose: This app helps you keep track of the expiry dates of medicines.\n\n" +
                "Developed by: Team DoseSafe\n\n" +
                "Contact: developer1.amol@gmail.com\n\n" +
                "Disclaimer: Information provided in this app is for reference only.";

        // Set the about content to TextView
        tvAboutContent.setText(aboutContent);

        // Back Button functionality
        btnBack.setOnClickListener(v -> finish()); // Close the About Activity when back button is clicked
    }
}
