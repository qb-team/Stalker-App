/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.qbteam.stalkerapp.model.tracking;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.Utils;

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that service is removed.
 */
public class TrackingStalker extends Service {

    private static final String PACKAGE_NAME ="it.qbteam.stalkerapp.model.Tracking.TrackingStalker";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    private static final String TAG = TrackingStalker.class.getSimpleName();

    /**
     * Switch per aggiornare il Locationrequest
     * 0 -> Massima accuretazza
     * 1 -> Acurattezza bilanciata
     * 2 -> Bassa accuratezza
     */
    public int NUMERO = 0;

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

//    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    //    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;

    public TrackingStalker() {
    }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);  // Instanzazione FusedLocationProviderClient

        /** Creazione del CallBack
         * https://developers.google.com/android/reference/com/google/android/gms/location/LocationCallback
         * */
        mLocationCallback = new LocationCallback() {    // Istanziazione LocationCallback
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        switchPriority(0);    // Istanziazione LocationRequest
        getLastLocation();          // Istanziazione FusedLocationListener

        HandlerThread handlerThread = new HandlerThread("il tag:  " + TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Per un determinato tipo di Android
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

    }

    /**
     * Sets the location request parameters.
     * https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
     * Constant Summary
     * int 	PRIORITY_BALANCED_POWER_ACCURACY 	Used with setPriority(int) to request "block" level accuracy.
     * int 	PRIORITY_HIGH_ACCURACY 	Used with setPriority(int) to request the most accurate locations available.
     * int 	PRIORITY_LOW_POWER 	Used with setPriority(int) to request "city" level accuracy.
     * int 	PRIORITY_NO_POWER 	Used with setPriority(int) to request the best accuracy possible with zero additional power consumption.
     *
     * setSmallestDisplacement(float smallestDisplacementMeters) --> Set the minimum displacement between location updates in meters
     *
     */
    private void createLocationRequest() {
        System.out.println("creato locationrequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(2);
    }

    public void setNumero(int i){
        removeLocationUpdates();
        switchPriority(i);
        requestLocationUpdates();
    }

    /**
     * Switch per aggiornare il Locationrequest
     * 0 -> Massima accuretazza
     * 1 -> Acurattezza bilanciata
     * 2 -> Bassa accuratezza
     */
    public void switchPriority(int i) {
        System.out.println("Switch priority avvenuto");
        switch (i){
            case 0:
                mLocationRequest = new LocationRequest();
                System.out.println("Massima accuretazza");
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                mLocationRequest.setSmallestDisplacement(2);
                break;
            case 1:
                mLocationRequest = new LocationRequest();
                System.out.println("Acurattezza bilanciata");
                mLocationRequest.setInterval(20000);
                mLocationRequest.setFastestInterval(10000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                break;
            case 2:
                mLocationRequest = new LocationRequest();
                System.out.println("Bassa accuratezza");
                mLocationRequest.setInterval(30000);
                mLocationRequest.setFastestInterval(15000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                break;
        }


    }




    public int getNUMERO(){
        return NUMERO;
    }



    /** Creazione FusedLocation
     * https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient
     * getlastlocation() --> Ritorna l'ultima posizione     *
     * */
    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Fallito il rintracciamento della posizione.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), TrackingStalker.class));
        try {
            System.out.println("RequestLocationUpdates è partito ");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {  // Se i permessi non sono stati accettati (???)
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }


    /** Non so bene come viene gestito --> Forse non serve nella nostra app */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {     // Serve per i servizi Foreground
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {     // Serve per i servizi Foreground
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {     // Serve per i servizi Foreground --> In particolare fa partire l'applicazione in background
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");

            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }





    /**
     * La notifica a schermo viene gestita qui
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, TrackingStalker.class);

        // Creazione del testo da stampare a schermo IN BACKGROUND
        CharSequence text = Utils.getLocationText(mLocation);


        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomePageActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, "Apri l' app",
                        activityPendingIntent)
                .addAction(R.drawable.ic_cancel,"Interrompi tracciamento",
                        servicePendingIntent)
                .setContentText(text) // Stampa a schermo IN BACKGROUND
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_stalkericon)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }


    /** Gestione posizione
     * Log.i(TAG, "New location: " + location); --> Stampa nel run
     * mLocation = location; --> crea l'oggetto di tipo "Location" mLocation
     * */
    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);

        System.out.println("L'intervallo è questo:  " + mLocationRequest.getInterval());
        System.out.println("L'intervallo veloce è questo:  " + mLocationRequest.getFastestInterval());
        mLocation = location;

        if (location!=null){
            handleLocation(location);
        }

        // Aggiornamento notifiche quando funziona in background
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    private void handleLocation(Location location){
        if (isInside(location)){
            //logica dentro location
        }
        else{
            int i= TrackingDistance.checkDistance(location);
            switchPriority(i);
            // Aggiornamento locationrequest
        }
    }

    public boolean isInside(Location location) {
        //Polygon Torre; --> come si usa Polygon?
        final ArrayList<LatLng> polygon = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        polygon.add(new LatLng(45.4139244815,11.8809040336));
        polygon.add(new LatLng(45.4137732038,11.8812763624));
        polygon.add(new LatLng(45.4134925404,11.8810503718));
        polygon.add(new LatLng(45.4136378199,11.8806753327));

        for (LatLng point : polygon) {
            builder.include(point);
        }
        System.out.println("creo builder:  " + builder);

        LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
        String isInsideString;

        boolean isInsideBoundary = builder.build().contains(test); // true se il test point è all'interno del confine
        boolean isInsideBoolean = PolyUtil.containsLocation(test, polygon, true); // false se il punto è all'esterno del polygon

        if (isInsideBoundary && isInsideBoolean == true )
        {
            isInsideString = "sei dentro";
            return true;

        }
        else {
            isInsideString = "sei fuori";
            return false;
        }
    }



    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    // Da studiare meglio
    public class LocalBinder extends Binder {
        public TrackingStalker getService() {
            return TrackingStalker.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    // Gestisce tutti i servizi in Background
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
