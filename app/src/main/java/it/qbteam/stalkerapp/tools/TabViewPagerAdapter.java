package it.qbteam.stalkerapp.tools;

import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> registeredFragments = new ArrayList();
    private final List<String> titol = new ArrayList<>();

    //TabViewPagerAdapter's constructor, creates page adapter.
    public TabViewPagerAdapter(final Resources resources, FragmentManager fragmentManager) {
        super(fragmentManager);
    }
  
    //Returns the fragment from its position in the view pager.
    @Override
    public Fragment getItem(int position) {

        return registeredFragments.get(position);

    }

    //Returns the view pager's size.
    @Override
    public int getCount() {
        return registeredFragments.size();
    }

    @Override
    public CharSequence getPageTitle( int position) {

        return titol.get(position);
    }

    //Adds a fragment in the view pager.
    public void addFragment(Fragment fragment, String title){

        registeredFragments.add(fragment);
        titol.add(title);
    }

   /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }*/

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
  /*  @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }*/


}
