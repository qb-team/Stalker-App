package com.example.stalkerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.stalkerapp.ui.main.HomeFragment;
import com.example.stalkerapp.ui.main.ListaPreferiti;
import com.example.stalkerapp.ui.main.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static FragmentManager fragmentManager;
    public FirebaseAuth fAuth;
    private static HomePage instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_home_page);
        fAuth = FirebaseAuth.getInstance();
        fragmentManager=getSupportFragmentManager();
        shouldDisplayHomeUp();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            HomeFragment fragment  = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,fragment,"Home_FRAGMENT");
            fragmentTransaction.commit();
        }

    }
    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }
    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if(fragmentManager.findFragmentByTag("Home_FRAGMENT") != null && fragmentManager.findFragmentByTag("Settings_FRAGMENT") == null)
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")).commit();
                            else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.fragment_container, new HomeFragment(), "Home_FRAGMENT").commit();
                            }
                            if(fragmentManager.findFragmentByTag("Home_FRAGMENT") != null && fragmentManager.findFragmentByTag("Settings_FRAGMENT") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Settings_FRAGMENT")).commit();
                            }


                            if(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT") != null){
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT")).commit();
                            }


                            break;

                        case R.id.nav_favorites:
                            if(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.fragment_container, new ListaPreferiti(), "Preferiti_FRAGMENT").commit();
                            }
                            if(fragmentManager.findFragmentByTag("Home_FRAGMENT") != null){
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Home_FRAGMENT")).commit();
                            }
                            if(fragmentManager.findFragmentByTag("Settings_FRAGMENT") != null){
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Settings_FRAGMENT")).commit();
                            }

                            break;

                        case R.id.nav_settings:
                            if(fragmentManager.findFragmentByTag("Settings_FRAGMENT") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Settings_FRAGMENT")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.fragment_container,new Settings(), "Settings_FRAGMENT").commit();
                            }
                            if(fragmentManager.findFragmentByTag("Home_FRAGMENT") != null){
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Home_FRAGMENT")).commit();
                            }
                            if(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT") != null){
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Preferiti_FRAGMENT")).commit();
                            }

                            break;
                    }
                    return true;
                }
            };

    @Override
    public void onBackPressed() {   // Funzionalità per il backbutton (tasto per andare indietro)
        AlertDialog alertDialog = new AlertDialog.Builder(HomePage.this).
                setTitle("Attenzione").setMessage("Sei sicuro di uscire ?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        }).
                setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }
    public static HomePage getInstance() {
        return instance;
    }
}
