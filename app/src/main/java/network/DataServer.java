package network;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import interfaces.IActivityData;
import interfaces.IView;
import model.Book;
import model.BookCategory;
import model.Result;
import okhttp3.Call;
import ui.CustomerProgress;
import util.GsonUtil;
import util.Util;

/**
 * Created by cz on 2017-6-18.
 */

public class DataServer<T> {

    private static final String ADRESS="http://192.168.1.109:8080/czreader/";

    private static final String TAG = "DataServer";

    private Result<T> result;

    private T data;

    private IView iView;

    private IActivityData iActivityData;


    public DataServer(IView iView)
    {
        this.iView=iView;
        result=new Result<>();
        result.setConnected(false);
        result.setErrorType(-2);

    }

    public DataServer(IView iView,IActivityData iActivityData)
    {
        this.iView=iView;
        this.iActivityData=iActivityData;
        result=new Result<>();
        result.setConnected(false);
        result.setErrorType(-2);

    }



    public DataServer(IActivityData iActivityData, Activity mcontext)
    {
        this.iActivityData=iActivityData;

    }




    public void getCategoryJson()
    {

        RequestData.submit("category","",ADRESS+"OneBook")
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: "+e.getMessage());

                        iView.onFaile(result);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: ->"+response);
                       result= (Result<T>) GsonUtil.fromJsonList(response, BookCategory.class);

                        if(isHasData())
                        {
                            List<T> mlist= (List<T>) Util.getListFromRes(result);

                            data= (T) mlist;
                            iView.onSuccees(mlist);
                        }
                    }
                });
    }

    public T getData()
    {
        return data;
    }

    public void getBookJsonByCate(String id)
    {

        RequestData.submit("item",id,ADRESS+"OneBook" )
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Log.d(TAG, "onError: "+e.getMessage());
                        iView.onFaile(result);
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: ->"+response);
                        result= (Result<T>) GsonUtil.fromJsonList(response, Book.class);

                        if(isHasData())
                        {
                            List<T> mlist= (List<T>) Util.getListFromRes(result);

                               iView.onSuccees(mlist);
                        }
                    }
                });
    }

    public void getBookList()
    {

        RequestData.submit("publish","publish",ADRESS+"OneBook" )
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: "+e.getMessage());

                        iActivityData.onFail();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: ->"+response);
                        result= (Result<T>) GsonUtil.fromJsonList(response, Book.class);

                        if(isHasData())
                        {
                            List<T> mlist= (List<T>) Util.getListFromRes(result);

                            iActivityData.onFinish(mlist);
                        }
                    }
                });
    }
    /**
     * 异步下载.txt文件
     * @param url
     */
    private void downLoadFile(String url,String parms,String values)
    {

        RequestData.downLoadfile(url,parms,values)
                .execute(new FileCallBack(Environment.getExternalStorageDirectory()
                        .getAbsolutePath(),"bookPath.txt") {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }
                    @Override
                    public void onResponse(File response, int id) {
                        Log.d(TAG, "onResponse :" + response.getAbsolutePath());
                        Log.d(TAG, "onResponse: "+response.getName());
                    }
                });
    }

    /**
     * 根据文件指针,请求BookPageServlet获取 部分 相应的bookJson
     * @param bookName .txt文件名 如：西游记
     * @param position 文件指针   如： 1024
     */
    public void requestBookPageServlet(String bookName,String position)
    {

        RequestData.submit("BookName",bookName,"Position",position,ADRESS+"BookDirectoryServlet")
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: " +e.getMessage());

                        iActivityData.onFail();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: ->"+response);
                        String content=response;

                        Result<Book> bookResult =GsonUtil.fromJsonObject(content,Book.class);
                           Book book =bookResult.getData();
                            iActivityData.onFinish(book);
                    }
                });
    }

    private boolean isHasData()
    {
        if(result!=null&&result.getData()!=null)
        {
            return true;
        }
        return false;
    }
}
