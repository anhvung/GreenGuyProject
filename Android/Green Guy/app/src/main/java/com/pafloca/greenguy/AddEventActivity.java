package com.pafloca.greenguy;

import android.os.Bundle;
<<<<<<< HEAD
//!  Activité : ajouter un événement.
/*!
  A remplir
*/
=======
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

>>>>>>> 4813d7323bd290424336f1153141e2d9c0d2e6b1
public class AddEventActivity extends AppCompatActivity {

    private EditText titre;
    private EditText date;
    private EditText lieu;
    private TextView terminer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        titre = findViewById(R.id.ajout_evenement_titre);
        date = findViewById(R.id.ajout_evenement_date);
        lieu = findViewById(R.id.ajout_evenement_lieu);
        terminer = findViewById(R.id.ajout_evenement_publier);
    }
}
