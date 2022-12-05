package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                    startActivity(new Intent(SplashActivity.this,SignInActivity.class));
                    finish();
                }
                catch (Exception e){

                }

            }
        }; thread.start();
    }
}
