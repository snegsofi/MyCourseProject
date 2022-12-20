package com.example.courseproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {


    public HomeFragment(){

    }

    private static final String ARG_PARAM1="param1";

    public static HomeFragment newInstance(String login)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,login);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    String login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            login = getArguments().getString(ARG_PARAM1);
        }
    }

    RadioGroup radioGroup;
    RadioButton russianRadioButton;
    RadioButton englishRadioButton;
    LinearLayout exitLinearLayout;

    TextView nameTextView;
    TextView surnameTextView;
    TextView loginTextView;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment,container,false);

        db = FirebaseFirestore.getInstance();
        radioGroup=view.findViewById(R.id.languageRadioGroup);
        russianRadioButton=view.findViewById(R.id.russianRadioButton);
        englishRadioButton=view.findViewById(R.id.englishRadioButton);
        exitLinearLayout=view.findViewById(R.id.exitLinearLayout);

        nameTextView=view.findViewById(R.id.nameTextViewHome);
        surnameTextView=view.findViewById(R.id.surnameTextViewHome);
        loginTextView=view.findViewById(R.id.loginTextViewHome);

        readData(login);

        exitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });


        russianRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad  = "ru";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,getResources().getDisplayMetrics());
                
            }
        });

        englishRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageToLoad  = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config,getResources().getDisplayMetrics());
            }
        });
        return view;
    }

    private void readData(String login){

        db.collection("Users")
                .whereEqualTo("Email",login)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
////

                                nameTextView.setText(document.get("Name").toString());
                                surnameTextView.setText(document.get("Surname").toString());
                                loginTextView.setText(document.get("Email").toString());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}
