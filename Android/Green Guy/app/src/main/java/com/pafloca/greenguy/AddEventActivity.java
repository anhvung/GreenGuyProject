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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
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

public class AddEventActivity extends AppCompatActivity {


    LatLng position;
    GoogleMap mMap;
    TextView ajouter;
    String address ;
    String fulladdress ;
    String titre;
    String desc;
    String lieu;
    Date deb;
    Date fin;
    int storedId;
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

        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);

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
        EditText titre_item= findViewById(R.id.ajout_evenement_titre);
        titre=titre_item.getText().toString();

        EditText desc_item= findViewById(R.id.ajout_evenement_description);
        desc=desc_item.getText().toString();

        EditText lieu_item= findViewById(R.id.ajout_evenement_lieu);
        lieu=lieu_item.getText().toString();

        DatePicker datedeb=findViewById(R.id.datedebcal);
        TimePicker timedeb=findViewById(R.id.datePicker);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
           deb=new Date(datedeb.getYear(),datedeb.getMonth(),datedeb.getDayOfMonth(),timedeb.getHour(),timedeb.getMinute());
        }
        else{
            deb=new Date(datedeb.getYear(),datedeb.getMonth(),datedeb.getDayOfMonth(),timedeb.getCurrentHour(),timedeb.getCurrentMinute());
        }
        DatePicker datefin=findViewById(R.id.date_Fin_cal);
        TimePicker timefin=findViewById(R.id.datePickerFin);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fin=new Date(datefin.getYear(),datefin.getMonth(),datefin.getDayOfMonth(),timefin.getHour(),timefin.getMinute());
        }
        else{
            fin=new Date(datefin.getYear(),datefin.getMonth(),datefin.getDayOfMonth(),timefin.getCurrentHour(),timefin.getCurrentMinute());
        }

        new uploadEvent().execute();
        //finish();
    }
    private class uploadEvent extends AsyncTask<Void,Void,Void> {

        String response_pic;
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<String> params=new  ArrayList<>();
            params.add("'event'");
            params.add("'"+titre+"'");
            params.add("'"+desc+"'");
            params.add("'"+lieu+"'");
            params.add(String.valueOf(deb.getTime()));
            params.add(String.valueOf(fin.getTime()));
            params.add(String.valueOf(storedId));
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0018",format(params));
            response_pic=connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(response_pic.equals("true")){
                finish();
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
            geocoder = new Geocoder(AddEventActivity.this, Locale.getDefault());

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
