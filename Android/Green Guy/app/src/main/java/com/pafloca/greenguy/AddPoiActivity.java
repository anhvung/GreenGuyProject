package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
/**
 * Représente un point d'intérêt.
 * A remplir
 */
=======
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//!  Activité : ajouter un point d'intérêt.
/*!
  A remplir
*/
>>>>>>> 8ab990d9b75bda6a7fd11f81d78a8f057d2c81ee
public class AddPoiActivity extends AppCompatActivity {

    String titre = "Nouveau POI";
    LatLng position;
    GoogleMap mMap;
    TextView ajouter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        position = bundle.getParcelable("position");

        ajouter = (TextView) findViewById(R.id.poi_ajouter);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publier();
            }
        });

    }

    private void publier() {
        //code qui enregistre les données entrés pour ajouter un nouveau POI
        //puis fermeture de l'activité
        finish();
    }

}
