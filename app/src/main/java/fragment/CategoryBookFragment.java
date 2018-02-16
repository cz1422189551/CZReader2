package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import activity.CategoryActivity;

import activity.HomeActivity;
import activity.OnLineReadActivity;
import adapter.CateAdapter;
import interfaces.IView;
import model.Book;
import network.DataServer;
import test.czreader.R;

/**
 * Created by cz on 2017-6-19.
 */

public class CategoryBookFragment extends BaseLazyFragment implements IView{

    private static final String TAG = "CategoryBookFragment";
    
    String requestId;
    private TextView tvTitle;

    private ImageButton imageButton ;

    private ListView listView;

    private CateAdapter adapter;

    private List<Book>  books=new ArrayList<>();

    private DataServer<List<Book>> dataServer =new DataServer<>(this);


    @Override
    public int getLayout() {
        return R.layout.frg_cate;
    }

    @Override
    public void initView(View view) {
        listView= (ListView) view.findViewById(R.id.ll_cate_view);
        tvTitle= (TextView) view.findViewById(R.id.tv_cate_title);
        imageButton= (ImageButton) view.findViewById(R.id.cate_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        adapter=new CateAdapter(books,getActivity());

    }


    private void  setListView()
    {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " +i);
                Book book =books.get(i);
                Intent intent =new Intent(getActivity(), OnLineReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookItem",book);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean isNeedShowProgress() {
        return true;
    }

    @Override
    public void loadData() {
        setListView();
        tvTitle.setText(CategoryActivity.intentData.getCategoryName());
        requestId =String.valueOf(CategoryActivity.intentData.getCategoryId());
        dataServer.getBookJsonByCate(requestId);
    }

    @Override
    public void onSuccees(Object object) {
        List<Book> booksList = (List<Book>) object;
        books.clear();
        for(Book book : booksList)
        {
            books.add(book);
        }
        super.closeProgress();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFaile(Object object) {
        super.closeProgress();
        Toast.makeText(getActivity(),"该网络无法链接上服务器,尝试重连",Toast.LENGTH_SHORT).show();;
        dataServer.getBookJsonByCate(requestId);
    }

    @Override
    public void onDestroy() {
        requestId=null;
        super.onDestroy();

    }


}
