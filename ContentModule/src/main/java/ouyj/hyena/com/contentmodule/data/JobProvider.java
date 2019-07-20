package ouyj.hyena.com.contentmodule.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import ouyj.hyena.com.contentmodule.FirstActivity;

public class JobProvider extends ContentProvider {

    private Context context;
    DBHelper mDbHelper = null;
    SQLiteDatabase db = null;
    public static final String AUTOHORITY = "ouyj.hyena.com.job";
    public static final int User_Code = 1;
    public static final int Job_Code = 2;

    /**
     * 构造方法
     */
    public JobProvider() { }


    //在ContentProvider类中注册URI
    private static final UriMatcher matcher;
    static{
        //常量NO_MATCH表示不匹配任何路径的返回码
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //如果匹配"content://ouyj.hyena.com.job/user"路径，则返回匹配码User_Code
        matcher.addURI(AUTOHORITY,"user", User_Code);
        //如果匹配"content://ouyj.hyena.com.job/job"路径，则返回匹配码Job_Code
        matcher.addURI(AUTOHORITY, "job", Job_Code);
    }
    /**
     * 根据URI匹配来获取对应的表名
     * @param uri
     * @return
     */
    private String getTableName(Uri uri){
        String tableName = null;
        switch (matcher.match(uri)) {
            case User_Code:
                tableName = DBHelper.USER_TABLE;
                break;
            case Job_Code:
                tableName = DBHelper.JOB_TABLE;
                break;
        }
        return tableName;
    }





    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    @Override
    public String getType(Uri uri) {
        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //得到表名并向该表添加数据
        String table = getTableName(uri);
        db.insert(table, null, values);

        //通知外部数据已变化
        Log.d(FirstActivity.TAG, "JobProvider insert！");
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }


    /**
     * 初始化数据库连接（运行于主线程，因此不要做耗时操作）
     * @return
     */
    @Override
    public boolean onCreate() {
        context = getContext();
        mDbHelper = new DBHelper(context);
        db = mDbHelper.getWritableDatabase();

        //删除表记录并加入数据
        db.execSQL("delete from user");
        db.execSQL("delete from job");
        db.execSQL("insert into user values(1,'Carson');");
        db.execSQL("insert into user values(2,'Kobe');");
        db.execSQL("insert into job values(1,'Android');");
        db.execSQL("insert into job values(2,'iOS');");
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        return db.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
