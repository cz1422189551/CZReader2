package activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import fragment.CategoryBookFragment;
import model.Book;
import model.BookCategory;
import test.czreader.R;

/**
 * Created by Administrator on 2017-6-19.
 */

public class CategoryActivity extends BaseActivity {

    private CategoryBookFragment bookFragment;

    public static  BookCategory intentData;

    @Override
    public void initView() {

        intentData = (BookCategory) getIntent().getSerializableExtra("category");
        bookFragment=new CategoryBookFragment();
        replaceFragment(bookFragment);
    }

    @Override
    public void loadView() {

    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_main;
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.ll_main_frg,fragment);
        transaction.commit();
    }

}
