package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;



public class FragmentAdapter extends FragmentPagerAdapter {


    List<Fragment> mlist;



    public FragmentAdapter(List<Fragment> mlist, FragmentManager fm) {
        super(fm);
        this.mlist =mlist;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }
}
