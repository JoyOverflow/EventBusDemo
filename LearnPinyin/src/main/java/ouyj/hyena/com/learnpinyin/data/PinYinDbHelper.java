package ouyj.hyena.com.learnpinyin.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ouyj.hyena.com.learnpinyin.data.PinYinContract.Character;

/**
 * Created by xuzhi on 2016/8/25.
 */
public class PinYinDbHelper extends SQLiteOpenHelper {

    //数据库名称和版本
    private static final String DATABASE_NAME = "PinyinCharacters.db";
    private static final int DATABASE_VERSION = 2;
    private final String TAG = this.getClass().getSimpleName();

    /**
     * 构造方法
     * @param context
     */
    public PinYinDbHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
        Log.d(TAG, "PinYinDbHelper！");
    }
    /**
     * 创建数据库文件
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate！");
        String sql = "CREATE TABLE " + Character.TABLE_NAME + " (" +
                Character._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                Character.COLUMN_NAME + " TEXT NOT NULL, " +
                Character.COLUMN_DONE + " TEXT NOT NULL, " +
                Character.COLUMN_PRONUNCIATION + " TEXT NOT NULL, " +
                Character.COLUMN_DISPLAY_SEQUENCE + " INTEGER NOT NULL);";

        //db.execSQL(sql);
    }
    /**
     * 升级数据库文件
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade！");
        db.execSQL("DROP TABLE IF EXISTS " + Character.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onDowngrade！");
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
