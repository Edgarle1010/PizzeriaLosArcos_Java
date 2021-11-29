package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.edgarlopez.pizzerialosarcos.R;

public class TermsServicesActivity extends AppCompatActivity {
    private ImageButton backButton;
    private TextView termsServiceTextView;
    private Button acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_services);

        backButton = findViewById(R.id.back_button_terms_service);
        termsServiceTextView = findViewById(R.id.terms_service_textview);
        acceptButton = findViewById(R.id.accept_terms_service_button);

        termsServiceTextView.setMovementMethod(LinkMovementMethod.getInstance());

        backButton.setOnClickListener(v -> finish());

        acceptButton.setOnClickListener(v -> {
            startActivity(new Intent(TermsServicesActivity.this,
                    SignInActivity.class));
        });
    }
}