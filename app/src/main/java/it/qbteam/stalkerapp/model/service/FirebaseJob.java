package it.qbteam.stalkerapp.model.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import it.qbteam.stalkerapp.tools.Utils;

public class FirebaseJob extends JobService {
    private static final String TAG = "FirebaseJob";


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        Intent service = new Intent(getApplicationContext(), Firebase.class);
        getApplicationContext().startService(service);
       // Utils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"Job cancelled before completion");
        return true;
    }
}