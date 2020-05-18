package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import usefulclasses.ClientConnexion;

import static java.lang.Math.min;

public class DisplayGeneralEvent extends AppCompatActivity {
    String id;
    int storedId;
    String newComment;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager lm;
    RecyclerView.Adapter adapter;
    ArrayList<String> commentsName;
    ArrayList<String> commentsMsg;
    ArrayList<String> commentsDate;
    ArrayList<String> commentsUserPic;
    String titre;
    String descr;
    long debutlong;
    long finlong;

    public static final String sep="!@@!!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_general_event);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        Intent intent = getIntent();
        id = intent.getStringExtra(MapsActivity.EXTRA_MESSAGE);
        new Display().execute();
        new DisplayComments().execute();


    }

    public void participer(View view) {
        new AjouterParticipant().execute();
    }

    public void participants(View view) {
        Intent intent=new Intent(this,ParticipantsActivity.class);
        intent.putExtra("EventGeneralId", Integer.parseInt(id));
        startActivity(intent);
    }

    public void inviter(View view) {
        Intent intent=new Intent(this,InviteFriendsActivity.class);
        intent.putExtra("EventGeneralId", Integer.parseInt(id));
        startActivity(intent);
    }

    public void calendar(View view) {
        Calendar beginCal = Calendar.getInstance();


        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, titre);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, descr);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, debutlong);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, finlong);
        startActivity(intent);
    }

    private class AjouterParticipant extends AsyncTask<Void,Void,Void> {
        String response;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0036",storedId+sep+id);
            response =connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(DisplayGeneralEvent.this, response, Toast.LENGTH_LONG).show();
        }
    }



    private class DisplayComments extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0031",String.valueOf(id));
            commentsName=new ArrayList<>(Arrays.asList(connect.magicSauce()));
            connect= new ClientConnexion("192.168.1.17",2345,"0032",String.valueOf(id));
            commentsMsg=new ArrayList<>(Arrays.asList(connect.magicSauce()));
            connect= new ClientConnexion("192.168.1.17",2345,"0033",String.valueOf(id));
            commentsDate=new ArrayList<>(Arrays.asList(connect.magicSauce()));
            connect= new ClientConnexion("192.168.1.17",2345,"0034",String.valueOf(id));
            commentsUserPic=new ArrayList<>(Arrays.asList(connect.magicSauce()));
            if(commentsDate.get(0).equals("")){
                commentsName=new ArrayList<>();
                commentsMsg=new ArrayList<>();
                commentsDate=new ArrayList<>();
                commentsUserPic=new ArrayList<>();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView =findViewById(R.id.commentslist);
            adapter= new CommentsAdapter( commentsUserPic,commentsName,commentsMsg,commentsDate,DisplayGeneralEvent.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(DisplayGeneralEvent.this));
            mRecyclerView.setAdapter(adapter);
        }
    }
    public void AjouterComment(View view) {
        EditText comment=findViewById(R.id.comment);
        newComment=comment.getText().toString();
        if (newComment==null || newComment.equals("")){
            Toast.makeText(this, "Commentaire vide", Toast.LENGTH_LONG).show();
        }
        else
            new addComment().execute();
    }
    private class addComment extends AsyncTask<Void,Void,Void> {
        String response;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0030",id+sep+storedId+sep+newComment);
            response =connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(response.equals("true")){
                Toast.makeText(DisplayGeneralEvent.this, "Commentaire ajoute", Toast.LENGTH_LONG).show();
                new updateNewMsg().execute();
            }
            else{
                Toast.makeText(DisplayGeneralEvent.this, "erreur", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);
        }
    }
    private class updateNewMsg extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0033", String.valueOf(id));
            ArrayList<String> newcommentsDate =new ArrayList<>(Arrays.asList(connect.magicSauce()));
            commentsDate.add(newcommentsDate.get(newcommentsDate.size() - 1));

            connect = new ClientConnexion("192.168.1.17", 2345, "0034", String.valueOf(id));
            ArrayList<String> newcommentsUserPic = new ArrayList<>(Arrays.asList(connect.magicSauce()));
            commentsUserPic.add(newcommentsUserPic.get(newcommentsUserPic.size() - 1));

            connect = new ClientConnexion("192.168.1.17", 2345, "0032", String.valueOf(id));
            ArrayList<String> newcommentsMsg = new ArrayList<>(Arrays.asList(connect.magicSauce()));
            commentsMsg.add(newcommentsMsg.get(newcommentsMsg.size() - 1));

            connect = new ClientConnexion("192.168.1.17", 2345, "0031", String.valueOf(id));
            ArrayList<String> newccommentsName = new ArrayList<>(Arrays.asList(connect.magicSauce()));
            commentsName.add(newccommentsName.get(newccommentsName.size() - 1));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();

        }
    }
    public void ajouterPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 42);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                newComment=bitmapToBase64(selectedImage);
                new addComment().execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(DisplayGeneralEvent.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(DisplayGeneralEvent.this, "You haven't picked an image",Toast.LENGTH_LONG).show();
        }
    }
    private class Display extends AsyncTask<Void,Void,Void> {
        String[] response;
        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0027",id);
            response =connect.magicSauce();
            titre=response[0];
            descr=response[1];
            debutlong=Long.parseLong(response[3]);
            finlong=Long.parseLong(response[4]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("greend","INVITE2 "+id);
            Log.d("greend", response[1]);
            TextView titre= findViewById(R.id.titre);
            TextView descr= findViewById(R.id.descr);
            TextView type= findViewById(R.id.type);
            TextView deb= findViewById(R.id.deb);
            TextView fin= findViewById(R.id.fin);
            TextView debtxt= findViewById(R.id.debtxt);
            TextView fintxt= findViewById(R.id.fintxt);
            LinearLayout ll= findViewById(R.id.layoutsButons);
            if(response[2].equals("event")){

                deb.setText(getDate(Long.parseLong(response[3]), "dd/MM/yyyy hh:mm"));
                fin.setText(getDate(Long.parseLong(response[4]), "dd/MM/yyyy hh:mm"));
            }
            else{
                deb.setVisibility(View.GONE);
                fin.setVisibility(View.GONE);
                debtxt.setVisibility(View.GONE);
                fintxt.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
            }

            TextView auteur= findViewById(R.id.auteur);
            titre.setText(response[0]);
            descr.setText(response[1]);
            type.setText(response[2]);
            auteur.setText(response[5]);
            super.onPostExecute(aVoid);
        }
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public String bitmapToBase64(Bitmap im){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        im=Bitmap.createScaledBitmap(im, 500, 500, true);
        im.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public Bitmap Base64toBitmap(String pp){
        byte[] decodedString = Base64.decode(pp, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
