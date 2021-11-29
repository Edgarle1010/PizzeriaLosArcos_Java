package com.edgarlopez.pizzerialosarcos.controller;

import static com.edgarlopez.pizzerialosarcos.util.Util.isValidPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageButton backImageButton;
    private EditText currentPasswordEditText,
            newPasswordEditText,
            verifyNewPasswordEditText;
    private Button saveButton;

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressBar = findViewById(R.id.change_password_progress);
        backImageButton = findViewById(R.id.back_button_change_password);
        currentPasswordEditText = findViewById(R.id.current_password_edit_text);
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        verifyNewPasswordEditText = findViewById(R.id.verify_new_password_edit_text);
        saveButton = findViewById(R.id.save_button_change_password);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        backImageButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            String currentPasswordText = currentPasswordEditText.getText().toString().trim();
            String passwordText = newPasswordEditText.getText().toString().trim();
            String verifyPasswordText = verifyNewPasswordEditText.getText().toString().trim();

            if (passwordText.length() < 8 && !isValidPassword(passwordText)) {
                verifyNewPasswordEditText.setError("La contraseña introducida no es válida.");
            }else if (!passwordText.equals(verifyPasswordText)) {
                verifyNewPasswordEditText.setError("La contraseña no coincide.");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = EmailAuthProvider
                        .getCredential(currentUser.getEmail(), currentPasswordText);

                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.VISIBLE);
                                currentUser.updatePassword(passwordText)
                                        .addOnCompleteListener(task1 -> {
                                            progressBar.setVisibility(View.GONE);
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(ChangePasswordActivity.this,
                                                        R.string.changue_paswword_successfully_message,
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                                finish();
                                            } else {
                                                Toast.makeText(ChangePasswordActivity.this,
                                                        task1.getException().getLocalizedMessage(),
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            } else {
                                Toast.makeText(ChangePasswordActivity.this,
                                        task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        verifyNewPasswordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                saveButton.callOnClick();
            }
            return false;
        });
    }
}