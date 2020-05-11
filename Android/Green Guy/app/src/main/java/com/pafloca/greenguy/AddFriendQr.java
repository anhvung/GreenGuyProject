package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.IdentificationActivity.sep;

public class AddFriendQr extends AppCompatActivity {
    String id;
    String sendMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_qr);
        //get ID
            SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
            int storedId = sharedPref.getInt("USER_ID", -1);
            id=String.valueOf(storedId);
        //set QR
            ImageView imvQrCode = findViewById(R.id.imageQR);
            Bitmap bitmap = null;
            try {
                bitmap = textToImage(id, 500, 500);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            imvQrCode.setImageBitmap(bitmap);


        //add friend button

            ImageButton add = findViewById(R.id.addFrienQr);
            add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    IntentIntegrator integrator = new IntentIntegrator(AddFriendQr.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan the QR code");
                    integrator.setBeepEnabled(true);
                    integrator.setOrientationLocked(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }
            });

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {

            if(result.getContents() == null) {


            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {

//Scanned successfully
                Toast.makeText(this, "scanned !", Toast.LENGTH_SHORT).show();
                addfriend(Integer.parseInt(result.getContents()),Integer.parseInt(id));
            }

        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }
    public void addfriend(int id1,int id2){
        sendMsg=id1+sep+id2;
        new add().execute();
    }
    private class add extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("greend", "Sending : "+sendMsg);
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0003", sendMsg);
            sendMsg = connect.magicSauce()[0];
            Log.d("greend", "Return : "+sendMsg);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(sendMsg.equals("true")){
                Toast.makeText(AddFriendQr.this, "Ajouté", Toast.LENGTH_SHORT).show();
            }
            else if (sendMsg.equals("already friend")){
                Toast.makeText(AddFriendQr.this, "Already friend", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(AddFriendQr.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //TEXT to QR
    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
