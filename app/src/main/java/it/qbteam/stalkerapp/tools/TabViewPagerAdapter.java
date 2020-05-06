package it.qbteam.stalkerapp.tools;

import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;//IN REALTA' NON LO USO, DEVO IMPLEMENTARE

    private final List<Fragment> registeredFragments = new ArrayList();
    private final List<String> titol = new ArrayList<>();
    /**
     * Create pager adapter

     * @param resources
     * @param fragmentManager
     */
    public TabViewPagerAdapter(final Resources resources, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {

        return registeredFragments.get(position);

    }

    @Override
    public int getCount() {
        return registeredFragments.size();
    }

    @Override
    public CharSequence getPageTitle( int position) {

        return titol.get(position);
    }


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
