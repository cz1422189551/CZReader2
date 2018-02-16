package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ReaderOpenHelp extends SQLiteOpenHelper {

	private static final String LOCALBOOK="create  table  localbook( "
			+ "b_id  integer primary key AUTOINCREMENT   ,"
			+ " b_name          text,  "
			+ "b_pos       integer,"
			+ " b_path     text, "
			+ "b_lasttime      text)";

	public ReaderOpenHelp(Context context, String name, CursorFactory factory,
                          int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(LOCALBOOK);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
