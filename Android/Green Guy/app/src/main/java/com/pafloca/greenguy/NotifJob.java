package com.pafloca.greenguy;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import usefulclasses.ClientConnexion;

import static androidx.lifecycle.Lifecycle.State.RESUMED;

public class NotifJob extends JobService {
    public static String CHANNEL_ID="com.pafloca.greenguy.2353";
    public static int notificationId = 347765;
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d("greend","Running service now..");
        //Small or Long Running task with callback

        //Reschedule the Service before calling job finished
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            scheduleRefresh();
        doBackgroundWork(jobParameters);

        return true;
    }
    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    Log.d("greend", "run: " + i);
                    if (jobCancelled) {
                        return;
                    }
                    try {
                        checkNotif();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("greend", "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }
public void checkNotif(){
    SharedPreferences sharedPref = getSharedPreferences("SAVE", Context.MODE_PRIVATE);
    int storedId = sharedPref.getInt("USER_ID", -1);
    ClientConnexion connect= new ClientConnexion("192.168.1.17",2345,"0035",String.valueOf(storedId));
    String[] response;
    response=connect.magicSauce();
    if (!response[0].equals("nada")) {

            Class<AllConvActivity> activ = null;
            switch (response[3]) {
                case "conv":
                    activ = AllConvActivity.class;
                    Log.e("greend", "NOUVEAU MSG");
                    break;
            }
            Intent intent = new Intent(this, activ);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_msg)
                    .setContentTitle(response[1])
                    .setContentText(response[2])
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


            notificationManager.notify(notificationId, builder.build());
        }




}


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GreenGuy";
            String description = "Nouveau Message";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return true;
    }

    private void scheduleRefresh() {
        Log.d("greend", "refresh job");
        JobInfo info;
        JobScheduler mJobScheduler = (JobScheduler) getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, NotifJob.class);

        info = new JobInfo.Builder(4296969, componentName)
                .setMinimumLatency(3000).build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
    }

}
