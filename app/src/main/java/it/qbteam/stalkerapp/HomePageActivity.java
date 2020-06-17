package it.qbteam.stalkerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.BuildConfig;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.json.JSONException;
import java.io.IOException;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.ChronometerOrganizationService;
import it.qbteam.stalkerapp.model.service.ChronometerPlaceService;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;
import it.qbteam.stalkerapp.tools.FragmentListenerFeatures;
import it.qbteam.stalkerapp.tools.Utils;
import it.qbteam.stalkerapp.ui.view.AccessHistoryFragment;
import it.qbteam.stalkerapp.ui.view.ActionTabFragment;
import it.qbteam.stalkerapp.ui.view.HomeFragment;
import it.qbteam.stalkerapp.ui.view.MyStalkersListFragment;
import lombok.SneakyThrows;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, FragmentListenerFeatures {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static User user;
    private static TextView nameOrg;
    private static TextView namePlace;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;
    private SwitchCompat switcher;
    private static SwitchCompat switcherMode;
    private ActionTabFragment actionTabFragment;
    private DrawerLayout drawer;
    private static String userEmail;
    private static TrackingStalker mService;
    private boolean mBound = false;
    private static String path = "/data/user/0/it.qbteam.stalkerapp/files";

    //Time spent in organization fields
    public static Handler sHandler;
    private static int hours = 0;
    private static int secs = 0;
    private static int mins = 0;
    private static Long currentTime = 0L;
    private boolean isBound = false;
    private static ChronometerOrganizationService myService;
    private static TextView time;

    //Time spent in place fields
    public static Handler sHandler1;
    private static int hours1 = 0;
    private static int secs1 = 0;
    private static int mins1 = 0;
    private static Long currentTime1 = 0L;
    private boolean isBound1 = false;
    private static ChronometerPlaceService myService1;
    private static TextView time1;

    private static final String SHARED_PREFS = "sharedPrefs";
    private SharedPreferences  mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        //Internal class method `ServiceConnection` which allows you to establish a connection with the` Bind Service`.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingStalker.LocalBinder binder = (TrackingStalker.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        //Method of the internal class `ServiceConnection` which allows you to disconnect the connection with the` Bind Service`.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;

        }
    };

    //Connects the chronometerService to the HomePageActivity.
    private final ServiceConnection chronometerServiceConnection = new ServiceConnection() {
        //Internal class method `ServiceConnection` which allows you to establish a connection with the` Bind Service`.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronometerOrganizationService.LocalBinder binder = (ChronometerOrganizationService.LocalBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        //Method of the internal class `ServiceConnection` which allows you to disconnect the connection with the` Bind Service`.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
            isBound = false;

        }
    };

    //Connects the chronometerService to the HomePageActivity.
    private final ServiceConnection chronometerPlaceConnection = new ServiceConnection() {
        //Internal class method `ServiceConnection` which allows you to establish a connection with the` Bind Service`.
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronometerPlaceService.LocalBinder binder = (ChronometerPlaceService.LocalBinder) service;
            myService1 = binder.getService();
            isBound1 = true;
        }

        //Method of the internal class `ServiceConnection` which allows you to disconnect the connection with the` Bind Service`.
        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService1 = null;
            isBound1 = false;

        }
    };

    //Creates Activity and manages the fragments connected to it. In this method there is the user authentication check,
    // in case the user is no longer logged in the goToMainActivity () method is invoked.
    @SuppressLint({"WrongViewCast", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        mPrefs = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        //user==null
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            updateFirebaseToken();
        }
        else{
            goToMainActivity();
        }

        if (savedInstanceState == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            initScreen();
        }
        else if(savedInstanceState != null){
            actionTabFragment = (ActionTabFragment) getSupportFragmentManager().getFragments().get(0);
        }

        HomePageActivity.sHandler = new Handler() {

            @Override
            public void handleMessage(Message timeMsg) {
                super.handleMessage(timeMsg);

                currentTime = Long.valueOf(timeMsg.obj.toString());
                hours = secs / 3600;
                secs = (int) (currentTime / 1000);
                mins = secs / 60;
                secs = secs % 60;
                setTime();
            }
        };


        HomePageActivity.sHandler1 = new Handler() {

            @Override
            public void handleMessage(Message timeMsg) {
                super.handleMessage(timeMsg);

                currentTime1 = Long.valueOf(timeMsg.obj.toString());
                hours1 = secs1 / 3600;
                secs1 = (int) (currentTime1 / 1000);
                mins1 = secs1 / 60;
                secs1 = secs1 % 60;
                setTimePlace();
            }
        };

        Toolbar toolbar=findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layoutID);
        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_viewID);
        navigationView.setNavigationItemSelectedListener( this);
        myReceiver = new MyReceiver();
        statusCheck();

        //setting switch button tracking in drawer menu
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_switchID);
        View actionView = MenuItemCompat.getActionView(menuItem);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switcherID);
        switcher.setOnClickListener(this);

        //Imposto nome organizzazione in cui l'utente è tracciato del drawer
        MenuItem itemNameOrg=menu.findItem(R.id.navi_org_item);
        actionView = MenuItemCompat.getActionView(itemNameOrg);
        nameOrg = (TextView) actionView.findViewById(R.id.name_orgID);
        nameOrg.setText("Nessuna organizzazione");


        //Imposto nome luogo in cui l'utente è tracciato del drawer
        MenuItem itemNamePlace=menu.findItem(R.id.navi_place_item);
        actionView = MenuItemCompat.getActionView(itemNamePlace);
        namePlace = (TextView) actionView.findViewById(R.id.name_placeID);
        namePlace.setText("Nessun luogo");

        //sets the switcherMode
        MenuItem itemSwitchMode= menu.findItem(R.id.nav_switch_ModeID);
        actionView = MenuItemCompat.getActionView(itemSwitchMode);
        switcherMode=(SwitchCompat) actionView.findViewById(R.id.switcherModeID);

        switcherMode.setOnClickListener(this);

        //setting user email in drawer menu
        View headerView= navigationView.getHeaderView(0);
        TextView emailTextView= headerView.findViewById(R.id.emailTextDrawerID);
        emailTextView.setText(userEmail);

        //imposto cronometro place
        MenuItem chronometerItem=menu.findItem(R.id.navi_time_insideID);
        actionView = MenuItemCompat.getActionView(chronometerItem);
        time = actionView.findViewById(R.id.timeID);

        //imposto cronometro place
        MenuItem chronometerPlaceItem=menu.findItem(R.id.navi_time_inside_PlaceID);
        actionView = MenuItemCompat.getActionView(chronometerPlaceItem);
        time1 = actionView.findViewById(R.id.timePlaceID);

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (checkPermissions()) {
                requestPermissions();
            }
        }

        setSwitchMode();
        setSwitchState();
    }

    @Override
    protected void onStart() {

        if(myService==null){
            this.bindService(new Intent(this, ChronometerOrganizationService.class), chronometerServiceConnection, Context.BIND_AUTO_CREATE);
        }

        if(myService1==null){
            this.bindService(new Intent(this, ChronometerPlaceService.class), chronometerPlaceConnection, Context.BIND_AUTO_CREATE);
        }

        super.onStart();
    }

    //This method is invoked when the main Activity is no longer visible to the user, that is, when the latter has decided to close the application.
    @Override
    public void onStop() {
        if (mBound) {
            this.unbindService(mServiceConnection);
            mBound = false;
        }
        if (isBound) {
            this.unbindService(chronometerServiceConnection);
            isBound = false;
        }
        if (isBound1) {
            this.unbindService(chronometerPlaceConnection);
            isBound1 = false;
        }


        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(TrackingStalker.ACTION_BROADCAST));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

    }

    public void updateFirebaseToken(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user=new User(task.getResult().getToken(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                       // System.out.println("TOKEN:\n"+user.getToken().substring(0, 100) +"\n"); // stampo i primi 100 solo per dire "okay c'è"
                        prefsEditor.putString("userToken",user.getToken());
                        prefsEditor.putString("userID",user.getUid());
                        prefsEditor.commit();
                        this.bindService(new Intent(this, TrackingStalker.class), mServiceConnection, Context.BIND_AUTO_CREATE);
                    } else {
                        // Handle error -> task.getException();
                    }
                });}

    //creates the action tab menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //manages the ActionTabFragment.
    private void initScreen() {
        // Creato l'actionTab in alto
        actionTabFragment = new ActionTabFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragmentID, actionTabFragment)
                .commit();

    }

    //Manages the back button.
    @Override
    public void onBackPressed() {
        if (!actionTabFragment.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            super.onBackPressed();
        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }

    //Manages the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.logoutID:
                setSwitchState();
                FirebaseAuth.getInstance().signOut();   //logout
                goToMainActivity();
                break;
            case R.id.alphabeticalOrderID:
                HomeFragment frag = (HomeFragment)ActionTabFragment.getHomeFragment();
                frag.alphabeticalOrder();
                break;
            case R.id.helperLinkID:
                Uri uri = Uri.parse("https://stalker-manuale-utente.readthedocs.io/app/funzionalita/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Moves the user to MainActivity.
    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Check the user's permissions about tracking.
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //Asks to the user permissions about tracking.
    private void requestPermissions() {
        System.out.print("requestPermissions");
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(findViewById(R.id.drawer_layoutID),"Devi avere i permessi",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(HomePageActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(HomePageActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    //Callback received when a permissions request has been completed.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                startTracking();
                stopTracking();
                switcher.setChecked(false);
            } else {
                // Permission denied.
                setSwitchState();
                Snackbar.make(
                        findViewById(R.id.drawer_layoutID),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show();
            }
        }
    }

    //Manage the start of tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    private void startTracking() {
        mService.requestLocationUpdates();
    }


    //Manage the end of the tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    private void stopTracking() {

        mService.removeLocationUpdates();
    }

    public static void stopChronometerService(){
        myService.stopSelf();
        playPauseTimeService();
        resetTime();
    }
    public static void stopChronometerPlaceService(){
        myService1.stopSelf();
        playPauseTimeServicePlace();
        resetTimePlace();
    }

    //Check if the GPS is active.
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    //Warn the user if the GPS is not active and helps him to active it.
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (checkPermissions()) {
                            requestPermissions();
                        } else {
                            startTracking();

                        }
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Manages the tracking switch in the drawer.
    public void setSwitchState() {
        Boolean switchTrackIsActive = mPrefs.getBoolean("switchTrack",false);
        if(switchTrackIsActive)
            switcher.setChecked(true);
        else
            switcher.setChecked(false);

    }

    public void setSwitchMode(){
        Boolean switchModeIsActive = mPrefs.getBoolean("switchMode",false);
        if(switchModeIsActive)
            switcherMode.setChecked(true);
        else
            switcherMode.setChecked(false);
    }

    //Comunicates with MyStalkerListFragment to add the organization.
    @Override
    public void addOrganization(Organization organization) throws IOException, JSONException {
        Fragment frag = (MyStalkersListFragment)ActionTabFragment.getMyStalkerFragment();
        ((MyStalkersListFragment) frag).addOrganization(organization);
    }

    @Override
    public void disableScroll(boolean enable) {
        actionTabFragment.disableScroll(enable);
    }

    @Override
    public boolean deleteAuthButton(String orgName) {
        Fragment frag = (MyStalkersListFragment)ActionTabFragment.getMyStalkerFragment();
        if(((MyStalkersListFragment) frag).organizationIsPresentInList(orgName))
            return true;
        else
            return false;
    }

    //Metodi per segnalare la tua posizione all'interno di un organizzazione o luogo
    public static void setNameOrg(String name){
        nameOrg.setText(name);
    }

    public static void setNamePlace(String name){
        namePlace.setText(name);
    }

    public static String getNameOrg(){return nameOrg.getText().toString();}

    public static String getNamePlace(){return namePlace.getText().toString();}

    public static boolean getSwitcherModeStatus(){
        if(switcherMode.isChecked())
            return true;
        else
            return false;
    }

    public static String getPath(){
        return path;
    }

    public static TabLayout getTabLayout(){return ActionTabFragment.getTabLayout();}

    //CRONOMETRO ORGANIZATION
    public static void setTime() {

        time.setText(String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));

    }

    public static Long getCurrentTime(){

        return currentTime;

    }
    public static void playPauseTimeService() {

        myService.startStop();

    }
    public static void resetTime() {

        myService.reset();
        hours = 0;
        mins = 0;
        secs = 0;
        setTime();
    }
    //FINE CRONOMETRO ORGANIZATION

    //CRONOMETRO PLACE
    public static void setTimePlace() {

        time1.setText(String.format("%02d", hours1) + ":" + String.format("%02d", mins1) + ":" + String.format("%02d", secs1));

    }

    public static Long getCurrentTimePlace(){

        return currentTime1;

    }
    public static void playPauseTimeServicePlace() {

        myService1.startStopPlace();

    }
    public static void resetTimePlace() {

        myService1.resetPlace();
        hours1 = 0;
        mins1 = 0;
        secs1 = 0;
        setTimePlace();
    }
//FINE CRONOMETRO PLACE

    @SneakyThrows
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.switcherID:

                if(switcher.isChecked()){

                    if (checkPermissions()) {
                        requestPermissions();
                    }
                    else {
                        switcher.setEnabled(false);
                        switcherMode.setEnabled(true);
                        prefsEditor.putBoolean("switchTrack",true);
                        prefsEditor.commit();
                        startTracking();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                switcher.setEnabled(true);
                            }
                        },2000);
                    }
                }
                else{
                    switcher.setEnabled(false);
                    switcherMode.setChecked(false);
                    switcherMode.setEnabled(false);
                    prefsEditor.putBoolean("switchTrack",false);
                    prefsEditor.commit();
                    stopTracking();
                    new Handler().postDelayed(() -> {
                        // This method will be executed once the timer is over
                        switcher.setEnabled(true);
                    },2000);
                }
                break;

            case R.id.switcherModeID:
                if(switcher.isChecked() && switcherMode.isChecked()){
                    switcherMode.setEnabled(false);
                    stopTracking();
                    prefsEditor.putBoolean("switchMode",true);
                    prefsEditor.commit();
                    new Handler().postDelayed(() -> {
                        // This method will be executed once the timer is over
                        switcherMode.setEnabled(true);
                        startTracking();
                    },2000);

                }
                else if(switcher.isChecked() && !switcherMode.isChecked())
                {
                    switcherMode.setEnabled(false);
                    prefsEditor.putBoolean("switchMode",false);
                    prefsEditor.commit();
                    stopTracking();
                    new Handler().postDelayed(() -> {
                        // This method will be executed once the timer is over
                        switcherMode.setEnabled(true);
                        startTracking();
                    },2000);
                }

                break;
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @SneakyThrows
        @Override
        public void onReceive(Context context, Intent intent) {
                Fragment frag = (AccessHistoryFragment )ActionTabFragment.getAccessHistoryFragment();
            ((AccessHistoryFragment) frag).saveAccess();
        }
    }

}
