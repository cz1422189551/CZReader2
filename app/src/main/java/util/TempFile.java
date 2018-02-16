package util;

/**
 * Created by Cz on 2017-6-28.
 */

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import db.DBUtil;
import interfaces.IActivityData;

import model.Book;
import model.LocalBook;
import network.DataServer;
import ui.BufferedRandomAccessFile;

/**
 * 将网络获取到数据变为存为一个临时的缓存文件TempFile，在从这歌TempFIle
 * 中操纵文件指针读取数据。(已经存在有的数据将不再请求网络).
 * 实现分页操作的关键类
 *
 * @author Cz
 */
public class TempFile {


    private static final String TAG = "TempFile";

    String filePath;  //TempFile的文件名

    String fileAbsuloPath; //TempFile文件的绝对路径

    public String houzhui = "_cz"; //pref文件添加的后缀名；


    File tempfile;//需要操纵的Tempfile

    Activity mContext;


    SharedPreferences shared; //创建一个已key=书名_cz values=position 的xml文件，用于记录上次阅读的位置。


    private int pageSize = 335;   //预计一个屏幕能显示的字符为550。。

    IActivityData iActivityData; //回调接口

    DataServer<Book> request;
    DBUtil dbUtil;

    int pageCount=1;



    private long poslenth;

    private long pos;

    private long localBookSize;


    public TempFile(Activity activity, String fileName, IActivityData iActivityData) {
        this.mContext = activity;
        this.filePath = fileName;
        this.iActivityData = iActivityData;
        request = new DataServer<Book>(iActivityData, mContext);
        shared = mContext.getSharedPreferences((fileName + houzhui), mContext.MODE_PRIVATE);
    }

    public TempFile(Activity activity, String fileName) {
        this.mContext = activity;
        this.filePath = fileName;
        dbUtil = DBUtil.getInstance(mContext);
    }

