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
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import org.json.JSONException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import it.qbteam.stalkerapp.HomePageActivity;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceAccess;
import it.qbteam.stalkerapp.model.backend.dataBackend.PlaceMovement;
import it.qbteam.stalkerapp.model.data.LatLngPlace;
import it.qbteam.stalkerapp.model.service.Server;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.data.LatLngOrganization;
import it.qbteam.stalkerapp.tools.Utils;
import lombok.SneakyThrows;


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

    private static final String PACKAGE_NAME = "it.qbteam.stalkerapp.model.Tracking.TrackingStalker";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    private static final String TAG = TrackingStalker.class.getSimpleName();
    private LatLngOrganization insideOrganization;
    private LatLngPlace insidePlace;
    private static final String CHANNEL_ID = "channel_01";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";
    private final IBinder mBinder = new LocalBinder();
    private static final int NOTIFICATION_ID = 12345678;
    private boolean mChangingConfiguration = false;
    private NotificationManager mNotificationManager;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Handler mServiceHandler;
    private Location mLocation;
    private List<LatLngOrganization> latLngOrganizationList;
    private List<LatLngPlace> latLngPlaceList;
    private Storage storage;
    private Server server;
    private OrganizationAccess organizationAccess;
    private PlaceAccess placeAccess;
    private OffsetDateTime organizationAccessTime;
    private OffsetDateTime placeAccessTime;
    private static int delay = 2000;
    private Timer timer;
    private Long orgID;
    private String accessType;
    private static boolean flag = false;
    private TrackingDistance trackingDistance;
    private boolean saveBattery = false;
    private String userToken;
    private OrganizationMovement organizationMovement;
    private PlaceMovement placeMovement;
    private List<PlaceAccess> placeAccessList;

    //usati una volta che l'app è killata.
    private static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences  mPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Gson gson;


    @Override
    public void onCreate() {

        latLngOrganizationList=new ArrayList<>();
        latLngPlaceList=new ArrayList<>();
        organizationAccess=new OrganizationAccess();
        storage = new Storage(null,null);
        server = new Server(null,null);
        timer = new Timer();
        mPrefs = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        userToken = mPrefs.getString("userToken", "");
        System.out.print("USER TOKEN SERVICE  "+ userToken);
        trackingDistance = new TrackingDistance();
        prefsEditor = mPrefs.edit();
        gson = new Gson();
        switchPriority(0);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {    // Istanziazione LocationCallback
            @SneakyThrows
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        getLastLocation();// Istanziazione FusedLocationListener

        HandlerThread handlerThread = new HandlerThread("il tag:  " + TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Per un determinato tipo di Android
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void switchPriority(int i) {
        switch (i) {
            case 0:  //default settings
                mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                break;

            case 1:  //distance<=150
                new Handler().postDelayed(() -> {
                         if(mPrefs.getBoolean("switchTrack", false)) {
                             mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                         }

                     }, 3000);

                new Handler().postDelayed(() -> {
                         if(mPrefs.getBoolean("switchTrack", false)) {
                             requestLocationUpdates();
                         }

                     }, 5000);

                break;

            case 2:  //distance<=500 or saveBattery
                new Handler().postDelayed(() -> {
                        if(mPrefs.getBoolean("switchTrack", false)) {
                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

                        }
                    }, 10000);

                new Handler().postDelayed(() -> {
                        if(mPrefs.getBoolean("switchTrack", false)) {
                            requestLocationUpdates();

                        }
                    }, 60000);

                break;

            case 3: //distance<=1000
                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

                    }
                }, 10000);

                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        requestLocationUpdates();
                    }
                }, 180000);

                break;

            case 4: //distance<=15000
                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    }
                }, 10000);

                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        requestLocationUpdates();
                    }
                }, 900000);// 15 min

            case 5:  //distance>15000
                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    }
                }, 10000);

                new Handler().postDelayed(() -> {
                    if(mPrefs.getBoolean("switchTrack", false)) {
                        requestLocationUpdates();
                    }
                }, 1800000);// 30 min

                break;
        }
    }

    private void getLastLocation() {

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                        }
                        else {
                            Log.w(TAG, "Fallito il rintracciamento della posizione.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    public void updateTrackingList() throws JSONException {

        latLngOrganizationList=LatLngOrganization.checkUpdateList(storage);
    }
    public void requestLocationUpdates() {

        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), TrackingStalker.class));

        try {
            updateTrackingList();
            Log.i(TAG,"RequestLocationUpdates è partito ");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } catch (SecurityException | JSONException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    public void removeLocationUpdates() {


            //Waits 3 sec before do the remove of the location
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (insideOrganization != null) {

                        organizationAccess = new OrganizationAccess();
                        //Update the access' list when the user exits from organization.
                        organizationAccess.setEntranceTimestamp(organizationAccessTime);
                        organizationAccess.setAccessType(accessType);
                        organizationAccess.setOrganizationId(insideOrganization.getOrgID());
                        organizationAccess.setOrgName(insideOrganization.getName());
                        organizationAccess.setExitTimestamp(OffsetDateTime.now());
                        organizationAccess.setTimeStay(HomePageActivity.getCurrentTime());

                        if (HomePageActivity.getSwitcherModeStatus()) {
                            //Comunicates the server that user is outside the organization(authenticated).
                            server.performOrganizationMovementServer(insideOrganization.getOrgAuthServerID(), insideOrganization.getOrgID(), userToken, -1, organizationMovement.getExitToken(),organizationAccess,prefsEditor,gson);
                        } else {
                            //Comunicates the server that user is outside the organization(anonymous).
                            server.performOrganizationMovementServer(null, insideOrganization.getOrgID(),userToken, -1, organizationMovement.getExitToken(), organizationAccess,prefsEditor,gson);
                        }

                        flag = false;

                        organizationMovement = null;
                        insideOrganization =null;
                        String organizationMovementJson = gson.toJson(null);
                        String insideOrganizationJson = gson.toJson(null);
                        prefsEditor.putString("organizationMovement",organizationMovementJson);
                        prefsEditor.putString("insideOrganization",insideOrganizationJson);
                        prefsEditor.commit();

                        Intent intent = new Intent(ACTION_BROADCAST);
                        intent.putExtra(EXTRA_LOCATION, mLocation);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                    if (insidePlace != null) {

                        placeAccess = new PlaceAccess();
                        //Update the access' list when the user exits from organization and place.
                        placeAccess.setEntranceTimestamp(placeAccessTime);
                        placeAccess.setPlaceName(insidePlace.getName());
                        placeAccess.setOrgId(orgID);
                        placeAccess.setExitTimestamp(OffsetDateTime.now());

                        if (HomePageActivity.getSwitcherModeStatus()) {
                            //Comunicates the server that user is outside the place(authenticated).
                            server.performPlaceMovementServer(placeMovement.getExitToken(), -1, insidePlace.getId(), insideOrganization.getOrgAuthServerID(), userToken, placeAccess,prefsEditor,gson);
                        } else {
                            //Comunicates the server that user is outside the place(anonymous).
                            server.performPlaceMovementServer(placeMovement.getExitToken(), -1, insidePlace.getId(), null, userToken, placeAccess,prefsEditor,gson);
                        }

                        //Deletes the place's list of the organization.
                        insidePlace = null;
                        placeMovement = null;
                        String placeMovementJson = gson.toJson(null);
                        String insidePlacejson = gson.toJson(null);
                        prefsEditor.putString("placeMovement",placeMovementJson);
                        prefsEditor.putString("insidePlace",insidePlacejson);
                        prefsEditor.commit();
                    }

                }
            }, 2000);

            if (insideOrganization != null) {
                Toast.makeText(getApplicationContext(), "Sei uscito dall'organizzazione: " + insideOrganization.getName(), Toast.LENGTH_SHORT).show();

            }
            if (insidePlace != null) {
                Toast.makeText(getApplicationContext(), "Sei uscito dal luogo: " + insidePlace.getName(), Toast.LENGTH_SHORT).show();

            }

            HomePageActivity.setNameOrg("Nessuna organizzazione");
            HomePageActivity.setNamePlace("Nessun luogo");
            HomePageActivity.playPauseTimeService();
            HomePageActivity.resetTime();

         //Reset of all parameters.
         Log.i(TAG, "Removing location updates");

         try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
             if(!mPrefs.getBoolean("switchTrack", false)) {
                 stopSelf();
             }

         } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    @SneakyThrows
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
            HomePageActivity.stopChronometerService();
        }

        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;

    }

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
         System.out.print("ON REBIND");
        //Restores Data after app kill
        mPrefs = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();

        String insidePlaceJson = mPrefs.getString("insidePlace", null);
        String placeMovementJson = mPrefs.getString("placeMovement", null);
        String insideOrganizationJson = mPrefs.getString("insideOrganization", null);
        String organizationMovementJson = mPrefs.getString("organizationMovement", null);

        if(insidePlace == null)
        insidePlace = gson.fromJson(insidePlaceJson,LatLngPlace.class);
        if(placeMovement == null)
        placeMovement = gson.fromJson(placeMovementJson, PlaceMovement.class);
        if(insideOrganization == null)
        insideOrganization = gson.fromJson(insideOrganizationJson, LatLngOrganization.class);
        if(organizationMovement == null)
        organizationMovement = gson.fromJson(organizationMovementJson, OrganizationMovement.class);

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

        if (!mChangingConfiguration &&  mPrefs.getBoolean("switchTrack", false)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {

        mServiceHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

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
                new Intent(this, HomePageActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, "Apri l'app", activityPendingIntent)
                .addAction(R.drawable.ic_cancel, "Interrompi tracciamento", servicePendingIntent)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_stalkericon_)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());
        if(insideOrganization!=null){

            builder.setContentText("Sei dentro all'organizzazione: "+insideOrganization.getName());
        }
        else{

            builder.setContentText("Non sei dentro a nessuna organizzazione");
        }

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void onNewLocation(Location location)  {

        mLocation=location;

        if (location != null) {

            if(!saveBattery)
                switchPriority(trackingDistance.checkDistance(mLocation,latLngOrganizationList));

            handleOrganizations(location);
        }
        // Aggiornamento notifiche quando funziona in background
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }


    private void handleOrganizations(Location location) {

        isInsideOrganizations(location);
        isInsidePlace(location);

    }

    private void isInsidePlace(Location location) {

        if (latLngPlaceList != null) {

            LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < latLngPlaceList.size(); i++) {

                for (LatLng point : latLngPlaceList.get(i).getLatLng()) {
                    builder.include(point);
                }
                boolean isInsideBoundary = builder.build().contains(actualPosition);
                boolean isInside = PolyUtil.containsLocation(actualPosition, latLngPlaceList.get(i).getLatLng(), true);
                if (isInsideBoundary && isInside) {

                    HomePageActivity.setNamePlace(latLngPlaceList.get(i).getName());

                    if (placeMovement == null && mPrefs.getBoolean("switchMode", false)) {

                        insidePlace = latLngPlaceList.get(i);

                        placeAccessTime=OffsetDateTime.now();

                        Toast.makeText(getApplicationContext(), "Sei dentro al luogo: " + insidePlace.getName()+" in modo autenticato", Toast.LENGTH_SHORT).show();

                        //Comunicates the server that user is outside the place
                        server.performPlaceMovementServer(null, 1, latLngPlaceList.get(i).getId(), insideOrganization.getOrgAuthServerID(), userToken,placeAccess, prefsEditor,gson);

                        timer.schedule( new TimerTask(){
                            @SneakyThrows
                            public void run() {
                                String placeMovementJson =mPrefs.getString("placeMovement",null);
                                placeMovement= gson.fromJson(placeMovementJson,PlaceMovement.class);
                                String insidePlacejson = gson.toJson(insidePlace);
                                prefsEditor.putString("insidePlace",insidePlacejson);
                                prefsEditor.commit();
                            }
                        }, delay);
                    }

                    else if(placeMovement== null && !mPrefs.getBoolean("switchMode", false)){

                        insidePlace = latLngPlaceList.get(i);

                        placeAccessTime=OffsetDateTime.now();

                        Toast.makeText(getApplicationContext(), "Sei dentro al luogo: " + insidePlace.getName()+" in modo non autenticato", Toast.LENGTH_SHORT).show();

                        //Comunicates the server that user is outside the place
                        server.performPlaceMovementServer(null, 1, latLngPlaceList.get(i).getId(), null, userToken,placeAccess, prefsEditor,gson);

                        timer.schedule( new TimerTask(){
                            @SneakyThrows
                            public void run() {
                                    String placeMovementJson =mPrefs.getString("placeMovement",null);
                                    placeMovement= gson.fromJson(placeMovementJson,PlaceMovement.class);
                                    String insidePlacejson = gson.toJson(insidePlace);
                                    prefsEditor.putString("insidePlace",insidePlacejson);
                                    prefsEditor.commit();
                            }
                        }, delay);
                    }
                }
                else {

                    if (placeMovement != null && latLngPlaceList.get(i).getId().equals(placeMovement.getPlaceId())) {

                        Toast.makeText(getApplicationContext(), "Sei uscito dal luogo: "+insidePlace.getName(), Toast.LENGTH_SHORT).show();

                        PlaceAccess placeAccess = new PlaceAccess();
                        //Update the access' list when the user exits from organization.
                        placeAccess.setEntranceTimestamp(placeAccessTime);
                        placeAccess.setPlaceName(insidePlace.getName());
                        placeAccess.setOrgId(orgID);
                        placeAccess.setExitTimestamp(OffsetDateTime.now());
                        Type type = new TypeToken<List<PlaceAccess>>(){}.getType();
                        String placeAccessListJson = mPrefs.getString("placeAccessList",null);
                        placeAccessList = gson.fromJson(placeAccessListJson, type);

                        if(placeAccessList!=null){
                            placeAccessList.add(placeAccess);
                            String placeAccessListFileJson = gson.toJson(placeAccessList);
                            prefsEditor.putString("placeAccessList",placeAccessListFileJson);
                            prefsEditor.commit();
                        }
                        else{
                            placeAccessList = new ArrayList<>();
                            placeAccessList.add(placeAccess);
                            String placeAccessListFileJson = gson.toJson(placeAccessList);
                            prefsEditor.putString("placeAccessList",placeAccessListFileJson);
                            prefsEditor.commit();

                        }

                        //Comunicates the server that user is outside the place
                        server.performPlaceMovementServer(placeMovement.getExitToken(), -1, latLngPlaceList.get(i).getId(), placeMovement.getOrgAuthServerId(), userToken,placeAccess,prefsEditor,gson);

                        placeMovement = null;
                        insidePlace = null;
                        String placeMovementJson = gson.toJson(null);
                        String insidePlacejson = gson.toJson(null);
                        prefsEditor.putString("placeMovement",placeMovementJson);
                        prefsEditor.putString("insidePlace",insidePlacejson);
                        prefsEditor.commit();

                        HomePageActivity.setNamePlace("Nessun luogo");
                    }
                }

            }

        }

    }

    public void isInsideOrganizations(Location location) {

        if(latLngOrganizationList!=null) {

            LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (int i = 0; i < latLngOrganizationList.size(); i++) {
                for (LatLng point : latLngOrganizationList.get(i).getLatLng()) {
                    builder.include(point);
                }

                boolean isInsideBoundary = builder.build().contains(actualPosition);

                boolean isInside = PolyUtil.containsLocation(actualPosition, latLngOrganizationList.get(i).getLatLng(), true);

                if (isInsideBoundary && isInside) {
                    HomePageActivity.setNameOrg(latLngOrganizationList.get(i).getName());
                    Long min =  HomePageActivity.getCurrentTime()/1000/60;

                    if(min >= 10L){// active saveBattery after 10 min
                        saveBattery = true;
                        switchPriority(2);
                    }
                    if (organizationMovement == null && mPrefs.getBoolean("switchMode", false) && !flag) {

                        flag = true;

                        HomePageActivity.playPauseTimeService();

                        accessType="Autenticato";

                        insideOrganization = latLngOrganizationList.get(i);// Viene creato un oggetto che identifica l'organizzazione

                        Toast.makeText(getApplicationContext(), "Sei dentro all'organizzazione: " + insideOrganization.getName()+" in modo autenticato", Toast.LENGTH_SHORT).show();

                        organizationAccessTime=OffsetDateTime.now();

                        orgID = insideOrganization.getOrgID();

                        //Comunicates the server that user is inside the organization
                        server.performOrganizationMovementServer(insideOrganization.getOrgAuthServerID(), insideOrganization.getOrgID(),userToken, 1, null, organizationAccess,prefsEditor,gson);

                        //Download the places' list.
                        server.performDownloadPlaceServer(insideOrganization.getOrgID(), userToken,prefsEditor,gson);


                        timer.schedule( new TimerTask(){
                            @SneakyThrows
                            public void run() {
                                try {
                                    String orgMove= mPrefs.getString("organizationMovement",null);
                                    organizationMovement = gson.fromJson(orgMove,OrganizationMovement.class);
                                    String insideOrganizationJson = gson.toJson(insideOrganization);
                                    prefsEditor.putString("insideOrganization",insideOrganizationJson);
                                    prefsEditor.commit();
                                    latLngPlaceList = LatLngPlace.updatePlace(mPrefs,gson);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 2000);

                    }

                    else if( organizationMovement == null && !mPrefs.getBoolean("switchMode", false) && !flag ){

                        flag = true;

                        HomePageActivity.playPauseTimeService();

                        accessType="Anonimo";

                        insideOrganization = latLngOrganizationList.get(i);// Viene creato un oggetto che identifica l'organizzazione

                        Toast.makeText(getApplicationContext(), "Sei dentro all'organizzazione: " + insideOrganization.getName()+" in modo non autenticato", Toast.LENGTH_SHORT).show();

                        organizationAccessTime=OffsetDateTime.now();

                        orgID=insideOrganization.getOrgID();

                        //Comunicates the server that user is inside the organization
                        server.performOrganizationMovementServer(null, insideOrganization.getOrgID(), userToken, 1, null, organizationAccess,prefsEditor,gson);

                        //Download the places' list.
                        server.performDownloadPlaceServer(insideOrganization.getOrgID(), userToken,prefsEditor,gson);

                        timer.schedule( new TimerTask(){
                            @SneakyThrows
                            public void run() {
                                try {
                                    String orgMove= mPrefs.getString("organizationMovement",null);
                                    organizationMovement = gson.fromJson(orgMove,OrganizationMovement.class);
                                    String insideOrganizationJson = gson.toJson(insideOrganization);
                                    prefsEditor.putString("insideOrganization",insideOrganizationJson);
                                    prefsEditor.commit();
                                    latLngPlaceList = LatLngPlace.updatePlace(mPrefs,gson);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 2000);
                    }
                }
                else {

                    if (organizationMovement!= null && latLngOrganizationList.get(i).getOrgID().equals(organizationMovement.getOrganizationId())) {

                        flag = false;

                        Toast.makeText(getApplicationContext(), "Sei uscito dall'organizzazione: "+ insideOrganization.getName(), Toast.LENGTH_SHORT).show();

                        OrganizationAccess organizationAccess = new OrganizationAccess();

                        //sets the organizationAccess' properties and
                        //Update the access' list when the user exits from organization.
                        organizationAccess.setEntranceTimestamp(organizationAccessTime);
                        organizationAccess.setAccessType(accessType);
                        organizationAccess.setOrganizationId(insideOrganization.getOrgID());
                        organizationAccess.setOrgName(insideOrganization.getName());
                        organizationAccess.setExitTimestamp(OffsetDateTime.now());
                        organizationAccess.setTimeStay(HomePageActivity.getCurrentTime());

                        //HomePageActivity.playPauseTimeService();
                        HomePageActivity.resetTime();

                        //Comunicates the server that user is outside the organization
                        server.performOrganizationMovementServer(insideOrganization.getOrgAuthServerID(), insideOrganization.getOrgID(), userToken, -1, organizationMovement.getExitToken(), organizationAccess,prefsEditor,gson);

                        // Notify anyone listening for broadcasts about the new location.
                        timer.schedule( new TimerTask(){
                            @SneakyThrows
                            public void run() {
                                Intent intent = new Intent(ACTION_BROADCAST);
                                intent.putExtra(EXTRA_LOCATION, location);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            }
                        }, delay);


                        insideOrganization = null;
                        organizationMovement = null;

                        String organizationMovementJson = gson.toJson(null);
                        String insideOrganizationJson = gson.toJson(null);
                        prefsEditor.putString("organizationMovement",organizationMovementJson);
                        prefsEditor.putString("insideOrganization",insideOrganizationJson);
                        prefsEditor.commit();

                        HomePageActivity.setNameOrg("Nessuna organizzazione");
                        saveBattery = false;
                    }
                }
            }
        }

    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
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

