package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    Button toLogin_btn;
    Button signUp_btn;

    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;
    TextInputLayout nameLayout;
    EditText nameTxt;
    TextInputLayout surnameLayout;
    EditText surnameTxt;


    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_fragment);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        toLogin_btn=findViewById(R.id.toLoginButton);
        signUp_btn=findViewById(R.id.signupButton);
        loginTxt=findViewById(R.id.loginEditText2);
        loginLayout=findViewById(R.id.textField3);
        passwordTxt=findViewById(R.id.passwordEditText2);
        passwordLayout=findViewById(R.id.textField4);
        nameTxt=findViewById(R.id.nameEditText);
        nameLayout=findViewById(R.id.textField1);
        surnameTxt=findViewById(R.id.surnameEditText);
        surnameLayout=findViewById(R.id.textField2);

        toLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameTxt.getText().toString().isEmpty()){
                    showError(nameLayout,"Поле обязательно для заполнения");
                }
                else if(surnameTxt.getText().toString().isEmpty()){
                    showError(surnameLayout,"Поле обязательно для заполнения");
                }
                else if(loginTxt.getText().toString().isEmpty()){
                    showError(loginLayout,"Поле обязательно для заполнения");
                }
                else if(passwordTxt.getText().toString().isEmpty()){
                    showError(passwordLayout,"Поле обязательно для заполнения");
                }
                else{
                    addUser(nameTxt.getText().toString(),
                            surnameTxt.getText().toString(),
                            loginTxt.getText().toString());
                    createAccount(loginTxt.getText().toString(),passwordTxt.getText().toString());
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

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!nameTxt.getText().toString().isEmpty()){
                    hideError(nameLayout);
                }
            }
        });

        surnameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!surnameTxt.getText().toString().isEmpty()){
                    hideError(surnameLayout);
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

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            toNewActivity(email);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }


    private void updateUI(FirebaseUser user) {

    }

    private void toNewActivity(String waiter){
        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
        intent.putExtra("waiter", waiter);
        Log.d("waiterSignInActivity",waiter);
        startActivity(intent);
    }

    private void reload() { }


    private void addUser(String name,String surname, String email){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Surname", surname);
        user.put("Email", email);
        user.put("Id", UUID.randomUUID().toString());

        // Add a new document with a generated ID
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


}
