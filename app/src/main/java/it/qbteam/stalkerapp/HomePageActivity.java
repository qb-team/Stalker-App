package it.qbteam.stalkerapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.BuildConfig;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.ui.AppBarConfiguration;

import it.qbteam.stalkerapp.model.tracking.TrackingDistance;
import it.qbteam.stalkerapp.presenter.HomeContract;
import it.qbteam.stalkerapp.tools.Utils;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;
import it.qbteam.stalkerapp.ui.view.ActionTabFragment;
import it.qbteam.stalkerapp.ui.view.HomeFragment;
import it.qbteam.stalkerapp.ui.view.MyStalkersListFragment;

public class HomePageActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;  // Receiver personalizzato

    // A reference to the service used to get location updates.
    private TrackingStalker mService = null;  // Classe che contiene tutti i metodi Google

    // Tracks the bound state of the service.
    private boolean mBound = false;
    static boolean active=false;
    private SwitchCompat switcher;
    private Location mlocation;
    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackingStalker.LocalBinder binder = (TrackingStalker.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };



    private  AppBarConfiguration mAppBarConfiguration;
    private ActionTabFragment actionTabFragment;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;




    @Override
    protected void onStart() {
        super.onStart();
        active= true;
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() == null ) {
            goToMainActivity();
        }
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, TrackingStalker.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        //requestPermissions();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this);
        //setting switch button in drawer menu
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switcher);



        if (savedInstanceState == null) {

            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserve the fragment stack inside each tab
            initScreen();

        } else {
            // restoring the previously created fragment
            // and getting the reference
            actionTabFragment = (ActionTabFragment) getSupportFragmentManager().getFragments().get(0);
        }


        myReceiver = new MyReceiver();

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();

            }
            else
                switcher.setChecked(true);
        }
        else
            switcher.setChecked(false);

        //setting user email in drawer menu
        View headerView= navigationView.getHeaderView(0);
        TextView userEmail=(TextView) headerView.findViewById(R.id.emailTextDrawerID);
        userEmail.setText(getIntent().getStringExtra("email"));


        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()){
                    if (!checkPermissions()) {
                        requestPermissions();
                        switcher.setChecked(false);
                    } else {
                        mService.requestLocationUpdates();
                    }
                }
                else{
                    mService.removeLocationUpdates();
                }
            }
        });


    }


    private void initScreen() {
        // Creating the ViewPager container fragment once
        actionTabFragment = new ActionTabFragment();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, actionTabFragment)
                .commit();
    }


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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();   //logout
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
                // Dopo togliere
            case R.id.nav_switch:
                switcher.setChecked(!switcher.isChecked());
                if(switcher.isChecked()){
                    if (!checkPermissions()) {
                        requestPermissions();
                    } else {
                        mService.requestLocationUpdates();
                    }
                }

                    else{
                    mService.removeLocationUpdates();
                }

                    break;

            case R.id.ordineAlfabetico:
                HomeFragment.getInstance().alphabeticalOrder();
                MyStalkersListFragment.getInstance().alphabeticalOrder();
            case R.id.cambianumero:
                //mService.setNumero(TrackingDistance.checkDistance(mlocation));
                System.out.println(mService.getNUMERO());
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /** ACCETTAZIONE PERMESSI */
    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(findViewById(R.id.drawer_layout),"Devi avere i permessi",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            switcher.setChecked(false);
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

    /** FINE ACCETTAZIONE PERMESSI */

    /**
     * Callback received when a permissions request has been completed.
     */

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
                switcher.setChecked(true);
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    ///////////// INDAGARE //////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(TrackingStalker.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }

        super.onStop();
    }
    ///////////// FINE INDAGARE //////////////////////

    /**
     * Gestisce la notifica a schermo
     * Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION); --> Si prende la location
     * Toast.makeText(MainActivity.this, Utils.getLocationText(location),Toast.LENGTH_SHORT).show(); --> La stampa a schermo
     * Receiver for broadcasts sent by {@link TrackingStalker}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(TrackingStalker.EXTRA_LOCATION);
            // Per stampare a schermo coordinate Lang e Lot
//            if (location != null) {
//                Toast.makeText(MainActivity.this, Utils.getLocationText(location),
//                        Toast.LENGTH_SHORT).show();
//            }
            // Ti dice se sei dentro un organizzazione oppure no
            if (location != null ){
                mlocation=location;
                Toast.makeText(HomePageActivity.this, Utils.isInside(location),Toast.LENGTH_LONG).show();

//                mService.switchPriority(Utils.checkDistance(location));
            }
        }
    }






}