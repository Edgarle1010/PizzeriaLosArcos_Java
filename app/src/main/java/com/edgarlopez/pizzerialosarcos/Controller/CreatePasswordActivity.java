package com.edgarlopez.pizzerialosarcos.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.edgarlopez.pizzerialosarcos.Util.Util.isValidPassword;

public class CreatePasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText password;
    private EditText verifyPassword;
    private Button signInButton;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        progressBar = findViewById(R.id.password_progress);
        password = findViewById(R.id.password_edit_text);
        verifyPassword = findViewById(R.id.verify_password_edit_text);
        signInButton = findViewById(R.id.create_account_button);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        signInButton.setOnClickListener(v -> {
            String passwordText = password.getText().toString().trim();
            String verifyPasswordText = verifyPassword.getText().toString().trim();

            if(passwordText.length() < 8 && !isValidPassword(passwordText)){
                password.setError("La contraseña introducida no es válida.");
            }else{
                if (!passwordText.equals(verifyPasswordText)) {
                    verifyPassword.setError("La contraseña no coincide.");
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    currentUser.updatePassword(verifyPasswordText)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String email = AppController.getInstance().getEmail();
                                    String phoneNumber = AppController.getInstance().getPhoneNumber();
                                    String name = AppController.getInstance().getName();
                                    String lastName = AppController.getInstance().getLastName();

                                    currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name)
                                                        .build();

                                                currentUser.updateProfile(profileUpdate)
                                                        .addOnCompleteListener(task1 -> {
                                                            progressBar.setVisibility(View.GONE);
                                                            if (task1.isSuccessful()) {
                                                                //we take user to AddJournalActivity
                                                                assert currentUser != null;
                                                                final String currentUserId = currentUser.getUid();

                                                                //Create a user Map so we can create a user in the User collection
                                                                Map<String, Object> userObj = new HashMap<>();
                                                                userObj.put("userId", currentUserId);
                                                                userObj.put("name", name);
                                                                userObj.put("lastname", lastName);
                                                                userObj.put("email", email);
                                                                userObj.put("phoneNumber", phoneNumber);
                                                                userObj.put("streaks", 0);
                                                                userObj.put("baned", false);

                                                                collectionReference.add(userObj)
                                                                        .addOnSuccessListener(documentReference -> documentReference.get()
                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task11) {
                                                                                        if (Objects.requireNonNull(task11.getResult().exists())) {
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                            AppController.getInstance().setUserId(currentUserId);

                                                                                            finishAffinity();
                                                                                            Intent intent = new Intent(CreatePasswordActivity.this,
                                                                                                    MenuActivity.class);
                                                                                            startActivity(intent);

                                                                                            Toast.makeText(CreatePasswordActivity.this,
                                                                                                    "Tu usuario se ha creado correctamente. Por favor no olvides tu usuario ni tu contraseña",
                                                                                                    Toast.LENGTH_SHORT)
                                                                                                    .show();

                                                                                        }else {
                                                                                            progressBar.setVisibility(View.GONE);
                                                                                        }
                                                                                    }
                                                                                }))
                                                                        .addOnFailureListener(e -> Toast.makeText(CreatePasswordActivity.this,
                                                                                e.getLocalizedMessage(),
                                                                                Toast.LENGTH_SHORT)
                                                                                .show());
                                                            }
                                                        });
                                            }
                                        }
                                    });

                                }else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(CreatePasswordActivity.this,
                                            "¡Ha ocurrido un error!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                }
            }
        });

        verifyPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    signInButton.callOnClick();
                }
                return false;
            }
        });
    }
}