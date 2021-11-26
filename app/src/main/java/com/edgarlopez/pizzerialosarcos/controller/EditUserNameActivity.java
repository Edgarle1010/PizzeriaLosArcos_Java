package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class EditUserNameActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageButton backImageButton;
    private EditText nameEditText;
    private EditText lastnameEditText;
    private Button saveButton;

    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name);

        user = FirebaseAuth.getInstance().getCurrentUser();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(EditUserNameActivity.this
                .getApplication())
                .create(UserViewModel.class);

        progressBar = findViewById(R.id.edit_username_progress);
        backImageButton = findViewById(R.id.back_button_edit_username);
        nameEditText = findViewById(R.id.name_edit_text_edit_username);
        lastnameEditText = findViewById(R.id.lastname_edit_text_edit_username);
        saveButton = findViewById(R.id.save_button_edit_username);

        backImageButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String lastname = lastnameEditText.getText().toString().trim();

            if (!name.matches("[A-Za-zÀ-ÿ '-]*") || name.isEmpty()) {
                nameEditText.setError("El nombre introducido no es válido");
            } else if (!lastname.matches("[A-Za-zÀ-ÿ '-]*") || lastname.isEmpty()) {
                lastnameEditText.setError("El apellido introducido no es válido");
            } else {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name + " " + lastname)
                        .build();

                progressBar.setVisibility(View.VISIBLE);
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.VISIBLE);
                                collectionReference
                                        .whereEqualTo("userId", user.getUid())
                                        .get().addOnCompleteListener(task1 -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task1.getResult()) {
                                            Map<String, Object> update = new HashMap<>();
                                            update.put("name", name);
                                            update.put("lastName", lastname);
                                            progressBar.setVisibility(View.VISIBLE);
                                            collectionReference
                                                    .document(document.getId())
                                                    .update(update).addOnCompleteListener(task2 -> {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (task2.isSuccessful()) {
                                                            User currUser = new User();
                                                            currUser.setUserId(user.getUid());
                                                            currUser.setName(name);
                                                            currUser.setLastName(name);
                                                            UserViewModel.update(currUser);

                                                            Toast.makeText(EditUserNameActivity.this,
                                                                    "Datos actualizados correctamente",
                                                                    Toast.LENGTH_SHORT)
                                                                    .show();

                                                            finish();
                                                        } else {
                                                            Toast.makeText(EditUserNameActivity.this,
                                                                    task2.getException().getLocalizedMessage(),
                                                                    Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                            });
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(EditUserNameActivity.this,
                                        task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });
    }
}