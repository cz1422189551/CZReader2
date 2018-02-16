package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import adapter.FragmentAdapter;
import fragment.BookShelfFragment;
import fragment.PublishFragment;
import test.czreader.MainActivity;
import test.czreader.R;
import ui.FragmentControl;
import util.NetworkUtils;
import util.PermissionUtil;
import util.Util;

/**
 * Created by cz on 2017-6-10.
 */

public class HomeActivity extends BaseActivity {




    private static final String TAG = "HomeActivity";

    public static final int UP_TXT = 1;

    private TextView tvPublish,tvBookShelf;

    private ViewPager mviewPager;

    private FragmentControl fragmentControl;

    PublishFragment publishFragment;

    private List<Fragment> mListFragment =new ArrayList<>();

    public static String chooseFilePath=null; //接收文件管理器的uri

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void DrawFragment()
    {
        publishFragment =new PublishFragment();
        Fragment bookShelfFragment=new BookShelfFragment();
        mListFragment.add(publishFragment);
        mListFragment.add(bookShelfFragment);
        Log.d(TAG, "DrawFragment: ");
        fragmentControl=new FragmentControl(this,mListFragment);
        mviewPager.setAdapter(fragmentControl.getFragmentAdapter());
        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                resetView();
                switch (position) {
                    case 0:
                        tvPublish.setTextColor(Color.parseColor("#1D89E5"));
                        break;
                    case 1:
                        tvBookShelf.setTextColor(Color.parseColor("#1D89E5"));
                        break;
                }
            }
            @Override
            public void onPageScrolled(int position, float offset, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }




    @Override
    public void initView() {
        mviewPager= (ViewPager) findViewById(R.id.view_pager);
        tvPublish= (TextView) findViewById(R.id.tv_frg_publish);
        tvPublish.setTextColor(Color.parseColor("#1D89E5"));
        tvBookShelf= (TextView) findViewById(R.id.tv_frg_bookshelf);
        PermissionUtil.getPermission(this);
    }

    @Override
    public void loadView() {
        DrawFragment();
        if(!isNetWork())
        {

            //没有网络跳转至书架
            mviewPager.setCurrentItem(1);
        }
    }



    //判断手机是否开启了网络
    private boolean isNetWork()
    {
        if(NetworkUtils.isWifiConnected(this))
        {
            Toast.makeText(this, "wifi环境", Toast.LENGTH_SHORT).show();
            if(NetworkUtils.isNetworkConnected(this))
            {
                return true;
            }
            else
            {
                Toast.makeText(this, "网络不可用,请检设置", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else {
                Toast.makeText(this, "当前为移动网络", Toast.LENGTH_SHORT).show();
                if (NetworkUtils.isNetworkConnected(this)) {
                    return true;
                } else {
                    Toast.makeText(this, "网络不可用,请检设置", Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
    }


    @Override
    public int getActivityLayout() {
        return R.layout.activity_home;
    }

    protected void resetView() {
        // TODO Auto-generated method stub
        tvPublish.setTextColor(Color.parseColor("#757575"));
        tvBookShelf.setTextColor(Color.parseColor("#757575"));
        Log.d(TAG, "resetView: setColor");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == UP_TXT) {

            String path = data.getDataString();
            String temp=Uri.decode(path);
          Uri  fileUri=Uri.parse(temp);
            try {
                chooseFilePath= Util.getPath(HomeActivity.this,fileUri);
            } catch (URISyntaxException e) {
                Log.d(TAG, "onActivityResult: "+e.getMessage());
            }
            Log.d(TAG, "onActivityResult: "+chooseFilePath);
        }
    }
}
