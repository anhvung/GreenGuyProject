package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import usefulclasses.ClientConnexion;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static usefulclasses.ClientConnexion.sep;

public class ConvActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager lm;
    RecyclerView.Adapter adapter;
    Bitmap pic;
    ArrayList<String> dates= new ArrayList<String>();
    ArrayList<String> msg= new ArrayList<String>();
    String hisname="name";
    String myname;
    int myid;
    int hisid;
    String monMsg;
    private static final String ALLCONVID="FROFILE_ID65468787484447";
    private static final String sep2="!sepPourlEsmSg?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);
        Intent intent = getIntent();
        String message = intent.getStringExtra(ALLCONVID);
        hisid = Integer.parseInt(message);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        myid = sharedPref.getInt("USER_ID", -1);
        new CheckNewConv().execute();
        //test();


    }
    private class CheckNewConv extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0012", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid)));
            connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new getAllMsg().execute();
        }
    }
    private class getAllMsg extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0013", String.valueOf(hisid));
            String[] pics = connect.magicSauce();
            pic=Base64toBitmap(pics[0]);
            connect = new ClientConnexion("192.168.1.17", 2345, "0014", String.valueOf(hisid));
            hisname = connect.magicSauce()[0];
            connect = new ClientConnexion("192.168.1.17", 2345, "0014", String.valueOf(myid));
            myname = connect.magicSauce()[0];
            connect = new ClientConnexion("192.168.1.17", 2345, "0015", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid)));
            dates= new ArrayList<>(Arrays.asList(connect.magicSauce()));
            connect = new ClientConnexion("192.168.1.17", 2345, "0016", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid)));
            msg= new ArrayList<>(Arrays.asList(connect.magicSauce()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView =findViewById(R.id.reyclerview_message_list);
            adapter= new MainAdapter(myid,dates,msg,pic,hisname,myname,ConvActivity.this);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ConvActivity.this));
            Button button_chatbox_send=findViewById(R.id.button_chatbox_send);
            button_chatbox_send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    EditText edittext_chatbox=findViewById(R.id.edittext_chatbox);
                    monMsg=myid+sep2+edittext_chatbox.getText().toString();
                    edittext_chatbox.getText().clear();
                    new sendMsg().execute();
                }
            });
        }
    }

    private class sendMsg extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String ladate=java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0017", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid))+sep+monMsg+sep+"0"+sep+ladate);
            connect.magicSauce();
            Log.d("greend", "send");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("greend", "updating updateMsg length : "+dates.size());
            new updateNewMsg().execute();

        }
    }
    private class updateNewMsg extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0015", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid)));
            ArrayList<String> newdates =new ArrayList<>(Arrays.asList(connect.magicSauce()));
            dates.add(newdates.get(newdates.size() - 1));
            connect = new ClientConnexion("192.168.1.17", 2345, "0016", String.valueOf(min(myid,hisid))+ sep+String.valueOf(max(myid,hisid)));
            ArrayList<String> newmsg = new ArrayList<>(Arrays.asList(connect.magicSauce()));
            msg.add(newmsg.get(newmsg.size() - 1));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("greend", "updating recycler view length : "+dates.size());
            adapter.notifyDataSetChanged();
        }
    }
    private void test() {
        dates.add("10:30");
        dates.add("11:30");
        dates.add("10:35");
        dates.add("10:60");
        msg.add("1hello");
        msg.add("2hello how are you");
        msg.add("1im fine");
        msg.add("2Dinner 2night?");
       hisname="firend name";
       myname="mememe";
       pic= BitmapFactory.decodeResource(getResources(), R.drawable.ic_friends);
    }
    public Bitmap Base64toBitmap(String pp){
        byte[] decodedString = Base64.decode(pp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
