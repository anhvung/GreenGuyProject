package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import usefulclasses.ClientConnexion;

import static usefulclasses.ClientConnexion.sep;

//!  Activité : ajouter un point d'intérêt.
/*!
  A remplir
*/

public class AddPoiActivity extends AppCompatActivity {

    LatLng position;
    TextView ajouter;
    String address ;
    String type ;
    String fulladdress ;
    String titre;
    String desc;
    String lieu;
    int storedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poi);

        Intent intent = getIntent();
        Bundle bundle = intent.getParcelableExtra("bundle");
        position = bundle.getParcelable("position");
        Log.d("greend", String.valueOf(position.latitude));
        Log.d("greend", String.valueOf(position.longitude));
        new AddPoiActivity.getAddress().execute();

        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);

        ajouter =  findViewById(R.id.poi_ajouter_button);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText add=findViewById(R.id.address);
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

        Geocoder geocoder = new Geocoder(AddPoiActivity.this);
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
        EditText titre_item= findViewById(R.id.ajout_poi_titre);
        titre=titre_item.getText().toString();

        EditText desc_item= findViewById(R.id.descrption);
        desc=desc_item.getText().toString();

        EditText lieu_item= findViewById(R.id.address);
        lieu=lieu_item.getText().toString();

        Spinner spinner =  findViewById(R.id.spinner);
        type=spinner.getSelectedItem().toString();


        new AddPoiActivity.uploadEvent().execute();
        //finish();
    }
    private class uploadEvent extends AsyncTask<Void,Void,Void> {

        String response_pic;
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<String> params=new  ArrayList<>();
            params.add("'"+type+"'");
            params.add("'"+titre+"'");
            params.add("'"+desc+"'");
            params.add("'"+lieu+"'");
            params.add(String.valueOf(0));
            params.add(String.valueOf(0));
            params.add(String.valueOf(storedId));
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0018",format(params));
            response_pic=connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(response_pic.equals("true")){
                Intent setIntent = new Intent(AddPoiActivity.this,MapsActivity.class);
                startActivity(setIntent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Champs invalides",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String format(ArrayList<String> liste) {
        String ret="";
        for (String s:liste) {
            ret+=s+sep;
        }
        if (ret.length() - sep.length()>=0)
            return ret.substring(0, ret.length() - sep.length());
        else
            return"";
    }
    private class getAddress extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(AddPoiActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
              /*city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

               */
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
            EditText add=findViewById(R.id.address);
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
