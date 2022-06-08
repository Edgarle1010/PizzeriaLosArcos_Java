package com.edgarlopez.pizzerialosarcos.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.edgarlopez.pizzerialosarcos.ui.ItemRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backButton;

    private UserViewModel userViewModel;

    private Button loginButton;
    private TextView signInTextView;
    private TextView recoveryPasswordTextView;
    private EditText passwordEditText;

    //e-mail login
    private ImageView loginWithEmailImage;
    private TextView emailTextView;
    private ImageView emailIconImage;
    private AutoCompleteTextView emailEditText;

    //phoneNumber login
    private ImageView loginWithPhoneNumberImage;
    private TextView phoneNumberTextView;
    private ImageView phoneNumberIconImage;
    private Spinner regionSpinner;
    private AutoCompleteTextView phoneNumberEditText;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private List<String> listRegion = new ArrayList<String>();
    private String prefix = "+52";
    private boolean emailIsSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.login_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(
                LoginActivity.this.getApplication())
                .create(UserViewModel.class);

        backButton = findViewById(R.id.back_button_login);

        //e-mail login
        loginWithEmailImage = findViewById(R.id.login_with_email_icon);
        emailTextView = findViewById(R.id.email_login_text);
        emailIconImage = findViewById(R.id.email_icon);
        emailEditText = findViewById(R.id.email);

        //phoneNumber login
        loginWithPhoneNumberImage = findViewById(R.id.login_with_phone_number_icon);
        phoneNumberTextView = findViewById(R.id.phone_number_text);
        phoneNumberIconImage = findViewById(R.id.phone_number_icon);
        regionSpinner = findViewById(R.id.region_login_spinner);
        phoneNumberEditText = findViewById(R.id.phone_number);

        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signInTextView = findViewById(R.id.signIn_text);
        recoveryPasswordTextView = findViewById(R.id.recovery_text);

        backButton.setOnClickListener(this);

        listRegion.add("+52");
        listRegion.add("+1");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listRegion);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(dataAdapter);

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                loginButton.callOnClick();
            }
            return false;
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_login:
                finish();
                break;
            case R.id.signIn_text:
                startActivity(new Intent(LoginActivity.this,
                        TermsServicesActivity.class));
                break;
            case R.id.recovery_text:
                startActivity(new Intent(LoginActivity.this,
                        RecoveryPasswordActivity.class));
                break;
            case R.id.login_button:
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = prefix + phoneNumberEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (emailIsSelected) {
                    loginEmailPasswordUser(email, password);
                }else {
                    loginWithPhoneNumberPasswordUser(phoneNumber, password);
                }

                break;
            case R.id.login_with_email_icon:
                showEmailLoginForm();

                break;
            case R.id.login_with_phone_number_icon:
                showPhoneNumberLoginForm();

                break;
        }
    }

    private void showPhoneNumberLoginForm() {
        loginWithEmailImage.setColorFilter(Color.GRAY);
        emailTextView.setVisibility(View.GONE);
        emailIconImage.setVisibility(View.GONE);
        emailEditText.setVisibility(View.GONE);

        loginWithPhoneNumberImage.setColorFilter(Color.BLACK);
        phoneNumberTextView.setVisibility(View.VISIBLE);
        phoneNumberIconImage.setVisibility(View.VISIBLE);
        regionSpinner.setVisibility(View.VISIBLE);
        phoneNumberEditText.setVisibility(View.VISIBLE);

        emailIsSelected = false;
    }

    private void showEmailLoginForm() {
        loginWithPhoneNumberImage.setColorFilter(Color.GRAY);
        phoneNumberTextView.setVisibility(View.GONE);
        phoneNumberIconImage.setVisibility(View.GONE);
        regionSpinner.setVisibility(View.GONE);
        phoneNumberEditText.setVisibility(View.GONE);

        loginWithEmailImage.setColorFilter(Color.BLACK);
        emailTextView.setVisibility(View.VISIBLE);
        emailIconImage.setVisibility(View.VISIBLE);
        emailEditText.setVisibility(View.VISIBLE);

        emailIsSelected = true;
    }

    private void loginEmailPasswordUser(String email, String pwd) {
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String currentUserId = user.getUid();

                            collectionReference
                                    .whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        if (e != null) {
                                        }
                                        assert queryDocumentSnapshots != null;
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                String id = snapshot.getString("userId");
                                                String name = snapshot.getString("name");
                                                String lastName = snapshot.getString("lastName");
                                                String email1 = snapshot.getString("email");
                                                String phoneNumber = snapshot.getString("phoneNumber");
                                                String streak = snapshot.get("streaks").toString();
                                                List<String> streaks = new ArrayList<String>();
                                                streaks.add(streak);
                                                String fcmToken = snapshot.getString("fcmToken");
                                                boolean baned = snapshot.getBoolean("isBaned");

                                                User user1 = new User(id, name, lastName, email1, phoneNumber, streaks, baned, fcmToken);

                                                userViewModel.getAllUsers().observe(this, users -> {
                                                    UserViewModel.insert(user1);
                                                });

                                                Toast.makeText(LoginActivity.this,
                                                        String.format(getString(R.string.welcome_message), name),
                                                        Toast.LENGTH_SHORT)
                                                        .show();

                                                finishAffinity();

                                                startActivity(new Intent(LoginActivity.this,
                                                        MenuActivity.class));
                                            }

                                        }

                                    });
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,
                                    Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,
                    "Por favor introduce tu e-mail y contraseña.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loginWithPhoneNumberPasswordUser(String phoneNumber, String password) {
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)) {
            collectionReference
                    .whereEqualTo("phoneNumber", phoneNumber)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (e != null) {
                        }
                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                AppController appController = AppController.getInstance();
                                String email = snapshot.getString("email");
                                loginEmailPasswordUser(email, password);
                            }
                        }else {
                            Toast.makeText(LoginActivity.this,
                                    "El número celular introducido no esta registrado.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }

                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,
                    "Por favor introduce tu número celular y contraseña.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}