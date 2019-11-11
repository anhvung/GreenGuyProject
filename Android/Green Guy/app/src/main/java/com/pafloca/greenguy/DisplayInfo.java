package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class DisplayInfo extends AppCompatActivity {
    private Map<String,String> dic =new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);
        TextView info=(TextView) findViewById(R.id.textView);
        dic.put("MOI","");
        dic.put("poubelle recyclable","Cette poubelle recycle tout déchet plastique");
        dic.put("Compost","Le poubelle est située dans le jardin commuautaire");
        dic.put("Event : Atelier DIY","Découvrir comment convertir un écran lcd cassé en une source de lumière naturelle ! \n Programmée le Samedi 16 novembre");
        dic.put("Eco-friendly","Cuisine uniquement fondée sur les produits locaux !");
        dic.put("Information","Télécom Paris, bâtiment à haute efficacité énergétique");
        dic.put("point d'eau","");
        Intent intent = getIntent();
        String message = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        info.setText(dic.get(message));
    }
}
