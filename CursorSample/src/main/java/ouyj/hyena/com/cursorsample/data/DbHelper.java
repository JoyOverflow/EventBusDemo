package ouyj.hyena.com.cursorsample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    final static private int DB_VERSION = 1;
    final static String TABLE_NAME = "persons";

    /**
     * 构造方法
     * @param context
     */
    public DbHelper(Context context) {
        super(context, null, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + " (" + Person._ID + " INTEGER PRIMARY KEY," + Person.NAME + " TEXT,"
                + Person.AGE + " INTEGER" + ");"
        );
        //插入表数据
        db.execSQL("insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('本田圭佑', 24);");
        db.execSQL("insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('遠藤保仁', 30);");
        db.execSQL("insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('松井大輔', 29);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
