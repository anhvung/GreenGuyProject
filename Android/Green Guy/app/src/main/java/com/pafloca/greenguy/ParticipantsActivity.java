package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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

import static com.pafloca.greenguy.MyProfileActivity.FROFILE_ID;

public class ParticipantsActivity extends AppCompatActivity {
    String[] friendsId;
    String [] friendsPic;
    String [] friendsName;
    int storedId;
    Bitmap defaultPic;
    int eventId;
    private static final int TAG = 421206948;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        Drawable d = getResources().getDrawable(R.mipmap.default_profile_foreground);
        defaultPic=drawableToBitmap(d);
        Intent mIntent = getIntent();
        eventId = mIntent.getIntExtra("EventGeneralId", -1);
        new getFriends().execute();
    }
    private class getFriends extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0037",String.valueOf(eventId));
            friendsId=connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0038",String.valueOf(eventId));
            friendsPic =connect.magicSauce();
            connect= new ClientConnexion("192.168.1.17",2345,"0039",String.valueOf(eventId));
            friendsName =connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (String s:friendsId){
                Log.d("greend", "ids: "+s);
            }
            setFriends();
        }
    }

    private void setFriends() {
        LinearLayout listLayout=findViewById(R.id.listlayout);

        for (int i=0;i<friendsId.length;i++){
            LinearLayout item=new LinearLayout(ParticipantsActivity.this);
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
            item.setTag(TAG,friendsId[i]);
            if(friendsName[i]=="" || friendsName[i]==null || friendsName[i].isEmpty() || friendsName[i]=="-1"){

            }
            else {
                listLayout.addView(item);
                item.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        Log.d("greend", String.valueOf( v.getTag(TAG)));
                        Intent intent=new Intent(ParticipantsActivity.this,MyProfileActivity.class);
                        intent.putExtra(FROFILE_ID, String.valueOf( v.getTag(TAG)));
                        startActivity(intent);
                    }
                });
            }

        }

    }
    public Bitmap Base64toBitmap(String pp){
        byte[] decodedString = Base64.decode(pp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
