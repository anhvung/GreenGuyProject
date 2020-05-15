package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import usefulclasses.ClientConnexion;

public class DisplayGeneralEvent extends AppCompatActivity {
    String id;
    int storedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_general_event);

        Intent intent = getIntent();
         id = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        new Display().execute();

    }
    private class Display extends AsyncTask<Void,Void,Void> {
        String[] response;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0027",id);
            response =connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("greend", response[1]);
            TextView titre= findViewById(R.id.titre);
            TextView descr= findViewById(R.id.descr);
            TextView type= findViewById(R.id.type);
            TextView deb= findViewById(R.id.deb);
            TextView fin= findViewById(R.id.fin);
            TextView auteur= findViewById(R.id.auteur);
            titre.setText(response[0]);
            descr.setText(response[1]);
            type.setText(response[2]);

            deb.setText(getDate(Long.parseLong(response[3]), "dd/MM/yyyy hh:mm"));
            fin.setText(getDate(Long.parseLong(response[4]), "dd/MM/yyyy hh:mm"));
            auteur.setText(response[5]);
            super.onPostExecute(aVoid);
        }
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
