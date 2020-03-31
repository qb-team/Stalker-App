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
import android.widget.Toolbar;
import com.example.stalkerapp.ui.main.HomeFragment;
import com.example.stalkerapp.ui.main.ListaPreferiti;
import com.example.stalkerapp.ui.main.Organizzazione;
import com.example.stalkerapp.ui.main.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
public class HomePage extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new ListaPreferiti();
    final Fragment fragment3 = new Settings();
    final Fragment fragment4= new Organizzazione();
    Fragment active = fragment1;
    public static FragmentManager fragmentManager;
    public FirebaseAuth fAuth;
    private static HomePage instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        instance=this;
        fAuth = FirebaseAuth.getInstance();
        fragmentManager=getSupportFragmentManager();
        shouldDisplayHomeUp();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();
        //I added this if statement to keep the selected fragment when rotating the device
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
//
                            fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            System.out.println("Creato Homefragment");
                            return true;

                        case R.id.nav_favorites:
//
                            fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            System.out.println("Creato ListaPreferiti");
                            return true;

                        case R.id.nav_settings:
//
                            fragmentManager.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            System.out.println("Creato Settings");
                            return true;
                    }
                    return false;
                }
            };
   /* @Override
public void onBackPressed() {   // Funzionalità per il backbutton (tasto per andare indietro)
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    }*/


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public static HomePage getInstance() {
        return instance;
    }
}