package ouyj.hyena.com.contentmodule.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    //数据库名称版本号和表名
    private static final String DATABASE_NAME = "finch.db";
    private static final int DATABASE_VERSION = 1;
    public static final String USER_TABLE = "user";
    public static final String JOB_TABLE = "job";

    /**
     * 构造方法
     * @param context
     */
    public DBHelper(Context context) {
        super(
                context, DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }
    /**
     * 创建数据库文件
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + JOB_TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " job TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
