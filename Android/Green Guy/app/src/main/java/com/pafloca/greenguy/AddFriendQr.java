package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class AddFriendQr extends AppCompatActivity {
    String id;
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
            ImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                   Intent intent=new Intent(v.getContext(),PhotoQr.class);
                }
            });

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
