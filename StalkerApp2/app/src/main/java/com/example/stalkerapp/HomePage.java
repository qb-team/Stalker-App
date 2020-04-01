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