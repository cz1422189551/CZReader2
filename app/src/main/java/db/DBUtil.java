package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.LocalBook;

/**
 * Created by Administrator on 2017-6-30.
 */

public class DBUtil {

    private static final String TAG = "DBUtil";

    public static final String DB_NAME="cz_reader";

    public static final int VERSION=1;

    private static DBUtil dbUtil;

    private SQLiteDatabase db;

    private DBUtil(Context mContext)
    {
        ReaderOpenHelp dbHelper=new ReaderOpenHelp(mContext,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }
    /**
     * 单例模式构造dbUtil对象
     * @param context
     * @return
     */
    public synchronized  static DBUtil getInstance(Context context)
    {
        if(dbUtil==null)
        {
            dbUtil=new DBUtil(context);
        }
        return dbUtil;
    }
    /**
     * 更新localbook表中数据并 查询
     * @param position
     * @param bId
     */
    public void updatePos(long position,int bId)
    {
        String tempa=String.valueOf(position);
        db.execSQL("update localbook set b_pos=? where b_id=?",new String[]{tempa,String.valueOf(bId)});
       LocalBook b= loadLocalBookById();
        Log.d(TAG, "updatePos: "+b.getbPos());
    }
    /**
     * 删除localbook表中的数据
     * @param bId  Id
     */
    public void deleteLocal(int bId)
    {
        String query="delete localbook  where b_id='"+bId+"'";
        Log.d(TAG, "deleteLocal: "+query);
        db.execSQL(query);
    }
    public void saveLocalBook(LocalBook localBook)
    {
        if(localBook!=null)
        {
            ContentValues values=new ContentValues();
            values.put("b_name",localBook.getbName());
            values.put("b_path",localBook.getbPath());
            values.put("b_pos",localBook.getbPos());
            values.put("b_lasttime",localBook.getLastTime());
            db.insert("localbook",null,values);
        }
    }
    public LocalBook  loadLocalBookById()
    {
       Cursor cursor= db.rawQuery("select * from localbook where b_id=?",new String[]{"1"});
        LocalBook localBook =new LocalBook();
        if(cursor.moveToFirst())
        {
            do{

                localBook.setbId(cursor.getInt(cursor.getColumnIndex("b_id")));
                localBook.setbName(cursor.getString(cursor.getColumnIndex("b_name")));
                localBook.setbPath(cursor.getString(cursor.getColumnIndex("b_path")));
                localBook.setbPos(cursor.getInt(cursor.getColumnIndex("b_pos")));
                localBook.setLastTime(cursor.getString(cursor.getColumnIndex("b_lasttime")));
            }while (cursor.moveToNext());
            return localBook;
        }
        return null;
    }
    public List<LocalBook>  loadLocalBooks()
    {
        List<LocalBook> list=new ArrayList<>();
        Cursor cursor =db.query("localbook",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do{
                LocalBook localBook =new LocalBook();
                localBook.setbId(cursor.getInt(cursor.getColumnIndex("b_id")));
                localBook.setbName(cursor.getString(cursor.getColumnIndex("b_name")));
                localBook.setbPath(cursor.getString(cursor.getColumnIndex("b_path")));
                localBook.setbPos(cursor.getInt(cursor.getColumnIndex("b_pos")));
                localBook.setLastTime(cursor.getString(cursor.getColumnIndex("b_lasttime")));
                list.add(localBook);
            }while (cursor.moveToNext());

        }
        return list;
    }

}
