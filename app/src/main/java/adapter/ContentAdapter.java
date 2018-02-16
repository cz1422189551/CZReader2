package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by cz on 2017-6-23.
 */

public class ContentAdapter extends FragmentPagerAdapter {

    private  ArrayList<Fragment> fragments;

    public ContentAdapter(FragmentManager fm, ArrayList<Fragment> fragments)
    {
        super(fm);
       this.fragments= fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
