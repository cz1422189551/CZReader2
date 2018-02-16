package ui;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import adapter.FragmentAdapter;

/**获取FragmentManager，作为Fragment集合的控制器
 * Created by cz on 2017-6-10.
 */

public class FragmentControl {

    public List<Fragment> mlist;

    FragmentAdapter mpagerAapter;

   public FragmentControl(AppCompatActivity mcontext, List<Fragment> mList)
    {
        mlist=mList;
        mpagerAapter = new FragmentAdapter(mlist,mcontext.getSupportFragmentManager());
    }

    public void addFragment(Fragment fragment)
    {
        mlist.add(fragment);
        mpagerAapter.notifyDataSetChanged();
    }

    public void removeFragment(Fragment fragment)
    {
        mlist.remove(fragment);
        mpagerAapter.notifyDataSetChanged();
    }


    public void removeAllFragment( )
    {
        for (Fragment fragment:
            mlist ) {
            mlist.remove(fragment);
        }
        mpagerAapter.notifyDataSetChanged();
    }

    public FragmentAdapter getFragmentAdapter()
    {
        return mpagerAapter;
    }
}
