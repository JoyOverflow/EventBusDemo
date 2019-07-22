package ouyj.hyena.com.learnpinyin;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import static ouyj.hyena.com.learnpinyin.data.Utility.copyDataBase;
import static ouyj.hyena.com.learnpinyin.data.Utility.doesDatabaseExist;



public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取动作条引用（设置动作条的图标）
        ActionBar actionBar = getSupportActionBar();
        //隐藏动作条
        //actionBar.hide();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //判断数据库文件是否存在（不存在则复制过去）
        Boolean dbExist = doesDatabaseExist(this,"PinyinCharacters.db");
        if(!dbExist) {
            try {
                Log.d(TAG, "复制数据库文件！！！");
                copyDataBase(getBaseContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Log.d(TAG, "已存在数据库文件！！！");
    }

    /**
     * 活动的选项菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
