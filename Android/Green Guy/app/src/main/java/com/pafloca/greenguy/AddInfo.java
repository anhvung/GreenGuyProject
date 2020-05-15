package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import usefulclasses.ClientConnexion;

import static usefulclasses.ClientConnexion.sep;

public class AddInfo extends AppCompatActivity {
    int storedId;
    String titre;
    String lien;
    String response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);

    }

    public void ajout(View view) {
        EditText edittitre = findViewById(R.id.ajout_inf_titre);
        titre=edittitre.getText().toString();
        EditText editlien = findViewById(R.id.ajout_lien);
        lien=editlien.getText().toString();
        new publish().execute();

    }
    private class publish extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<String> params=new  ArrayList<>();
            long time= System.currentTimeMillis();
            params.add(String.valueOf(storedId));
            params.add("'"+titre+"'");
            params.add("'"+lien+"'");
            params.add(String.valueOf(time));
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0023",format(params));
            response=connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(response.equals("true")){
                finish();
            }
            else{
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
}
