package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import usefulclasses.ClientConnexion;

import static java.lang.Math.min;

public class MyProfileActivity extends AppCompatActivity {
    String[] response;
    String sendMsg;
    int storedId;
    TextView age;
    ImageView photo;
    TextView nom;
    TextView lieu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        photo = findViewById(R.id.my_page_profil_image);
        age = findViewById(R.id.age);
        nom = findViewById(R.id.nom);
        lieu = findViewById(R.id.lieu);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        new getInfo().execute();
    }

    public void updateInfo(){
        nom.setText(response[0]);
        age.setText(response[1]);
        if (response[2]=="" ||response[3].substring(0,min(4,response[3].length())).contains("null")|| response[2]==null ||response[2].isEmpty()){
            lieu.setText("non renseigné");
        }
        else{
            lieu.setText(response[2]);
        }
        if (response[3]=="" ||response[3].substring(0,min(4,response[3].length())).contains("null")|| response[3]==null ||response[3].isEmpty()){
            Log.d("greend", "NO PICTURE FOUND");
        }



    }
    private class getInfo extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0004",String.valueOf(storedId));
            response=connect.magicSauce();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateInfo();
        }
    }
}
