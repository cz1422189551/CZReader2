package fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.HomeActivity;
import adapter.HasBookAdapter;
import db.DBUtil;
import model.LocalBook;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import test.czreader.R;

/**
 * Created by cz on 2017-6-10.
 */


public class BookShelfFragment extends BaseLazyFragment{

    private static final String TAG = "BookShelfFragment";

    private RecyclerView mRecyclerView;


    private HasBookAdapter adapter;

    private LinearLayout noBookLayout;

    private LinearLayout hasBookLayout;


    private DBUtil db;

    private ImageButton  queryLocal;



    private List<LocalBook> localBooks;

    private List<LocalBook> adapterList=new ArrayList<>();


    @Override
    public void onResume() {
        if(HomeActivity.chooseFilePath!=null)
        {
            try {
               File file = new File(HomeActivity.chooseFilePath);
                LocalBook localBook=new LocalBook();
                localBook.setbPos(0);
                localBook.setbPath(file.getAbsolutePath());
                String temp=file.getName();
               temp= temp.substring(0,temp.length()-4);
                localBook.setbName(temp);
                Log.d(TAG, "onResume: "+localBook.getbName());
                localBook.setLastTime(String.valueOf(System.currentTimeMillis()));
                db.saveLocalBook(localBook);
                localBooks=db.loadLocalBooks();
                setLayoutVisible();
                HomeActivity.chooseFilePath=null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    @Override
    public int getLayout() {
        return R.layout.frg_bookshelf;
    }


    @Override
    public void initView(View view) {

        db=DBUtil.getInstance(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_hasbook_view);
        noBookLayout= (LinearLayout) view.findViewById(R.id.ll_local_nobook_fragment);
        hasBookLayout= (LinearLayout) view.findViewById(R.id.ll_local_hasbook_fragment);
        queryLocal= (ImageButton) view.findViewById(R.id.btn_local_nobook_fragment);
        queryLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startFileManager();
            }
        });
        adapter=new HasBookAdapter(getActivity(),adapterList);
        mRecyclerView.setAdapter(adapter);

    }


    public void startFileManager(){
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_CONTACT = 101;
//            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
//            , Manifest.permission.READ_EXTERNAL_STORAGE
//            , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
//            //验证是否许可权限
//            for (String str : permissions) {
//                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    //申请权限
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                    return;
//                }
//            }
//        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        getActivity().startActivityForResult(intent, HomeActivity.UP_TXT);
    }

    @Override
    public boolean isNeedShowProgress() {
        return false;
    }

    @Override
    public void loadData() {
        localBooks=db.loadLocalBooks();
        setRecyclerView();
        setLayoutVisible();
    }


    private void setLayoutVisible()
    {
        if(localBooks.size()<1)
        {
            Log.d(TAG, "setLayoutVisible: "+localBooks.size());
            hasBookLayout.setVisibility(View.GONE);
            noBookLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.d(TAG, "setLayoutVisible: "+localBooks.size());
            updateAdapter();
            noBookLayout.setVisibility(View.GONE);
            hasBookLayout.setVisibility(View.VISIBLE);
        }
    }


    private void updateAdapter()
    {
        if(localBooks!=null)
        {
            adapterList.clear();;
            for(LocalBook l : localBooks)
            {
                Log.d(TAG, "updateAdapter: bid"+l.getbId());
                Log.d(TAG, "updateAdapter: bName"+l.getbName());
                Log.d(TAG, "updateAdapter: bPath"+l.getbPath());
                adapterList.add(l);
            }
            adapter.notifyDataSetChanged();
        }

    }



    private void setRecyclerView()
    {
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager layoutManager1=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setAdapter(adapter);
    }


}
