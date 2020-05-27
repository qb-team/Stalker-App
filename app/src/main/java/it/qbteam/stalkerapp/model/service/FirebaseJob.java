package it.qbteam.stalkerapp.model.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import it.qbteam.stalkerapp.tools.Utils;

public class FirebaseJob extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), Firebase.class);
        getApplicationContext().startService(service);
        Utils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}