package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private Button loginButton;
    private Button signInButton;
    private Button guestButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        progressBar = findViewById(R.id.welcome_progress);
        loginButton = findViewById(R.id.loginButton);
        signInButton = findViewById(R.id.signInButton);
        guestButton = findViewById(R.id.guestButton);

        loginButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        guestButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        authStateListener = firebaseAuth -> {
            progressBar.setVisibility(View.GONE);
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                currentUser = firebaseAuth.getCurrentUser();
                String currentUserId = currentUser.getUid();

                progressBar.setVisibility(View.VISIBLE);
                collectionReference
                        .whereEqualTo("userId", currentUserId)
                        .get().addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    String userId = snapshot.getString("userId");

                                    if (userId.equals(currentUserId)) {
                                        startActivity(new Intent(WelcomeActivity.this,
                                                MenuActivity.class));
                                        finish();
                                    }
                                }
                            } else {
                                Toast.makeText(WelcomeActivity.this,
                                        task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                progressBar.setVisibility(View.GONE);
            }
        };

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                startActivity(new Intent(WelcomeActivity.this,
                        LoginActivity.class));
                break;
            case R.id.signInButton:
                startActivity(new Intent(WelcomeActivity.this,
                        TermsServicesActivity.class));
                break;
            case R.id.guestButton:
                AlertDialog.Builder aD = new AlertDialog.Builder(this);
                aD.setTitle("Modo invitado");
                aD.setMessage("Iniciaras sesión en modo invitado, podrás ver el menú completo y la mayoria de las funciones pero no podrás realizar pedidos hasta realizar el proceso de registro.");
                aD.setPositiveButton(R.string.acept, (dialog, which) -> {
                    startActivity(new Intent(WelcomeActivity.this,
                            MenuActivity.class));
                    finish();
                });
                aD.setNegativeButton("Volver", null);
                aD.setCancelable(false);

                AlertDialog dialog = aD.create();
                dialog.show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}