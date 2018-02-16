package util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import db.DBUtil;
import interfaces.IActivityData;
import model.Book;
import network.DataServer;

import static android.R.attr.data;

/**分页思路，将服务器返回的字符串保存到一个副本文件TempFile中，
 * 根据TempFile的长度，分割成页数；
 * Created by cz on 2017-7-3.
 */

public class TempFile2 {

    private static final String TAG = "TempFile2";

    private long pageCount=1; //页数

    private int  contentSize=335; //默认一页大概是335个字符。

    private int tempFileCharLen=0; //副本文件的字符数。

    private int pageSum=1;//副本文件的总页数


    private File tempFile=null;  //副本文件

    private String filePath;  //TempFile的文件名

    private String fileAbsuloPath; //TempFile文件的绝对路径

    public String houzhui = "_cz"; //pref文件添加的后缀名；

    private Activity mContext;

    private SharedPreferences shared; //创建一个已key=书名_cz values=position 的xml文件，用于记录上次阅读的位置。

    IActivityData iActivityData; //回调接口

    DataServer<Book> request;
    DBUtil dbUtil;


    public TempFile2(Activity activity, String fileName, IActivityData iActivityData) {
        this.mContext = activity;
        this.filePath = fileName;
        this.iActivityData = iActivityData;
        request = new DataServer<Book>(iActivityData, mContext);
        shared = mContext.getSharedPreferences((fileName + houzhui), mContext.MODE_PRIVATE);
    }

    public TempFile2(Activity activity, String fileName) {
        this.mContext = activity;
        this.filePath = fileName;
        dbUtil = DBUtil.getInstance(mContext);
    }


    /**
     * 每次从服务器获取到内容都需要保存，保存之后重新更新副本文件页数
     * @param book
     */
    public void saveContent(Book book) {

        String content = book.getPartContent();

        long pageSize = book.getBookSize();
        OutputStreamWriter writerStream=null;
        try {
            writerStream = new OutputStreamWriter(new FileOutputStream(tempFile,true),"UTF-8");
            BufferedWriter writer =new BufferedWriter(writerStream);
            if (tempFile.exists()) {
                writer.write(content);
            } else {
                tempFile.createNewFile();
                writer.write(content);
            }
            writer.flush();
            if(writer!=null)
            {
                writer.close();
                writerStream.close();
            }
            //重置文件页数
            resetPageSum();
            SharedPreferences.Editor editor = shared.edit();
            // FIXME: 2017-7-3  服务器文件的总页数
            editor.putLong("bookSize", pageSize);
            editor.commit();

        } catch (IOException e) {
            Log.d(TAG, "saveContent: "+e.getMessage());
        }

    }

    /**
     * 根据文件页数从TempFile里面读取，若超过TempFile且不大于服务器文件长度则可以
     * 发送页数从服务器中读取。
     * 若超过服务器总页数，需要将页数还原。
     * @return 填充页数，内容的对象
     */
    public Book loadContent(Book book)
    {
       long current=book.getPosition();
        Log.d(TAG, "loadContent: "+current);
        if(current==0)
        {
            Log.d(TAG, "loadContent: 没有阅读过，请求服务器");
            request.requestBookPageServlet(book.getPath(),String.valueOf(book.getPosition()));
            return book;
        }else
        {
            if(current>pageSum)
            {
                Log.d(TAG, "loadContent: 已经从临时文件中读取完了，请求服务器");
                request.requestBookPageServlet(book.getPath(),String.valueOf(book.getPosition()));
                return book;
            }
            else
            {
                Log.d(TAG, "loadContent: 读"+current+"页");
                readFromTempFile(book);
            }
        }
        return book;
    }




    private Book readFromTempFile(Book book)
    {
        long current=book.getPosition();
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(tempFile), "UTF-8");

            BufferedReader br = new BufferedReader(in);
            if(book.getPosition()==1)
            {
                br.skip(0);
            }
            else
            {
                br.skip(contentSize*current);
            }


            char data[] = new char[contentSize];

            int len = br.read(data, 0, data.length);

            book.setBookName(tempFile.getName());
            book.setPosition(current);
            String content = new String(data, 0, len);
            Log.d(TAG, "readFromTempFile: "+content);
            book.setPartContent(content);
            if (br != null) {
                in.close();
                br.close();

            }
            //保存指针
            SharedPreferences.Editor editor = shared.edit();

            editor.putLong((filePath + houzhui), current);

            Log.d("TempFile2", "将页数写入pref文件中,position为" + current);

            editor.commit();
        }
        catch (Exception e)
        {
            Log.d(TAG, "readFromTempFile: "+e.getMessage());
        }
        return book;
    }









    private String createTempFilePath(String fileName) {
        String tempPath="";
        try {


//            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
//                // 保存到sd卡
//                tempPath = Environment.getExternalStorageState(). + File.separator
//                        + fileName + ".txt";
//            } else {
                // 保存到file目录
                tempPath = mContext.getCacheDir()
                        + File.separator + filePath + ".txt";

//            }
            Log.d(TAG, tempPath);

        } catch (Exception e) {
            Log.d(TAG, "getFIleAbsolutePath" + e.getMessage());
        }
        return tempPath;
    }


    /**
     * 返回上次阅读的页数
     * @return
     */
    public long getLastPageCount()
    {

        fileAbsuloPath = createTempFilePath(filePath);

        tempFile = new File(fileAbsuloPath);

        if (tempFile.exists()) {
            if (shared != null) {
                pageCount = shared.getLong((filePath + houzhui), 0);
                Log.d(TAG, "获取到了上次返回的页数" + pageCount);
            } else {
                pageCount =0;
            }

        } else {

            pageCount = 0;
        }
        return pageCount;
    }


    /**
     * 重置副本文件的页数，字符总数；
     */
    private void resetPageSum()
    {
        InputStreamReader in= null;
        try {
            in = new InputStreamReader(new FileInputStream(tempFile), "UTF-8");
            BufferedReader br = new BufferedReader(in);

            String line = "";
            StringBuffer sb=new StringBuffer();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            tempFileCharLen=sb.length();
            if(tempFileCharLen%contentSize!=0)
            {
                pageSum=(tempFileCharLen/contentSize)+1;
            }
            else
            {
                pageSum=tempFileCharLen/contentSize;
            }


            if(br!=null)
            {
                in.close();
                br.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }








    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 返回文件字符数量
     * @return
     */
    public int getTempFileCharLen() {
        return tempFileCharLen;
    }








}
