package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.OnLineReadActivity;
import adapter.CateAdapter;
import adapter.ContentAdapter;
import adapter.RecyclerViewAdapter;
import interfaces.IActivityData;
import interfaces.IView;
import model.Book;
import model.BookCategory;
import okhttp3.Call;
import network.DataServer;
import network.RequestData;
import test.czreader.R;

/**HomeActivity的Fragment
 * Created by cz on 2017-6-10.
 */

public class PublishFragment extends BaseLazyFragment implements IView, IActivityData {

    private static final String TAG = "PublishFragment";

    private RecyclerView mRecyclerView;

    private ListView  mlistView;

    private RecyclerViewAdapter adapter;

    private CateAdapter listAdapter;


    private DataServer<BookCategory> dataServerCategory=new DataServer<>(this);

    private DataServer<Book> dataServerBook=new DataServer<Book>(this,this);



    private List<BookCategory> listCategory=new ArrayList<>();


    private List<Book> listBook=new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.frg_publish;
    }

    /**
     * 初始化View
     * @param view 父布局
     */
    @Override
    public void initView(View view) {



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mlistView= (ListView) view.findViewById(R.id.list_home);

        adapter=new RecyclerViewAdapter(getContext(),listCategory);

        listAdapter =new CateAdapter(listBook,getContext());
    }


    /**
     * 是否展示滚动进度条
     * @return
     */
    @Override
    public boolean isNeedShowProgress() {
        return false;
    }

    /**
     * 具体的加载数据操作
     */
    @Override
    public void loadData() {

        setRecyclerView();
        setListView();

        //异步请求网络获取数据
        dataServerCategory.getCategoryJson();
        dataServerBook.getBookList();

    }


    private void setListView()
    {
        mlistView.setAdapter(listAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Log.d(TAG, "onItemClick: " +i);
                Book book =listBook.get(i);
                Intent intent =new Intent(getActivity(), OnLineReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookItem",book);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private void setRecyclerView()
    {

        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        StaggeredGridLayoutManager layoutManager1=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 成功的时候回调
     * @param object
     */
    @Override
    public void onSuccees(Object object) {



        List<BookCategory> categories = (List<BookCategory>) object;
        listCategory.clear();
        for(BookCategory category : categories)
        {
            listCategory.add(category);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onFaile(Object object) {

    }

    @Override
    public void onFinish(Object object) {

        List<Book> books = (List<Book>) object;
        listBook.clear();
        for(Book b : books)
        {
            listBook.add(b);
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail() {

    }
}
