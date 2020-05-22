package it.qbteam.stalkerapp.ui.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
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

    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    private TabViewPagerAdapter adapter;
    private static MyStalkersListFragment myStalkersListFragment;
    private static HomeFragment homeFragment;
    public ActionTabFragment() {
        // Required empty public constructor.
    }

    //Creation of the fragment as a component.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Creation of the graphic part displayed by the user.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action_tab, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabID);
        viewPager = (ViewPager) view.findViewById(R.id.viewpagerID);
        return view;
    }

    //Indicates that the creation of the activity has been completed.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Note that we are passing childFragmentManager, not FragmentManager.
        adapter = new TabViewPagerAdapter(getResources(),getChildFragmentManager());
        //add fragment
        myStalkersListFragment= new MyStalkersListFragment();
        homeFragment= new HomeFragment();
        adapter.addFragment(homeFragment,"");
        adapter.addFragment(myStalkersListFragment,"");

        viewPager.setAdapter(adapter);
        // The one-stop shop for setting up this TabLayout with a ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_stalkericon_);

        // Fixed tabs display all tabs concurrently
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //Management of the back button.
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
    public static Fragment getMyStalkerFragment(){
        return myStalkersListFragment;
    }
    public static Fragment getHomeFragment(){
        return homeFragment;
    }

}
