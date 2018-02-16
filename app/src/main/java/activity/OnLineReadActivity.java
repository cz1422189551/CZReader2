package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import interfaces.IActivityData;
import model.Book;
import model.Directory;
import network.DataServer;
import test.czreader.R;
import ui.AutoSplitTextView;
import util.TempFile;
import util.TempFile2;


/**
 * Created by cz on 2017-6-19.
 */

public class OnLineReadActivity extends FragmentActivity implements  IActivityData{


    private static final String TAG = "OnLineReadActivity";


    public static Book book; //具体的书

    private   TempFile2 tempFile;

    long pageCount = 0;//文件的指针；
    
    String bookName;

    long bookSize;

    private AutoSplitTextView tvContent;

    private TextView tvPageSize,tvCurrentPage,tvTitle;


    private Book partBook;

    private float down;

     private long currentPage;

    long pageSum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_core);
        initView();
        loadView();
    }

    public void initView() {

        book = (Book) getIntent().getSerializableExtra("bookItem");

        Log.d(TAG, "initView: "+book.getBookName());

        partBook=new Book();
        partBook.setBookName(bookName);

        tempFile=new TempFile2(this,book.getPath(),this);
       tvContent= (AutoSplitTextView) findViewById(R.id.frg_core_tv_content);
        tvPageSize= (TextView) findViewById(R.id.frg_core_tv_totalpage);
        tvCurrentPage= (TextView) findViewById(R.id.frg_core_tv_currentpage);
        tvTitle= (TextView) findViewById(R.id.frg_core_tv_title);
        setChangePage();
    }


    public void loadView() {

        //第一次打开，获取tempFile的指针,服务器的.txt大小，填充至book对象内
        pageCount=tempFile.getLastPageCount();
        partBook.setPosition(pageCount);
        partBook.setPath(book.getPath());
     //   bookSize=tempFile.getBookSize();
      //  partBook.setBookSize(bookSize);
        //加载load
        partBook=tempFile.loadContent(partBook);

        String temp=book.getBookName();

        tvTitle.setText(temp);
        tvContent.setText(partBook.getPartContent());

       // computerPageSize();
    }


//    private void computerPageSize()
//    {
//        if(pageSum==0)
//        {
//            long total=partBook.getBookSize();
//             pageSum=total/tempFile.getPageSize();
//        }
//        currentPage=partBook.getPosition()/tempFile.getPageSize();
//        if(currentPage==0)
//        {
//            currentPage++;
//        }
//        if(currentPage>=pageSum)
//        {
//            currentPage=pageSum;
//        }
//        tvCurrentPage.setText(String.valueOf(currentPage));
//        tvPageSize.setText(String.valueOf(pageSum));
//
//    }



    /**
     * 从服务器获取到的数据，将被保存到TempFile中，若已存在则追加至文末；
     * @param object
     */
    @Override
    public void onFinish(Object object) {

            //从服务器获取到book
            partBook= (Book) object;
        if(partBook.getPosition()==0)
        {
            partBook.setPosition(1);

        }
        Log.d(TAG, "onFinish: "+partBook.getPartContent());
            //将从服务器获取到内容,追加的TempFile末尾
            tempFile.saveContent(partBook);
            //加载到的内容和指针以book形式返回,并覆盖掉partBook
        partBook=tempFile.loadContent(partBook);
        tvContent.setText(partBook.getPartContent());

    }

    @Override
    public void onFail() {

        Log.d(TAG, "onFail: 连接服务器失败");

    }

    /**
     * 返回上一页
     */
    private void forwardPage()
    {
        long pos=partBook.getPosition();
       if(pos==1)
       {
           return ;
       }
        else
        {
          pos--;
            partBook.setPosition(pos);
            partBook=tempFile.loadContent(partBook);
            tvContent.setText(partBook.getPartContent());
           // computerPageSize();;
        }
    }

    private void nextPage() {
        long pos = partBook.getPosition();


        long tempPos = pos+1;

        partBook.setPosition(tempPos);
        partBook = tempFile.loadContent(partBook);
        tvContent.setText(partBook.getPartContent());
       // computerPageSize();
    }


    private void setChangePage()
    {
        tvContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down=event.getX();
                        Log.d("ReaderActivity","DOWN -> downXPosition"+down);
                        break;
                    case MotionEvent.ACTION_UP:
                        float resultPostion=event.getX();
                        if(resultPostion<down)
                        {
                            Log.d("ReaderActivity", "调用下一页");
                            nextPage();

                        }else if(resultPostion>down)
                        {
                            forwardPage();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                }

                return true;
            }
        });
    }

}
