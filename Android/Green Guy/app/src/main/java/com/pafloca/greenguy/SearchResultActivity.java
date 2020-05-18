package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import usefulclasses.ClientConnexion;
import static usefulclasses.ClientConnexion.sep;

public class SearchResultActivity extends AppCompatActivity {
    String message;
    private static final String sep2="!sepPourlEsmSg?";
    private static final int TAG = 421206948;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        message= intent.getStringExtra(MapsActivity.EXTRA_MESSAGE2);
        Log.d("greend","message search : "+message);
        new Result().execute();
    }

    private class Result extends AsyncTask<Void,Void,Void> {
        String[] titresPlusId;

        @Override
        protected Void doInBackground(Void... voids) {
            ClientConnexion connect = new ClientConnexion("192.168.1.17", 2345, "0045", message);
            titresPlusId = connect.magicSauce();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            LinearLayout ll=findViewById(R.id.list_result);
            for (int i=0;i<titresPlusId.length;i++){
                LinearLayout item=new LinearLayout(SearchResultActivity.this);
                item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                item.setOrientation(LinearLayout.VERTICAL);
                item.setGravity(Gravity.CENTER_VERTICAL );
                String[] str= titresPlusId[i].split(sep2, -1);
                String titre=str[0];
                String id=str[1].substring(1);
                Log.d("greend","id search : "+id);
                TextView title=new TextView(SearchResultActivity.this);
                title.setText(titre);



                item.addView(title);
                item.setTag(TAG,id);
                item.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {

                        Intent intent=new Intent(SearchResultActivity.this,DisplayGeneralEvent.class);
                        intent.putExtra(MapsActivity.EXTRA_MESSAGE, String.valueOf( v.getTag(TAG)));
                        startActivity(intent);
                    }
                });
                ll.addView(item);


            }
        }
    }

}
