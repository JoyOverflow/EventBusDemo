package ouyj.hyena.com.contentmodule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FirstActivity extends AppCompatActivity {

    public static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        //得到user表的uri
        Uri uri_user = Uri.parse("content://ouyj.hyena.com.job/user");

        //通过ContentResolver与外部进行交互
        ContentResolver resolver =  getContentResolver();

        //插入数据（插入失败不会异常）
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put("name", "ouyj");
        resolver.insert(uri_user,values);

        //通过ContentResolver查询数据（获得游标）
        Cursor cursor = resolver.query(
                uri_user,
                new String[]{"_id","name"},
                null,
                null,
                null
        );
        while (cursor.moveToNext()){

            String str=String.format(
                    "编号:%d 姓名:%s",
                    cursor.getInt(0),
                    cursor.getString(1)
            );
            Log.d(TAG, str);
        }
        cursor.close();




    }
}
