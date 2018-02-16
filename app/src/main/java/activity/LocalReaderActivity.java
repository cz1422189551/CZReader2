package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import model.Book;
import model.Directory;
import model.LocalBook;
import test.czreader.R;
import ui.AutoSplitTextView;
import util.TempFile;


/**
 * Created by Administrator on 2017-6-19.
 */

public class LocalReaderActivity extends FragmentActivity {


    private static final String TAG = "LocalReaderActivity";


    public static LocalBook localBook;

    private TempFile tempFile;


    long postion = 0;//文件的指针；


    long bookSize;

    private AutoSplitTextView tvContent;

    private TextView tvPageSize,tvCurrentPage,tvTitle;


    private float down;

     private long currentPage;

    long pageSum;

    public  String content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.local_activity);
        initView();
        loadView();
    }

    public void initView() {

        localBook = (LocalBook) getIntent().getSerializableExtra("localBookItem");

        Log.d(TAG, "initView: "+localBook.getbName());

        tempFile=new TempFile(this,localBook.getbPath());
       tvContent= (AutoSplitTextView) findViewById(R.id.local_tv_content);
        tvPageSize= (TextView) findViewById(R.id.local_tv_totalpage);
        tvCurrentPage= (TextView) findViewById(R.id.local_tv_currentpage);
        tvTitle= (TextView) findViewById(R.id.local_tv_title);
        setChangePage();
    }


    public void loadView() {

        //第一次打开，获取tempFile的指针,服务器的.txt大小，填充至book对象内

        postion=localBook.getbPos();

        bookSize=tempFile.getLocalBookSize();

        //加载load
        content=tempFile.loadLocalBook(localBook);
        tvTitle.setText(localBook.getbName());
        tvContent.setText(content);

        computerPageSize();
    }


    /**
     * 计算页数
     */
    private void computerPageSize()
    {
        if(pageSum==0)
        {

            long total=tempFile.getLocalBookSize();
             pageSum=total/tempFile.getPageSize();
        }
        currentPage=tempFile.getPos()/tempFile.getPageSize();
        if(currentPage==0)
        {
            currentPage++;
        }
        if(currentPage>=pageSum)
        {
            currentPage=pageSum;
        }
        tvCurrentPage.setText(String.valueOf(currentPage));
        tvPageSize.setText(String.valueOf(pageSum));

    }


    /**
     * 返回上一页
     */
    private void forwardPage()
    {
        long pos=tempFile.getPos();
        if(pos<tempFile.getPageSize()||tempFile.getLocalBookSize()<tempFile.getPageSize())
        {
            return ;
        }
        else
        {
            currentPage--;
            pos=pos-tempFile.getPageSize();
            localBook.setbPos(pos);
            content=tempFile.loadLocalBook(localBook);
            tvContent.setText(content);
            computerPageSize();
        }
    }

    private void nextPage() {
        long pos = tempFile.getPos();
        pos = pos + tempFile.getPageSize();

        long tempPos = pos;

        localBook.setbPos(pos);
        content = tempFile.loadLocalBook(localBook);

        if (tempPos == tempFile.getPos())
        {
            currentPage++;
        }

        Log.d(TAG, "nextPage: "+content);
        tvContent.setText(content);
        computerPageSize();
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
                            Log.d(TAG, "onTouch: postion的值："+tempFile.getLocalBookSize());

                        }else if(resultPostion>down)
                        {
                            forwardPage();
                            Log.d("ReaderActivity", "调用上一页");
                            Log.d(TAG, "onTouch: postion的值："+tempFile.getLocalBookSize());
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
