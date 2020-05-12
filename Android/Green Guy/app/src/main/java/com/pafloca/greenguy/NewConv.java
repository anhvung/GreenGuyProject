package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.MyProfileActivity.drawableToBitmap;

public class NewConv extends AppCompatActivity {
    String[] friendsId;
    String [] friendsPic;
    String [] friendsName;
    int storedId;
    Bitmap defaultPic;
    private static final int TAG2 = R.id.TAG2;
    private static final String ALLCONVID="FROFILE_ID65468787484447";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conv);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        Drawable d = getResources().getDrawable(R.mipmap.default_profile_foreground);
        defaultPic=drawableToBitmap(d);
        new getFriends().execute();
    }
    private void setFriends() {
        LinearLayout listLayout=findViewById(R.id.newconvlayout);

        for (int i=0;i<friendsId.length;i++){
            LinearLayout item=new LinearLayout(NewConv.this);
            item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setGravity(Gravity.CENTER_VERTICAL );

            ImageView pic =new ImageView(this);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(200,200);
            pic.setLayoutParams(parms);

            if (friendsPic[i].length()<100){
                pic.setImageBitmap(defaultPic);

            }
            else{
                pic.setImageBitmap(Base64toBitmap(friendsPic[i]));
            }

            TextView text = new TextView(this);
            text.setText(friendsName[i]);

            item.addView(pic);
            item.addView(text);
            item.setTag(TAG2,friendsId[i]);
            if(friendsName[i]=="" || friendsName[i]==null || friendsName[i].isEmpty() || friendsName[i]=="-1"){

            }
            else {
                listLayout.addView(item);
                item.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v)
                    {
                        Log.d("greend", String.valueOf( v.getTag(TAG2)));
                        Intent intent=new Intent(NewConv.this,ConvActivity.class);
                        intent.putExtra(ALLCONVID, String.valueOf( v.getTag(TAG2)));
                        startActivity(intent);
                    }
                });
            }

        }

    }
    private class getFriends extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0006", String.valueOf(storedId));
            friendsId = connect.magicSauce();

            connect = new ClientConnexion("192.168.1.17", 2345, "0007", String.valueOf(storedId));
            friendsName = connect.magicSauce();

            connect = new ClientConnexion("192.168.1.17", 2345, "0008", String.valueOf(storedId));
            friendsPic = connect.magicSauce();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setFriends();
        }
    }
    public Bitmap Base64toBitmap(String pp){
        byte[] decodedString = Base64.decode(pp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
