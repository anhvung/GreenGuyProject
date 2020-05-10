package com.pafloca.greenguy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import usefulclasses.ClientConnexion;

import static java.lang.Boolean.parseBoolean;


/**
 * création de profil.
 *
 */

public class CreateProfileActivity extends AppCompatActivity {

    private EditText nom_utilisateur;
    private EditText email;
    private EditText mdp;
    private EditText age;
    private TextView senregistrer;
    private TextView sidentifier;
    public static final String PREF= "PREF";
    public static final String sep="!@@!!";
    boolean allParamValid = false;
    String sendMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        nom_utilisateur = findViewById(R.id.senregistrer_nom_utilisateur);
        email = findViewById(R.id.senregistrer_email);
        mdp = findViewById(R.id.senregistrer_mdp);
        age = findViewById(R.id.senregistrer_age);

        senregistrer = findViewById(R.id.senregistrer_terminer);
        senregistrer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                sendMsg="'"+nom_utilisateur.getText().toString()+"'"+sep+"'"+email.getText().toString()+"'"+sep+"'"+mdp.getText().toString()+"'"+sep+age.getText().toString();
                new ServerFirstTime().execute();
            }
        });
        sidentifier = findViewById(R.id.senregistrer_switch_to_sidentifier);
        sidentifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent intent = new Intent(v.getContext(), IdentificationActivity.class);

                startActivity(intent);
            }
        });
    }
    private class ServerFirstTime extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0001",sendMsg);
            sendMsg=connect.magicSauce();
            allParamValid=parseBoolean(sendMsg)    ;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (allParamValid){
                Toast.makeText(CreateProfileActivity.this, "Compte créé !",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateProfileActivity.this, IdentificationActivity.class);

                startActivity(intent);
            }
            else{
                Toast.makeText(CreateProfileActivity.this, sendMsg,
                        Toast.LENGTH_LONG).show();
            }


        }
    }

}
