package ouyj.hyena.com.testmodule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //跨进程要操作的表
        Uri uri_user = Uri.parse("content://ouyj.hyena.com.job/user");

        ContentValues values = new ContentValues();
        values.put("_id", 4);
        values.put("name","gdkong");


        ContentResolver resolver = getContentResolver();
        resolver.insert(uri_user,values);
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
