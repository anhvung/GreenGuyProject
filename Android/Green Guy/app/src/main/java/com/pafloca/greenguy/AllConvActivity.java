package com.pafloca.greenguy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.IdentificationActivity.sep;

public class AllConvActivity extends AppCompatActivity {
    String[] response_name;
    String[] response_id;
    String[] response_pic;
    int storedId;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager lm;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_conv);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AllConvActivity.this, NewConv.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        new getConvs().execute();

    }

    private class getConvs extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0009",String.valueOf(storedId));
            response_id=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0010",encode(response_id));
            if(response_id[0]==""||response_id.length==0||response_id==null){

            }
            else{
                response_name=connect.magicSauce();
                connect= new ClientConnexion("192.168.1.17",2345,"0011",encode(response_id));
                response_pic=connect.magicSauce();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView =findViewById(R.id.convlist);
            adapter= new ConvAdapter(response_id,response_name,response_pic,AllConvActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AllConvActivity.this));
            mRecyclerView.setAdapter(adapter);


        }
    }
    public String encode(String[] liste){
        String ret="";
        for(String s:liste){
            ret+=s+sep;
        }
        return ret.substring(0,ret.length()-sep.length());
    }
}