    /**
     * 根据传入的Book，该.txt文件的总长，获取Book指针
     *
     * @return 有PageSize内容的Book
     */
    public Book loadBookContent(Book book) {

        long position = book.getPosition();

        long bookSize = book.getBookSize();

        String requestBookName = book.getPath();

        Book contentBook = new Book();

        try {

            //判断tempFile文件是否是第一次被创建
            if (position == -1) {
                Log.d(TAG, "pos=-1，TempFile文件第一次被创建,请求服务器：参数postion=0");
                //请求服务器
                request.requestBookPageServlet(requestBookName, "0");
                return contentBook;
            }
            //文件已经创建了
            else {

                if (bookSize == 0) {
                    Log.d(TAG, "readPosFile:读取错误，服务器的BookSize=0");
                    return contentBook;
                }
                if (pageSize > bookSize) {
                    Log.d(TAG, "readPosFile: pageSize大于服务器的BookSize,即该.txt长度已经被读完了");
                    position = position - pageSize;
                    book.setPosition(position);
                    return book;
                } else {
                    if (position > bookSize) {
                        Log.d(TAG, "readPosFile: 指针大于服务器的BookSize，即指针已经偏移超过了服务器.txt文件的长度");
                        if (tempfile.length() < bookSize) {
                            long shengxia = bookSize - tempfile.length();
                            position = position - pageSize + shengxia;
                            //请求服务器 ，请求指针为position
                            request.requestBookPageServlet(requestBookName, String.valueOf(position));
                            return book;
                        }

                    } else {
                        if (position < tempfile.length()) {

                            Log.d(TAG, "readPosFile: 指针小于tempFile的长度=+"+tempfile.length()+",tempFile中获取"+position);
                            //从TempFile中获取

                            InputStreamReader in=new InputStreamReader(new FileInputStream(tempfile),"UTF-8");

                            BufferedReader br = new BufferedReader(in);

                            position = br.skip(position);



                            char data[] = new char[pageSize];

                           int len= br.read(data, 0, data.length);

                            book.setBookName(tempfile.getName());
                            book.setPosition(position);
                            String content=new String(data,0,len);
                            book.setPartContent(content);
                            if(br!=null)
                            {
                                in.close();
                                br.close();

                            }
                            Log.d("TempFile", "从" + position + "指针处读出内容 ->" + book.getPartContent());

                            //保存指针
                            SharedPreferences.Editor editor = shared.edit();

                            editor.putLong((filePath + houzhui), position);

                            Log.d("TempFile", "将指针position写入pref文件中,position为" + position);

                            editor.commit();
                            return book;

                        } else {
                            Log.d(TAG, "readPosFile: 指针大于TempFile的长度,需要请求服务器");
                            //请求网络
                            request.requestBookPageServlet(requestBookName, String.valueOf(position));
                            return book;
                        }
                    }
                    return book;
                }
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return book;
        }

    }


    /**
     * 每次从服务器获取到内容的时候,都需要向TempFile添加;
     */
    public void saveContent(Book book) {

        String content = book.getPartContent();

        long pageSize = book.getBookSize();
        OutputStreamWriter writerStream=null;
        try {
            writerStream = new OutputStreamWriter(new FileOutputStream(tempfile,true),"UTF-8");
            BufferedWriter writer =new BufferedWriter(writerStream);
            if (tempfile.exists()) {
                writer.write(content);
            } else {
                tempfile.createNewFile();
                writer.write(content);
            }
            writer.flush();
            if(writer!=null)
            {
                writer.close();
                writerStream.close();
            }
            SharedPreferences.Editor editor = shared.edit();
            editor.putLong("bookSize", pageSize);
            editor.commit();

        } catch (IOException e) {
            Log.d(TAG, "saveContent: "+e.getMessage());
        }

    }

    public long getPos() {
        return pos;
    }

    public String loadLocalBook(LocalBook localBook) {
        long position = localBook.getbPos();

        pos = position;

        File file = new File(localBook.getbPath());

        localBookSize = file.length();

        if (position == 0) {
            //读pageSize
            //文件指针，操纵文件指针来读取文件
            try {

                BufferedRandomAccessFile reader = new BufferedRandomAccessFile(file, "r");

                reader.seek(position);

                byte[] data = new byte[pageSize];

                int len = reader.read(data, 0, data.length);

                Log.d(TAG, "读取到len的长度" + len);

                String content = new String(data, 0, len, "GB2312");

                reader.close();

                Log.d(TAG, "从" + position + "指针处读出内容 ->" + content);

                dbUtil.updatePos(position, localBook.getbId());
                reader.close();
                return content;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println(e.getMessage());

                return null;
            }

        } else {
            if (position > localBookSize) {
                pos = position - pageSize;
                return null;
            } else {
                //文件指针，操纵文件指针来读取文件
                try {

                    BufferedRandomAccessFile reader = new BufferedRandomAccessFile(file, "r");

                    reader.seek(position);

                    byte[] data = new byte[pageSize];

                    int len = reader.read(data, 0, data.length);

                    Log.d(TAG, "读取到len的长度" + len);

                    String content = new String(data, 0, len, "GB2312");

                    reader.close();

                    Log.d(TAG, "从" + position + "指针处读出内容 ->" + content);

                    dbUtil.updatePos(position, localBook.getbId());
                    reader.close();
                    return content;

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println(e.getMessage());

                    return null;
                }
            }

        }


    }


    /**
     * 创建文件的绝对路径
     *
     * @return 返回文件的绝对路径
     */
    private String getFileAbsolutePath() {
        String fileDstPath = "";
        try {

            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                // 保存到sd卡
                fileDstPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                      +File.separator+ filePath + ".txt";

            } else {
                // 保存到file目录
                fileDstPath = mContext.getFilesDir().getAbsolutePath()
                        + File.separator + filePath + ".txt";

            }
            Log.d(TAG, fileDstPath);

        } catch (Exception e) {
            Log.d(TAG, "getFIleAbsolutePath"+e.getMessage());
        }
        return fileDstPath;

    }

    /**
     * 获取pref记录的文件大小
     *
     * @return
     */
    public long getBookSize() {

        return shared.getLong("bookSize", 0);
    }


    public long getLocalBookSize() {
        return localBookSize;
    }


    public long getTempFileSize() {
        return tempfile.length();
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 判断是否创建过一个TempFile 若创建了,则从上次记录的位置给Pos指针赋值
     * 调用位置:第一次打开.txt文件的时候调用
     */
    public long getPosition() {
        long position = 0;

        fileAbsuloPath = getFileAbsolutePath();

        tempfile = new File(fileAbsuloPath);

        if (tempfile.exists()) {
            if (shared != null) {
                position = shared.getLong((filePath + houzhui), 0);
                Log.d(TAG, "获取到了断点pos" + position);
            } else {
                position = -1;
            }

        } else {
            //文件第一次被创建时，文件指针设置为-1,这个地方没有设计好，高层也参与设置。 修改时注意:ReaderActivity中
            position = -1;
        }
        return position;

    }
}