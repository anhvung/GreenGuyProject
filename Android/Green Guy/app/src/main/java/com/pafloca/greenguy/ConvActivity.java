package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);
        test();
        mRecyclerView =findViewById(R.id.reyclerview_message_list);
        adapter= new MainAdapter(dates,msg,pic,hisname,myname,this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button button_chatbox_send=findViewById(R.id.button_chatbox_send);
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                EditText edittext_chatbox=findViewById(R.id.edittext_chatbox);
                dates.add("new date");
                String input="1"+edittext_chatbox.getText().toString();
                msg.add(input);

                adapter.notifyDataSetChanged();
            }
        });

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
}
