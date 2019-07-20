package ouyj.hyena.com.cursorsample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    //数据库文件名称和版本号
    private static final String DATABASE_NAME = "persons.db";
    private static final int DB_VERSION = 1;
    //数据表名称
    public static final String TABLE_NAME = "persons";


    /**
     * 构造方法（传递上下文，数据库文件名称和版本号）
     * @param context
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    /**
     * 数据库文件不存在时执行（创建数据库）
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("Create Table " + TABLE_NAME + " ("
                + Person._ID + " Integer Primary Key,"
                + Person.NAME + " TEXT,"
                + Person.AGE + " Integer"
                + ");"
        );
        //插入表数据
        db.execSQL("Insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('本田圭佑', 24);");
        db.execSQL("Insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('遠藤保仁', 30);");
        db.execSQL("Insert into " + TABLE_NAME + "(" + Person.NAME + "," + Person.AGE + ") values ('松井大輔', 29);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
