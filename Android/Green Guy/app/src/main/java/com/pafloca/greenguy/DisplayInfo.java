package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import usefulclasses.ClientConnexion;

import static com.pafloca.greenguy.EventMenuAdapter.EXTRA_INFOID;

/**
 * Raffiches les info d'un événement / point d'intérêt.
 * A remplir
 */
public class DisplayInfo extends AppCompatActivity {
    int storedId;
    String infoid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);
        SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        storedId = sharedPref.getInt("USER_ID", -1);
        Intent intent = getIntent();
        infoid= intent.getStringExtra(EXTRA_INFOID);
        new Display().execute();
    }

    private class Display extends AsyncTask<Void,Void,Void> {
    String[] response;
        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
            storedId = sharedPref.getInt("USER_ID", -1);
            ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0026",infoid);
            response =connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("greend", response[1]);
            TextView titre= findViewById(R.id.titre);
            TextView auteur= findViewById(R.id.auteur);
            TextView date= findViewById(R.id.date);
            titre.setText(response[0]);
            final WebView webView = findViewById(R.id.webview);

            WebSettings settings = webView  .getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url){
                    super.onPageFinished(view, url);
                    webView.setVisibility(View.VISIBLE);
                    webView.canGoForward();
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl){
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            });
            String url=response[1];
            if(!url.startsWith("http://") && !url.startsWith("https://"))
                url = "https://" + url;
            webView.loadUrl(url);


            date.setText(getDate(Long.parseLong(response[2]), "dd/MM/yyyy hh:mm:ss.SSS"));
            auteur.setText(response[3]);
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
}
