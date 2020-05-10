package com.pafloca.greenguy;

import android.os.Bundle;

<<<<<<< HEAD

=======
>>>>>>> 8ab990d9b75bda6a7fd11f81d78a8f057d2c81ee
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
/**
 * Activité : ajouter un événement.
 * A remplir
 */
=======


>>>>>>> 8ab990d9b75bda6a7fd11f81d78a8f057d2c81ee
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
