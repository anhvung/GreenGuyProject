package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.IdentificationActivity.sep;
import static java.lang.Math.min;

public class MyProfileActivity extends AppCompatActivity {
    String[] response;
    String sendMsg;
    int storedId;
    TextView age;
    ImageView photo;
    TextView nom;
    TextView lieu;
    ImageButton setImage;
    ImageView my_page_profil_image;
    Bitmap image;
    private static final int TAG = 421206948;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
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
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        new getInfo().execute();

    }

    private void setFriends() {
        final LinearLayout listLayout=findViewById(R.id.friend_list_layout);
        for (int i=0;i<10;i++){
            LinearLayout item=new LinearLayout(MyProfileActivity.this);
            item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            item.setOrientation(LinearLayout.HORIZONTAL);

            ImageView pic =new ImageView(this);
            pic.setImageBitmap(image);
            TextView text = new TextView(this);
            text.setText("test name "+i);

            item.addView(pic);
            item.addView(text);
            item.setTag(TAG,i);
            listLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Log.d("greend", String.valueOf((int) v.getTag(TAG)));
                }
            });
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
        }
        else{

            byte[] decodedString = Base64.decode( response[3], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image = decodedByte; //for testin
            my_page_profil_image.setImageBitmap(image);

        }
        setFriends();


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


}
