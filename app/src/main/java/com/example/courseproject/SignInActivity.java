package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    Button signUp_btn;
    Button signIn_btn;

    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_fragment);

        signUp_btn=findViewById(R.id.singUpButton);
        signIn_btn=findViewById(R.id.loginButton);
        loginTxt=findViewById(R.id.loginEditText);
        loginLayout=findViewById(R.id.textField);
        passwordTxt=findViewById(R.id.passwordEditText);
        passwordLayout=findViewById(R.id.textField1);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginTxt.getText().toString().isEmpty()){
                    showError(loginLayout,"Поле обязательно для заполнения");
                }
                else if(passwordTxt.getText().toString().isEmpty()){
                    showError(passwordLayout,"Поле обязательно для заполнения");
                }
                else {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        loginTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!loginTxt.getText().toString().isEmpty()){
                    hideError(loginLayout);
                }
            }
        });

        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!passwordTxt.getText().toString().isEmpty()){
                    hideError(passwordLayout);
                }
            }
        });


    }

    private void showError(TextInputLayout textInputLayout, String string) {
        textInputLayout.setError(string);
    }

    private void hideError(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
    }
}
