package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
                int storedId = sharedPref.getInt("USER_ID", -1);
                if (storedId==-1){
                    Intent intent=new Intent(SplashScreen.this, CreateProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(SplashScreen.this, MapsActivity.class);
                    startActivity(intent);
                }
            }

        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.splash_layout);
                bgElement.setBackgroundColor(Color.BLACK);
            }

        }, 300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.splash_layout);
                bgElement.setBackgroundColor(Color.BLUE);
            }

        }, 600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.splash_layout);
                bgElement.setBackgroundColor(Color.YELLOW);
            }

        }, 900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.splash_layout);
                bgElement.setBackgroundColor(Color.GREEN);
            }

        }, 1200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.splash_layout);
                bgElement.setBackgroundColor(Color.WHITE);
            }

        }, 1500);


    }
}
