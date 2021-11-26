package com.edgarlopez.pizzerialosarcos.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edgarlopez.pizzerialosarcos.R;
import com.edgarlopez.pizzerialosarcos.model.ItemViewModel;
import com.edgarlopez.pizzerialosarcos.model.User;
import com.edgarlopez.pizzerialosarcos.model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class UserInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private ImageButton backImageButton;
    private CardView userNameCardView,
            changePasswordCardView,
            deleteAccountCardView;
    private TextView userNameTextView,
            phoneNumberTextView,
            emailTextView;
    private Button logoutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private ItemViewModel itemViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        itemViewModel = new ViewModelProvider.AndroidViewModelFactory(UserInformationActivity.this
                .getApplication())
                .create(ItemViewModel.class);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(UserInformationActivity.this
                .getApplication())
                .create(UserViewModel.class);

        progressBar = findViewById(R.id.user_information_progress);
        backImageButton = findViewById(R.id.back_button_user_information);
        userNameCardView = findViewById(R.id.edit_username_card_view);
        changePasswordCardView = findViewById(R.id.change_password_card_view);
        deleteAccountCardView = findViewById(R.id.delete_account_card_view);
        userNameTextView = findViewById(R.id.username_textview_user_information);
        phoneNumberTextView = findViewById(R.id.phone_number_object_textview_user_information);
        emailTextView = findViewById(R.id.email_object_textview_user_information);
        logoutButton = findViewById(R.id.logout_button);

        backImageButton.setOnClickListener(this);
        userNameCardView.setOnClickListener(this);
        changePasswordCardView.setOnClickListener(this);
        deleteAccountCardView.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_user_information:
                finish();
                break;
            case R.id.edit_username_card_view:
                startActivity(new Intent(UserInformationActivity.this,
                        EditUserNameActivity.class));
                break;
            case R.id.change_password_card_view:
                startActivity(new Intent(UserInformationActivity.this,
                        ChangePasswordActivity.class));
                break;
            case R.id.delete_account_card_view:
                final EditText edittext = new EditText(UserInformationActivity.this);
                edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edittext.setHint(R.string.password_hint);
                edittext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                AlertDialog.Builder aD = new AlertDialog.Builder(this);
                aD.setTitle(R.string.important_announcement);
                aD.setMessage(R.string.delete_account_message);
                aD.setView(edittext);
                aD.setNegativeButton(R.string.cancel_option, null);
                aD.setPositiveButton(R.string.acept, (dialog, which) -> {
                    progressBar.setVisibility(View.VISIBLE);

                    String edittextValue = edittext.getText().toString();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), edittextValue);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(taskR -> {
                                progressBar.setVisibility(View.GONE);
                                if (taskR.isSuccessful()) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    user.delete()
                                            .addOnCompleteListener(task -> {
                                                progressBar.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    ItemViewModel.deleteAll();
                                                    UserViewModel.deleteAll();

                                                    Toast.makeText(UserInformationActivity.this,
                                                            R.string.delete_account_successfully,
                                                            Toast.LENGTH_SHORT)
                                                            .show();

                                                    Intent intent = new Intent(UserInformationActivity.this,
                                                            WelcomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(UserInformationActivity.this,
                                                            Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(UserInformationActivity.this,
                                            Objects.requireNonNull(taskR.getException()).getLocalizedMessage(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                });
                aD.setCancelable(false);

                AlertDialog d = aD.create();
                d.show();
                break;
            case R.id.logout_button:
                if (user != null && firebaseAuth != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.logout_question_message);
                    builder.setNegativeButton(R.string.cancel_option, null);
                    builder.setPositiveButton(R.string.acept, (dialog, which) -> {
                        firebaseAuth.signOut();

                        ItemViewModel.deleteAll();
                        UserViewModel.deleteAll();

                        Intent intent = new Intent(UserInformationActivity.this,
                                WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
                    builder.setCancelable(false);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);

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
                            String name = snapshot.getString("name");
                            String lastName = snapshot.getString("lastName");
                            String email = snapshot.getString("email");
                            String phoneNumber = snapshot.getString("phoneNumber");

                            userNameTextView.setText(name + " " + lastName);
                            phoneNumberTextView.setText(phoneNumber);
                            emailTextView.setText(email);
                        }

                    }

                });
    }
}