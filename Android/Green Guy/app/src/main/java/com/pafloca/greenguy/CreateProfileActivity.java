package com.pafloca.greenguy;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
/**
 * création de profil.
 *
 */
public class CreateProfileActivity extends AppCompatActivity {

    private EditText nom_utilisateur;
    private EditText email;
    private EditText mdp;
    private EditText age;
    private TextView senregistrer;
    private TextView sidentifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        nom_utilisateur = findViewById(R.id.senregistrer_nom_utilisateur);
        email = findViewById(R.id.senregistrer_email);
        mdp = findViewById(R.id.senregistrer_mdp);
        age = findViewById(R.id.senregistrer_age);

        senregistrer = findViewById(R.id.senregistrer_terminer);
        sidentifier = findViewById(R.id.senregistrer_switch_to_sidentifier);
    }
}
