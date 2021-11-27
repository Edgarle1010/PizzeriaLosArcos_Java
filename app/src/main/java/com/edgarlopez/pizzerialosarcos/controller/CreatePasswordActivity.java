package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.edgarlopez.pizzerialosarcos.util.Util.isValidPassword;

public class CreatePasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText password;
    private EditText verifyPassword;
    private Button signInButton;
    private FirebaseUser currentUser;

    private UserViewModel userViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(CreatePasswordActivity.this
                .getApplication())
                .create(UserViewModel.class);

        progressBar = findViewById(R.id.password_progress);
        password = findViewById(R.id.password_edit_text);
        verifyPassword = findViewById(R.id.verify_password_edit_text);
        signInButton = findViewById(R.id.create_account_button);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (AppController.getInstance().isRecoveryRequest()) {
            signInButton.setText(R.string.save_text);
        }

        signInButton.setOnClickListener(v -> {
            String passwordText = password.getText().toString().trim();
            String verifyPasswordText = verifyPassword.getText().toString().trim();

            if (passwordText.length() < 8 && !isValidPassword(passwordText)) {
                password.setError("La contraseña introducida no es válida.");
            } else {
                if (!passwordText.equals(verifyPasswordText)) {
                    verifyPassword.setError("La contraseña no coincide.");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    currentUser.updatePassword(verifyPasswordText)
                            .addOnCompleteListener(task -> {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    if (AppController.getInstance().isRecoveryRequest()) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        collectionReference
                                                .whereEqualTo("userId", currentUser.getUid())
                                                .get().addOnCompleteListener(task13 -> {
                                            progressBar.setVisibility(View.GONE);
                                            if (task13.isSuccessful()) {
                                                for (QueryDocumentSnapshot snapshot : task13.getResult()) {
                                                    String id = snapshot.getString("userId");
                                                    String name = snapshot.getString("name");
                                                    String lastName = snapshot.getString("lastName");
                                                    String email1 = snapshot.getString("email");
                                                    String phoneNumber = snapshot.getString("phoneNumber");
                                                    int streaks = snapshot.getLong("streaks").intValue();
                                                    boolean baned = snapshot.getBoolean("baned");

                                                    User user1 = new User(id, name, lastName, email1, phoneNumber, streaks, baned);

                                                    userViewModel.getAllUsers().observe(this, users -> {
                                                        UserViewModel.insert(user1);
                                                    });

                                                    Toast.makeText(CreatePasswordActivity.this,
                                                            R.string.password_recovery_successfully_message,
                                                            Toast.LENGTH_LONG)
                                                            .show();

                                                    AppController.getInstance().setRecoveryRequest(false);

                                                    finishAffinity();

                                                    startActivity(new Intent(CreatePasswordActivity.this,
                                                            WelcomeActivity.class));
                                                }
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(CreatePasswordActivity.this,
                                                        task.getException().getLocalizedMessage(),
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });

                                    } else {
                                        String email = AppController.getInstance().getEmail();
                                        String phoneNumber = AppController.getInstance().getPhoneNumber();
                                        String name = AppController.getInstance().getName();
                                        String lastName = AppController.getInstance().getLastName();

                                        progressBar.setVisibility(View.VISIBLE);
                                        currentUser.updateEmail(email).addOnCompleteListener(task12 -> {
                                            progressBar.setVisibility(View.GONE);
                                            if (task12.isSuccessful()) {
                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name + " " + lastName)
                                                        .build();

                                                progressBar.setVisibility(View.VISIBLE);
                                                currentUser.updateProfile(profileUpdate)
                                                        .addOnCompleteListener(task1 -> {
                                                            progressBar.setVisibility(View.GONE);
                                                            if (task1.isSuccessful()) {

                                                                assert currentUser != null;
                                                                final String currentUserId = currentUser.getUid();

                                                                User user = new User(currentUserId, name, lastName, email, phoneNumber, 0, false);

                                                                progressBar.setVisibility(View.VISIBLE);
                                                                collectionReference
                                                                        .document(currentUserId)
                                                                        .set(user).addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {
                                                                        userViewModel.getAllUsers().observe(CreatePasswordActivity.this, users -> {
                                                                            UserViewModel.insert(user);
                                                                        });

                                                                        AlertDialog.Builder aD = new AlertDialog.Builder(CreatePasswordActivity.this);
                                                                        aD.setTitle(String.format(getString(R.string.congratulations_message), name));
                                                                        aD.setMessage(R.string.user_create_successfully_message);
                                                                        aD.setPositiveButton(R.string.acept, (dialog, which) -> {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            finishAffinity();
                                                                            Intent intent = new Intent(CreatePasswordActivity.this,
                                                                                    MenuActivity.class);
                                                                            startActivity(intent);
                                                                        });
                                                                        aD.setCancelable(false);

                                                                        AlertDialog dialog = aD.create();
                                                                        dialog.show();

                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(CreatePasswordActivity.this,
                                                                                task.getException().getLocalizedMessage(),
                                                                                Toast.LENGTH_SHORT)
                                                                                .show();
                                                                    }

                                                                });
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(CreatePasswordActivity.this,
                                            task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                }
            }
        });

        verifyPassword.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                signInButton.callOnClick();
            }
            return false;
        });
    }
}