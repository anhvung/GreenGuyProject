package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Représente un point d'intérêt.
 * A remplir
 */

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//!  Activité : ajouter un point d'intérêt.
/*!
  A remplir
*/

public class AddEventActivity extends AppCompatActivity {

    String titre = "Nouveau POI";
    LatLng position;
    GoogleMap mMap;
    TextView ajouter;
    String address ;
    String city ;
    String state ;
    String country ;
    String postalCode;
    String knownName ;
    String fulladdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        Bundle bundle = intent.getParcelableExtra("bundle");
        position = bundle.getParcelable("position");
        Log.d("greend", String.valueOf(position.latitude));
        Log.d("greend", String.valueOf(position.longitude));

        new getAddress().execute();
        ajouter =  findViewById(R.id.ajout_evenement_publier);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText add=findViewById(R.id.ajout_evenement_lieu);
                fulladdress=add.getText().toString();
                if (notFar(fulladdress)){
                    Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
                    publier();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Address is too far from chosen location",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean notFar(String fulladdress) {
        double latitude=0;
        double longitude=0;
        int maxdis=2000;

        Geocoder geocoder = new Geocoder(AddEventActivity.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(fulladdress, 1);
            if(addresses.size() > 0) {
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
            }
            if(distance(position.latitude,position.longitude,latitude,longitude)<maxdis){
                Log.d("greend", "distance : "+distance(position.latitude,position.longitude,latitude,longitude));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void publier() {
        //code qui enregistre les données entrés pour ajouter un nouveau POI
        //puis fermeture de l'activité
        //finish();
    }
    private class getAddress extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(AddEventActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            } catch (IOException e) {
                Log.d("greend", "error");
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fulladdress=address;
            EditText add=findViewById(R.id.ajout_evenement_lieu);
            add.setText(fulladdress);
            Log.d("greend", "adresses ::::   "+fulladdress);


        }
    }
    public float distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
