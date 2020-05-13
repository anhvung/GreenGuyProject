package com.pafloca.greenguy;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.os.Build;
import android.util.Log;
import android.view.View;

public class NotifJob extends JobService {
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
