package it.qbteam.stalkerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;

import it.qbteam.stalkerapp.model.backend.model.Organization;
import it.qbteam.stalkerapp.model.data.User;
import it.qbteam.stalkerapp.tools.Utils;
import it.qbteam.stalkerapp.ui.view.ActionTabFragment;
import it.qbteam.stalkerapp.ui.view.HomeFragment;
import it.qbteam.stalkerapp.ui.view.MyStalkersListFragment;

public class HomePageActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static HomePageActivity instance = null;
    // The BroadcastReceiver used to listen from broadcasts from the service.
    static boolean active=false;
    private SwitchCompat switcher;
    private Location mlocation;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser firebaseUser;
    private  AppBarConfiguration mAppBarConfiguration;
    private ActionTabFragment actionTabFragment;
    private DrawerLayout drawer;
    private static String userEmail;
    private User user;
    private ArrayList<Organization> myStalkerList;
    // Monitors the state of the connection to the service.
    @Override
    protected void onStart() {
        super.onStart();

    }
    public static HomePageActivity getInstance() {
        return instance;
    }

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
        instance=this;
        fStore = FirebaseFirestore.getInstance();
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
        NavigationView navigationView = findViewById(R.id.nav_viewID);
        navigationView.setNavigationItemSelectedListener( this);
        //setting switch button in drawer menu
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_switchID);
        View actionView = MenuItemCompat.getActionView(menuItem);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switcherID);


        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        //setting user email in drawer menu
        View headerView= navigationView.getHeaderView(0);
        TextView emailTextView=(TextView) headerView.findViewById(R.id.emailTextDrawerID);
        emailTextView.setText(userEmail);

        // Restore the state of the buttons when the activity (re)launches.
        setSwitchState(Utils.requestingLocationUpdates(this));


        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()){
                    if (!checkPermissions()) {
                        requestPermissions();
                    } else
                        //.requestLocationUpdates();
                        MyStalkersListFragment.getInstance().startTracking();
                }
                else{
                    //mService.removeLocationUpdates();
                    MyStalkersListFragment.getInstance().stopTracking();
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

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initScreen() {
        // Creato l'actionTab in alto
        actionTabFragment = new ActionTabFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragmentID, actionTabFragment)
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.logoutID:
                FirebaseAuth.getInstance().signOut();   //logout
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.alphabeticalOrderID:
                    HomeFragment.getInstance().alphabeticalOrder();
                break;
            case R.id.cambianumero:
                //mService.setNumero(TrackingDistance.checkDistance(mlocation));
                //System.out.println(mService.getNUMERO());
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
                MyStalkersListFragment.getInstance().startTracking();
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

    public void statusCheck() {//Controllo se il GPS è attivo
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {//Allert nel caso in cui il GPS non sia attivo
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (!checkPermissions()) {
                            requestPermissions();
                        } else {
                            MyStalkersListFragment.getInstance().startTracking();
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



    public void setSwitchState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            switcher.setChecked(true);
        } else {
            switcher.setChecked(false);
        }
    }


    public  String getUID(){
        return user.getUid();
    }
    public  String getuserToken(){
        return user.getToken();
    }

}