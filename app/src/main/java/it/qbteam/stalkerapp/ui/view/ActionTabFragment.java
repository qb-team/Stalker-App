package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.qbteam.stalkerapp.tools.OnBackPressListener;
import it.qbteam.stalkerapp.R;
import it.qbteam.stalkerapp.tools.TabViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ActionTabFragment extends Fragment {
    /**
     * TabPagerIndicator
     *
     * Please refer to ViewPagerIndicator library
     */
    protected TabLayout tabLayout;

    protected ViewPager viewPager;

    private TabViewPagerAdapter adapter;


    public ActionTabFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action_tab, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabID);
        viewPager = (ViewPager) view.findViewById(R.id.viewpagerID);
        //setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new TabViewPagerAdapter(getResources(),getChildFragmentManager());
        //add fragment



        adapter.addFragment(new HomeFragment(),"");
        adapter.addFragment(new MyStalkersListFragment(),"");
       //adapter.addFragment(new Settings(),"");

        viewPager.setAdapter(adapter);
        // The one-stop shop for setting up this TabLayout with a ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_stalkericon);

        // Fixed tabs display all tabs concurrently
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */

    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getItem(viewPager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

}
