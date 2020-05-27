package it.qbteam.stalkerapp.tools;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.preference.PreferenceManager;
import java.text.DateFormat;
import java.util.Date;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.service.Firebase;
import it.qbteam.stalkerapp.model.service.FirebaseJob;

public class Utils {

    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";
    private static final String TAG = "Utils";

    //Returns true if requesting location updates, otherwise returns false.
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    //Stores the location updates state in SharedPreferences.
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    //Returns the object as a human readable string.
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    //Returns the information about the actual location.
    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {

        ComponentName serviceComponent = new ComponentName(context, FirebaseJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(123, serviceComponent);
        builder.setMinimumLatency(3000000); // wait at least
        builder.setOverrideDeadline(3100000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());


    }
    public static void cancelJob(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }





}