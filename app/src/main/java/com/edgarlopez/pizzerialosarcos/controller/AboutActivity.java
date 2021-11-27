package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.edgarlopez.pizzerialosarcos.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backImageButton;
    private CardView termsServiceCardView,
            privacyStatementCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        backImageButton = findViewById(R.id.back_button_about);
        termsServiceCardView = findViewById(R.id.term_service_card_view);
        privacyStatementCardView = findViewById(R.id.privacy_statement_card_view);

        backImageButton.setOnClickListener(this);
        termsServiceCardView.setOnClickListener(this);
        privacyStatementCardView.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_about:
                finish();
                break;
            case R.id.term_service_card_view:
                Intent termServiceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pizzerialosarcos-9c55f.web.app"));
                startActivity(termServiceIntent);
                break;
            case R.id.privacy_statement_card_view:
                Intent privacyStatementIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pizzerialosarcos-9c55f.web.app/notice_privacy.html"));
                startActivity(privacyStatementIntent);
                break;
        }
    }
}