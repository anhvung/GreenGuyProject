package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.IdentificationActivity.sep;
import static java.lang.Math.min;

public class MyProfileActivity extends AppCompatActivity {
    String[] response;
    String[] friendsId;
    String [] friendsPic;
    String [] friendsName;
    String sendMsg;
    int storedId;
    TextView age;
    ImageView photo;
    TextView nom;
    TextView lieu;
    ImageButton setImage;
    ImageView my_page_profil_image;
    Bitmap image;
    Bitmap defaultPic;
    public final static String FROFILE_ID="FROFILE_ID";
    private static final int TAG = 421206948;
    boolean mine = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyProfileActivity.FROFILE_ID);
        Log.d("greend", "extra message : "+message);
        if (message==null){
            SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
            storedId = sharedPref.getInt("USER_ID", -1);
        }
        else{
            mine=false;
            storedId = Integer.parseInt(message);
        }



        Drawable d = getResources().getDrawable(R.mipmap.default_profile_foreground);

        defaultPic=drawableToBitmap(d);
        photo = findViewById(R.id.my_page_profil_image);
        age = findViewById(R.id.age);
        nom = findViewById(R.id.nom);
        lieu = findViewById(R.id.lieu);
        setImage=findViewById(R.id.setImage);
        my_page_profil_image=findViewById(R.id.my_page_profil_image);
        setImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 42);
            }
        });



        new getInfo().execute();
        new getFriends().execute();

    }

    private void setFriends() {
        LinearLayout listLayout=findViewById(R.id.friend_list_layout);

        for (int i=0;i<friendsId.length;i++){
            LinearLayout item=new LinearLayout(MyProfileActivity.this);
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
                        Intent intent=new Intent(MyProfileActivity.this,MyProfileActivity.class);
                        intent.putExtra(FROFILE_ID, String.valueOf( v.getTag(TAG)));
                        startActivity(intent);
                    }
                });
            }

        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image=selectedImage;
                new getPicture().execute();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MyProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MyProfileActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    public void updateInfo(){
        nom.setText(response[0]);
        age.setText(response[1]);
        if (response[2]=="" ||response[3].substring(0,min(4,response[3].length())).contains("null")|| response[2]==null ||response[2].isEmpty()){
            lieu.setText("non renseigné");
        }
        else{
            lieu.setText(response[2]);
        }
        if (response[3]=="" ||response[3].substring(0,min(4,response[3].length())).contains("null")|| response[3]==null ||response[3].isEmpty()){
            Log.d("greend", "NO PICTURE FOUND");
            my_page_profil_image.setImageBitmap(defaultPic);
        }
        else{

            byte[] decodedString = Base64.decode( response[3], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image = decodedByte; //for testin
            my_page_profil_image.setImageBitmap(image);

        }



    }
    private class getInfo extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0004",String.valueOf(storedId));
            response=connect.magicSauce();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateInfo();

        }
    }
    private class getFriends extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0006",String.valueOf(storedId));
            friendsId=connect.magicSauce();

            connect= new ClientConnexion("192.168.1.17",2345,"0007",String.valueOf(storedId));
            friendsName =connect.magicSauce();

            connect= new ClientConnexion("192.168.1.17",2345,"0008",String.valueOf(storedId));
            friendsPic =connect.magicSauce();


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

    private class getPicture extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0005",storedId+sep+bitmapToBase64(image));
            sendMsg=connect.magicSauce()[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            byte[] decodedString = Base64.decode(sendMsg, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            my_page_profil_image.setImageBitmap(decodedByte);
        }
    }

    public String bitmapToBase64(Bitmap im){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        im=Bitmap.createScaledBitmap(im, 500, 500, true);
        im.compress(Bitmap.CompressFormat.JPEG, 75, baos); // bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public boolean notapic (String pic){
        return pic.substring(0,min(4,pic.length())).contains("null");
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
