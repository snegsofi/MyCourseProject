package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity {

    Button signUp_btn;
    Button signIn_btn;

    TextInputLayout loginLayout;
    TextInputLayout passwordLayout;
    EditText passwordTxt;
    EditText loginTxt;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_fragment);


//        setInitialData();
//
//        for(int i=0;i<parentModelClassList.size();i++){
//            writeDishes(parentModelClassList.get(i).getCategoryName(),
//                    parentModelClassList.get(i).getDishList());
//        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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
                    signIn(loginTxt.getText().toString(),passwordTxt.getText().toString());
                    //Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    //startActivity(intent);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() { }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            toNewActivity(loginTxt.getText().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {

    }

    private void toNewActivity(String waiter){
        Intent intent=new Intent(SignInActivity.this,MainActivity.class);
        intent.putExtra("waiter", waiter);
        Log.d("waiterSignInActivity",waiter);
        startActivity(intent);
    }


//    FirebaseFirestore db;
//
//    private void writeDishes(String categoryName, List<Dish> dishList){
//
//        db = FirebaseFirestore.getInstance();
//
//        Map<String, Object> dish = new HashMap<>();
//        dish.put("categoryName", categoryName);
//        dish.put("dishList", dishList);
//
//
//
//
//        // Add a new document with a generated ID
//        db.collection("Dishes")
//                .add(dish)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
//
//
//    List<FoodCategory> parentModelClassList;
//
//    List<Dish> breakfastList;
//    List<Dish> saladList;
//    List<Dish> mainDishList;
//    List<Dish> dessertList;
//    List<Dish> drinkList;
//
//    public void setInitialData() {
//
//        parentModelClassList=new ArrayList<>();
//        breakfastList=new ArrayList<>();
//        saladList=new ArrayList<>();
//        mainDishList=new ArrayList<>();
//        dessertList=new ArrayList<>();
//        drinkList=new ArrayList<>();
//
//
//        breakfastList.add(new Dish(349, "Английский завтрак", R.drawable.img, 0));
//        breakfastList.add(new Dish(319, "Morning plate #1", R.drawable.img_1, 0));
//        breakfastList.add(new Dish(319, "Омлет с фермерским окороком и сыром", R.drawable.img_2, 0));
//        breakfastList.add(new Dish(439, "Омлет с форелью и сливочным сыром", R.drawable.img_3, 0));
//        breakfastList.add(new Dish(279, "Гречка с беконом и яйцом пашот", R.drawable.img_4, 0));
//        breakfastList.add(new Dish(299, "Сырники со сметаной", R.drawable.img_5, 0));
//        breakfastList.add(new Dish(299, "Сырника с соусом крем брюле", R.drawable.img_6, 0));
//        breakfastList.add(new Dish(149, "Блинчики со сливочным маслом", R.drawable.img_7, 0));
//        breakfastList.add(new Dish(149, "Глазунья", R.drawable.img_8, 0));
//        breakfastList.add(new Dish(149, "Каша рисовая", R.drawable.img_9, 0));
//        breakfastList.add(new Dish(149, "Каша овсяная", R.drawable.img_10, 0));
//        breakfastList.add(new Dish(179, "Пшенная каша с тыквой", R.drawable.img_11, 0));
//        breakfastList.add(new Dish(439, "Жареный творог", R.drawable.img_12, 0));
//        breakfastList.add(new Dish(449, "Боул", R.drawable.img_13, 0));
//        breakfastList.add(new Dish(149, "Каша пшенная", R.drawable.img_14, 0));
//        breakfastList.add(new Dish(379, "Гриль сэндвич с курицей", R.drawable.img_15, 0));
//
//
//        parentModelClassList.add(new FoodCategory("Завтраки", breakfastList));
//
//        saladList.add(new Dish(349, "Салат цезарь с куриной грудкой", R.drawable.img_16, 0));
//        saladList.add(new Dish(429, "Салат цезарь с креветками", R.drawable.img_17, 0));
//        saladList.add(new Dish(249, "Салат с паштетом из куриной печени", R.drawable.img_18, 0));
//        saladList.add(new Dish(419, "Салат с пастрами", R.drawable.img_19, 0));
//        saladList.add(new Dish(349, "Салат с печеной свеклой", R.drawable.img_20, 0));
//
//
//        parentModelClassList.add(new FoodCategory("Салаты", saladList));
//
//
//        mainDishList.add(new Dish(349, "Строганов из индейки с грибами и гречей", R.drawable.img_21, 0));
//        mainDishList.add(new Dish(349, "Оладьи из курицы с картофельным пюре", R.drawable.img_22, 0));
//        mainDishList.add(new Dish(519, "Паста с милиями и креветками", R.drawable.img_23, 0));
//        mainDishList.add(new Dish(349, "Паста Карбонара", R.drawable.img_24, 0));
//        mainDishList.add(new Dish(449, "Киноа с тыквой", R.drawable.img_25, 0));
//        mainDishList.add(new Dish(379, "Жареные сосиски", R.drawable.img_26, 0));
//        mainDishList.add(new Dish(379, "Паста с индейкой, тыквой и печеным баклажаном", R.drawable.img_27, 0));
//
//
//        parentModelClassList.add(new FoodCategory("Горячие блюда", mainDishList));
//
//        dessertList.add(new Dish(179, "Пирожное Наполеон", R.drawable.img_28, 0));
//        dessertList.add(new Dish(329, "Тарталетка с клубникой", R.drawable.img_29, 0));
//        dessertList.add(new Dish(379, "Тарталетка с лесными ягодами", R.drawable.img_30, 0));
//        dessertList.add(new Dish(169, "Пирожное Сметанник", R.drawable.img_31, 0));
//        dessertList.add(new Dish(249, "Пирожное Трюфельное", R.drawable.img_32, 0));
//        dessertList.add(new Dish(179, "Пирожное Красный бархат", R.drawable.img_33, 0));
//        dessertList.add(new Dish(69, "Макарон фисташка", R.drawable.img_34, 0));
//        dessertList.add(new Dish(69, "Макарон соленая карамель", R.drawable.img_35, 0));
//
//        parentModelClassList.add(new FoodCategory("Десерты", dessertList));
//
//        drinkList.add(new Dish(119, "Вода Байкал газированная", R.drawable.img_36, 0));
//        drinkList.add(new Dish(119, "Вода Жемчужина негазированная", R.drawable.img_37, 0));
//        drinkList.add(new Dish(149, "Морс Бодрый Клубничный", R.drawable.img_38, 0));
//        drinkList.add(new Dish(149, "Морс Бодрый Клюквенный", R.drawable.img_39, 0));
//        drinkList.add(new Dish(149, "Морс Бодрый Клубника-банан", R.drawable.img_40, 0));
//        drinkList.add(new Dish(149, "Морс Бодрый Манго-маракуйя", R.drawable.img_41, 0));
//        drinkList.add(new Dish(149, "Морс Бодрый Ягодный", R.drawable.img_42, 0));
//
//        parentModelClassList.add(new FoodCategory("Напитки", drinkList));
//
//    }
}
