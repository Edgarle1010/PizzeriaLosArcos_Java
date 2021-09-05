package com.edgarlopez.pizzerialosarcos.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.edgarlopez.pizzerialosarcos.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegisterNumberActivity extends AppCompatActivity {
    private Spinner spinnerRegion;
    private EditText numberEditText;
    private Button nextButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private boolean exist;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private String prefix = "+52";
    private List<String> listRegion = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());

        firebaseAuth = FirebaseAuth.getInstance();

        spinnerRegion = findViewById(R.id.region_spinner);
        numberEditText = findViewById(R.id.phonenumber_text_edit_text);
        nextButton = findViewById(R.id.next_number_button);
        progressBar = findViewById(R.id.register_number_progress);

        listRegion.add("🇲🇽 México (+52)");
        listRegion.add("🇺🇸 Estados Unidos (+1)");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listRegion);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(dataAdapter);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentRegion = parent.getItemAtPosition(position).toString();

                if (currentRegion.equals(listRegion.get(0))) {
                    prefix = "+52";
                } else {
                    prefix = "+1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(v -> {
            String phoneNumber = numberEditText.getText().toString().trim();
            if (phoneNumber.length() == 10) {
                phoneNumber = prefix + phoneNumber;
                progressBar.setVisibility(View.VISIBLE);

                checkPhoneNumber(phoneNumber);

            } else {
                numberEditText.setError("El número celular introducido no es válido.");
            }
        });

        numberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    nextButton.callOnClick();
                }
                return false;
            }
        });

    }

    private void checkPhoneNumber(String phoneNumber) {
        collectionReference
                .whereEqualTo("phoneNumber", phoneNumber)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            numberEditText.setError("El número celular ya ha sido registrado anteriormente");
                        } else {
                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(firebaseAuth)
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(30L, TimeUnit.SECONDS)
                                            .setActivity(RegisterNumberActivity.this)
                                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                                @Override
                                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                                }

                                                @Override
                                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(RegisterNumberActivity.this,
                                                            e.getLocalizedMessage(),
                                                            Toast.LENGTH_LONG)
                                                            .show();
                                                }

                                                @Override
                                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(s, forceResendingToken);
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(RegisterNumberActivity.this,
                                                            "En breve recibiras un código de seguridad para verificar tu número celular.",
                                                            Toast.LENGTH_LONG)
                                                            .show();

                                                    AppController.getInstance().setPhoneNumber(phoneNumber);
                                                    AppController.getInstance().setVerificationId(s);
                                                    AppController.getInstance().setResendToken(forceResendingToken.toString());

                                                    startActivity(new Intent(RegisterNumberActivity.this,
                                                            VerificationCodeActivity.class));
                                                }
                                            })
                                            .build();

                            PhoneAuthProvider.verifyPhoneNumber(options);
                        }
                    }
                });
    }
}