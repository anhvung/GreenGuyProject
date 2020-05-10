package com.pafloca.greenguy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import usefulclasses.ClientConnexion;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Page identification
 */

public class IdentificationActivity extends AppCompatActivity {

    private EditText email;
    private EditText mdp;
    private TextView senregistrer;
    private TextView sidentifier;
    public static final String sep;

    static {
        sep = "!@@!!";
    }

    String sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        email = findViewById(R.id.sidentifier_nom_utilisateur_ou_email);
        mdp = findViewById(R.id.sidentifier_mdp);

        senregistrer = findViewById(R.id.sidentifier_switch_to_senregistrer);
        senregistrer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                Intent intent = new Intent(v.getContext(), CreateProfileActivity.class);
                startActivity(intent);
            }
        });
        sidentifier = findViewById(R.id.sidentifier_terminer);
        sidentifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                sendMsg="'"+email.getText().toString()+"'"+sep+mdp.getText().toString();
                new login().execute();
            }
        });
    }
    private class login extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0002",sendMsg);
            sendMsg=connect.magicSauce();
            String[] responseList = sendMsg.split(sep, -1);
            Log.d("greend", sendMsg);
            if (responseList.length!=1){
                SharedPreferences sharedPref = getSharedPreferences("SAVE",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("USER_ID",Integer.parseInt(responseList[0]));
                editor.putString("USER_NAME", responseList[1]);
                editor.commit();
                Intent intent = new Intent(IdentificationActivity.this,MapsActivity.class);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (sendMsg.equals("false")){
                Toast.makeText(IdentificationActivity.this, "wrong psd try again",
                        Toast.LENGTH_SHORT).show();
            }
            else if (sendMsg.equals("err")){
                Toast.makeText(IdentificationActivity.this, "error try again",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(IdentificationActivity.this, "Hello again !",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
