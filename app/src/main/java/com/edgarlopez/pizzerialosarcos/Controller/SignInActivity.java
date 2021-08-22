package com.edgarlopez.pizzerialosarcos.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.edgarlopez.pizzerialosarcos.R;

public class SignInActivity extends AppCompatActivity {
    private Button nextButton;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        nextButton = findViewById(R.id.next_signin_button);
        nameEditText = findViewById(R.id.name_edit_text);
        lastNameEditText = findViewById(R.id.lastname_edit_text);
        emailEditText = findViewById(R.id.email_account);

        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    nextButton.callOnClick();
                }
                return false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String lastname = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (!name.matches("[A-Za-zÀ-ÿ '-]*")) {
                    nameEditText.setError("El nombre introducido no es válido");
                    return;
                }

                else if (!lastname.matches("[A-Za-zÀ-ÿ '-]*")) {
                    lastNameEditText.setError("El apellido introducido no es válido");
                    return;
                }

                else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    emailEditText.setError("El email introducido no es válido");
                    return;
                }else {
                    AppController.getInstance().setName(name);
                    AppController.getInstance().setLastName(lastname);
                    AppController.getInstance().setEmail(email);

                    startActivity(new Intent(SignInActivity.this,
                            RegisterNumberActivity.class));
                }
            }
        });
    }

}