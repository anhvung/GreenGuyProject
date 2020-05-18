package com.pafloca.greenguy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {
    public static int fast = 150;
    int i = 0;
    int[] colors={Color.WHITE,Color.BLACK,Color.BLUE,Color.YELLOW,Color.BLUE,Color.GREEN,Color.CYAN,Color.MAGENTA};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        scheduleJob();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
                int storedId = sharedPref.getInt("USER_ID", -1);
                if (storedId==-1){
                    Intent intent=new Intent(SplashScreen.this, CreateProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(SplashScreen.this, MapsActivity.class);
                    startActivity(intent);
                }
            }

        }, 0);//faster

        new whouaa().execute();

    }
    private class whouaa extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
           i++;
            try
            {
                Thread.sleep( fast );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ConstraintLayout bgElement = findViewById(R.id.splash_layout);
            bgElement.setBackgroundColor(colors[i%colors.length]);
            ImageView mImageView = findViewById(R.id.imageViewS);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(60, 0, 60, 120*i);
            mImageView.setLayoutParams(lp);
            new whouaa().execute();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this, MapsActivity
                .class);
        startActivity(intent);
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, NotifJob.class);
        JobInfo info;
        info = new JobInfo.Builder(4296969, componentName)
                .setMinimumLatency(3000).build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("greend", "Job scheduled");
        } else {
            Log.d("greend", "Job scheduling failed");
        }
    }
    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("greend", "Job cancelled");
    }
}
