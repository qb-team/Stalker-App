package com.example.stalkerapp.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.stalkerapp.OnBackPressListener;
import com.example.stalkerapp.R;
import com.example.stalkerapp.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ActionTab extends Fragment {
    /**
     * TabPagerIndicator
     *
     * Please refer to ViewPagerIndicator library
     */
    protected TabLayout indicator;

    protected ViewPager pager;

    private ViewPagerAdapter adapter;


    public ActionTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action_tab, container, false);

        indicator = (TabLayout) view.findViewById(R.id.tab);
        pager = (ViewPager) view.findViewById(R.id.viewpager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ViewPagerAdapter(getResources(),getChildFragmentManager());

        pager.setAdapter(adapter);
        // The one-stop shop for setting up this TabLayout with a ViewPager.
        indicator.setupWithViewPager(pager);
        // Fixed tabs display all tabs concurrently
        indicator.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */

    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

}
