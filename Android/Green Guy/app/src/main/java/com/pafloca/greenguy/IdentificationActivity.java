package com.pafloca.greenguy;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Page identification
 */
public class IdentificationActivity extends AppCompatActivity {

    private EditText nom_utilisateur_ou_email;
    private EditText mdp;
    private TextView senregistrer;
    private TextView sidentifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        nom_utilisateur_ou_email = findViewById(R.id.sidentifier_nom_utilisateur_ou_email);
        mdp = findViewById(R.id.sidentifier_mdp);

        senregistrer = findViewById(R.id.sidentifier_switch_to_senregistrer);
        sidentifier = findViewById(R.id.sidentifier_terminer);
    }
}
