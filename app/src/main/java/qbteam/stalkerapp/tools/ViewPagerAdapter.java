package qbteam.stalkerapp.tools;

import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;//IN REALTA' NON LO USO, DEVO IMPLEMENTARE

    private final List<Fragment> registeredFragments = new ArrayList();
    private final List<String> titolo= new ArrayList<>();
    /**
     * Create pager adapter

     * @param resources
     * @param fm
     */
    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
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

        return titolo.get(position);
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    public void addFragment(Fragment fragment, String title){

        registeredFragments.add(fragment);
        titolo.add(title);
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


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
   /* public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }*/
}
