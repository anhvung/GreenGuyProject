package com.pafloca.greenguy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import usefulclasses.ClientConnexion;

public class ListOfEventsActivity extends AppCompatActivity {
    String[] ids;
String[] titre;
    String[] dates;
    String[] types;
    String[] datesdeb;
    String[] datesfin;
    private static final int TAG = 421206948;
int storedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        new getmyEvents().execute();


    }
    private class getmyEvents extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0041", String.valueOf(storedId));
            ids = connect.magicSauce();
            connect = new ClientConnexion("192.168.1.17", 2345, "0042", String.valueOf(storedId));
            titre = connect.magicSauce();
            connect = new ClientConnexion("192.168.1.17", 2345, "0043", String.valueOf(storedId));
            datesdeb = connect.magicSauce();
            connect = new ClientConnexion("192.168.1.17", 2345, "0044", String.valueOf(storedId));
            datesfin = connect.magicSauce();
        return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LinearLayout ll=findViewById(R.id.list_of_eventsll);
            for (int i=0;i<ids.length;i++){
                LinearLayout item=new LinearLayout(ListOfEventsActivity.this);
                item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                item.setOrientation(LinearLayout.VERTICAL);
                item.setGravity(Gravity.CENTER_VERTICAL );
                LinearLayout datell=new LinearLayout(ListOfEventsActivity.this);
                datell.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                datell.setOrientation(LinearLayout.HORIZONTAL);
                datell.setGravity(Gravity.CENTER_VERTICAL );

                TextView title=new TextView(ListOfEventsActivity.this);
                title.setText(titre[i]);

                TextView datedeb=new TextView(ListOfEventsActivity.this);
                datedeb.setText(getDate(Long.parseLong(datesdeb[i]), "dd/MM/yyyy hh:mm")+"     ");

                TextView dateFin=new TextView(ListOfEventsActivity.this);
                dateFin.setText(getDate(Long.parseLong(datesfin[i]), "dd/MM/yyyy hh:mm"));

                datell.addView(datedeb);
                datell.addView(dateFin);

                item.addView(title);
                item.addView(datell);
                item.setTag(TAG,ids[i]);
                item.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        Intent intent=new Intent(ListOfEventsActivity.this,DisplayGeneralEvent.class);
                        intent.putExtra(MapsActivity.EXTRA_MESSAGE, String.valueOf( v.getTag(TAG)));
                        startActivity(intent);
                    }
                });
                ll.addView(item);


            }
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
