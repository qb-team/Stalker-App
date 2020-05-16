package it.qbteam.stalkerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.BreakIterator;

import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.model.tracking.TrackingStalker;
import it.qbteam.stalkerapp.tools.Utils;
import it.qbteam.stalkerapp.ui.view.ActionTabFragment;
import it.qbteam.stalkerapp.ui.view.HomeFragment;
import it.qbteam.stalkerapp.ui.view.MyStalkersListFragment;

public class HomePageActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener, HomeFragment.FragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static User user;
    private static TextView nameOrg;
    private static TextView namePlace;
    private SwitchCompat switcher;
    private FirebaseAuth fAuth;
    private ActionTabFragment actionTabFragment;
    private DrawerLayout drawer;
    private static String userEmail;
    private static TrackingStalker mService;
    private boolean mBound = false;
    private NavigationView navigationView;
    private  View actionView;

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

    //Method that is invoked when the application is opened.
    @Override
    protected void onStart() {
        super.onStart();
    }

    //Creates Activity and manages the fragments connected to it. In this method there is the user authentication check,
    // in case the user is no longer logged in the goToMainActivity () method is invoked.
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if (FirebaseAuth.getInstance().getCurrentUser() != null ) {
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                user=new User(task.getResult().getToken(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                                System.out.println("TOKEN CREATO:"+task.getResult().getToken()+"UID CREATO:"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }
        statusCheck();
        fAuth= FirebaseAuth.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            userEmail=fAuth.getCurrentUser().getEmail();

        }
        else goToMainActivity();

        Toolbar toolbar=findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layoutID);
        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.nav_viewID);
        navigationView.setNavigationItemSelectedListener( this);
        //setting switch button in drawer menu
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_switchID);
        actionView = MenuItemCompat.getActionView(menuItem);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switcherID);
        //Imposto nome organizzazione in cui l'utente è tracciato del drawer
        MenuItem itemNameOrg=menu.findItem(R.id.navi_org_item);
        actionView = MenuItemCompat.getActionView(itemNameOrg);
        nameOrg = (TextView) actionView.findViewById(R.id.name_orgID);

        //Imposto nome luogo in cui l'utente è tracciato del drawer
        MenuItem itemNamePlace=menu.findItem(R.id.navi_place_item);
        actionView = MenuItemCompat.getActionView(itemNamePlace);
        namePlace = (TextView) actionView.findViewById(R.id.name_placeID);



        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        //setting user email in drawer menu
        View headerView= navigationView.getHeaderView(0);
        TextView emailTextView= headerView.findViewById(R.id.emailTextDrawerID);
        emailTextView.setText(userEmail);

        // Restore the state of the buttons when the activity (re)launches.
        setSwitchState(Utils.requestingLocationUpdates(this));


        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()){
                    if (!checkPermissions()) {
                        requestPermissions();
                    }
                    else {
                        try {
                            startTracking();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    stopTracking();

                }
            }
        });

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
        this.bindService(new Intent(this, TrackingStalker.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }


    //creates the action tab menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_tab, menu);
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
                FirebaseAuth.getInstance().signOut();   //logout
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.alphabeticalOrderID:
                Fragment frag = (HomeFragment)ActionTabFragment.getHomeFragment();
                ((HomeFragment) frag).alphabeticalOrder();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Moves the user to MainActivity.
    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Check the user's permissions about tracking.
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //Asks to the user permissions about tracking.
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                try {

                   startTracking();

                } catch (IOException e) {
                    e.printStackTrace();

                }
            } else {
                // Permission denied.
                setSwitchState(false);
                Snackbar.make(
                        findViewById(R.id.drawer_layoutID),
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
    //This method is invoked when the main Activity comes is paused and its return is expected in a short time.
    @Override
    public void onPause() {
        super.onPause();
    }

    //This method is invoked when the main Activity is no longer visible to the user, that is, when the latter has decided to close the application.
    @Override
    public void onStop() {

        if (mBound) {
            this.unbindService(mServiceConnection);
            mBound = false;
        }

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    //Method that is called when a shared resource between two views is modified, added or removed.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            HomePageActivity homePageActivity= new HomePageActivity();
            homePageActivity.setSwitchState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }

    }

    //Manage the start of tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    public void startTracking() throws IOException {
        Storage.deleteMovement();
        Storage.deletePlaceMovement();
        mService.requestLocationUpdates();
    }


    //Manage the end of the tracking by referring to the organizations chosen and entered by the user in the `MyStalkersList` view.
    public void stopTracking() {
        mService.removeLocationUpdates();
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
                        if (!checkPermissions()) {
                            requestPermissions();
                        } else {
                            try {
                                startTracking();

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
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
    public void setSwitchState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            switcher.setChecked(true);
        } else {
            switcher.setChecked(false);
        }
    }

    //Returns user's token.
    public static String getUserToken(){
        return user.getToken();
    }


    @Override
    public void sendOrganization(Organization organization) throws IOException, JSONException {
        Fragment frag = (MyStalkersListFragment)ActionTabFragment.getMyStalkerFragment();
        ((MyStalkersListFragment) frag).addOrganization(organization);
    }
    public static void setNameOrg(String name){
        nameOrg.setText(name);

    }
    public static void setNamePlace(String name){
        namePlace.setText(name);

    }


}