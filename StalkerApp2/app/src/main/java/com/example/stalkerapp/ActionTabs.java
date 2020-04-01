package com.example.stalkerapp;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ActionTabs extends FragmentPagerAdapter {
        private static final String TAG = ActionTabs.class.getSimpleName();
        private ArrayList<Fragment> fragments;

        public static final int HOME = 0;
        public static final int PREFERITI = 1;
        public static final int SETTINGS = 2;
        public static final String UI_TAB_HOME = "HOME";
        public static final String UI_TAB_PREFERITI = "PREFERITI";
        public static final String UI_TAB_SETTINGS = "SETTINGS";


        public ActionTabs(FragmentManager fm, ArrayList<Fragment> fragments){
            super(fm);
            this.fragments = fragments;
        }

        // Return the Fragment associated with a specified position.
        public Fragment getItem(int pos){
            Log.d(TAG, "getItem " + "position" + pos);
            return fragments.get(pos);
        }

        // Return the number of views available
        public int getCount(){
            Log.d(TAG, "getCount " + "size " + fragments.size());
            return fragments.size();
        }

        // This method may be called by the ViewPager to obtain a title string
        // to describe the specified page
        public CharSequence getPageTitle(int position) {
            Log.d(TAG, "getPageTitle " + "position " + position);
            switch (position) {
                case HOME:
                    return UI_TAB_HOME;
                case PREFERITI:
                    return UI_TAB_PREFERITI;
                case SETTINGS:
                    return UI_TAB_SETTINGS;
                default:
                    break;
            }
            return null;
        }
    }