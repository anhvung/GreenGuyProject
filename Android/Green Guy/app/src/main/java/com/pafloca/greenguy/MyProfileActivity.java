package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ImageView photo = findViewById(R.id.my_page_profil_image);
        TextView age = findViewById(R.id.age);
        TextView nom = findViewById(R.id.nom);
        TextView lieu = findViewById(R.id.lieu);
        update_profile();

    }

    private void update_profile() {

    }

}
