package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.BuildConfig;
import com.edgarlopez.pizzerialosarcos.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Button loginButton;
    private Button signInButton;
    private Button guestButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_PizzeriaLosArcos);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        if (!isEmulator()) {
            firebaseAppCheck.installAppCheckProviderFactory(
                    SafetyNetAppCheckProviderFactory.getInstance());
        } else {
            Log.i("DEBUG_APP_CHECK", "Using debug version of AppCheck.");
            firebaseAppCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance());
        }

        progressBar = findViewById(R.id.welcome_progress);
        loginButton = findViewById(R.id.loginButton);
        signInButton = findViewById(R.id.signInButton);
        guestButton = findViewById(R.id.guestButton);

        loginButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        guestButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        showProgressDialogWithTitle("Iniciando sesión...");
        authStateListener = firebaseAuth -> {
            hideProgressDialogWithTitle();
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                showProgressDialogWithTitle("Iniciando sesión...");
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                collectionReference
                        .whereEqualTo("userId", user.getUid())
                        .get().addOnCompleteListener(task -> {
                            hideProgressDialogWithTitle();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    String userId = snapshot.getString("userId");
                                    if (userId.equals(userId)) {
                                        startActivity(new Intent(WelcomeActivity.this,
                                                MenuActivity.class));
                                        finish();
                                    }
                                }
                            } else {
                                Toast.makeText(WelcomeActivity.this,
                                        String.valueOf(task.getException()),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }

    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
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
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

}