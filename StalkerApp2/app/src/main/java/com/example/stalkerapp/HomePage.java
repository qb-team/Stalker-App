package com.example.stalkerapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.stalkerapp.ui.main.HomeFragment;
import com.example.stalkerapp.ui.main.ListaPreferiti;
import com.example.stalkerapp.ui.main.Settings;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/*import androidx.annotation.NonNull;
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
public class HomePage extends AppCompatActivity  {
    final Fragment home = new HomeFragment();
    final Fragment preferiti = new ListaPreferiti();
    final Fragment settings = new Settings();
    Fragment active = home;
    public static FragmentManager fragmentManager;
    public FirebaseAuth fAuth;
    private static HomePage instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setActionBarTitle("Home");
        instance=this;
        fAuth = FirebaseAuth.getInstance();
        fragmentManager=getSupportFragmentManager();
       // shouldDisplayHomeUp();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //getSupportFragmentManager().addOnBackStackChangedListener(this);
        fragmentManager.beginTransaction().add(R.id.fragment_container, settings, "3").hide(settings).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, preferiti, "2").hide(preferiti).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container,home, "1").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")!=null)
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")).commit();

                            fragmentManager.beginTransaction().hide(active).show(home).commit();
                            active = home;
                            setActionBarTitle("Home");
                            System.out.println("Creato Homefragment");
                            return true;

                        case R.id.nav_favorites:
                            if(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")!=null)
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")).commit();

                            fragmentManager.beginTransaction().hide(active).show(preferiti).commit();
                            setActionBarTitle("Lista preferiti");
                            active = preferiti;
                            System.out.println("Creato ListaPreferiti");
                            return true;

                        case R.id.nav_settings:
                            if(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")!=null)
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("Organizzazione_FRAGMENT")).commit();

                            fragmentManager.beginTransaction().hide(active).show(settings).commit();
                            setActionBarTitle("Settings");
                            active = settings;
                            System.out.println("Creato Settings");
                            return true;
                    }
                    return false;
                }
            };



    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public static HomePage getInstance() {
        return instance;
    }
}*/
public class HomePage extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabs myViewPageAdapter;
    public static FragmentManager fragmentManager;
    public FirebaseAuth fAuth;
    private static HomePage instance = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        instance=this;
        fAuth = FirebaseAuth.getInstance();
        fragmentManager=getSupportFragmentManager();
        // Define SlidingTabLayout (shown at top)
        // and ViewPager (shown at bottom) in the layout.
        // Get their instances.
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);

        // create a fragment list in order.
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ListaPreferiti());
        fragments.add(new Settings());


        // use FragmentPagerAdapter to bind the TabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together.
        myViewPageAdapter =new ActionTabs(this.getSupportFragmentManager(),
                fragments);
        // add the PagerAdapter to the viewPager
        viewPager.setAdapter(myViewPageAdapter);

        // The one-stop shop for setting up this TabLayout with a ViewPager.
        tabLayout.setupWithViewPager(viewPager);
        // Fixed tabs display all tabs concurrently
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    public static HomePage getInstance() {
        return instance;
    }




}