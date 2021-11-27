package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edgarlopez.pizzerialosarcos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationCodeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText verificationCodeEditText;
    private TextView phoneNumberTextView;
    private Button nextButton;
    private String phoneNumber;
    private String verificationId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.verification_code_progress);
        verificationCodeEditText = findViewById(R.id.verification_code_text_edit_text);
        phoneNumberTextView = findViewById(R.id.verification_code_text);
        nextButton = findViewById(R.id.next_verification_code_button);

        phoneNumber = AppController.getInstance().getPhoneNumber();
        verificationId = AppController.getInstance().getVerificationId();

        phoneNumberTextView.setText(String.format("%s %s", phoneNumberTextView.getText().toString(), phoneNumber));

        nextButton.setOnClickListener(v -> {
            String code = verificationCodeEditText.getText().toString().trim();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(VerificationCodeActivity.this,
                                    CreatePasswordActivity.class));
                        }else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                verificationCodeEditText.setError("El código de verificación introducido no es válido");
                            }
                        }
                    });
        });

        verificationCodeEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                nextButton.callOnClick();
            }
            return false;
        });
    }
}