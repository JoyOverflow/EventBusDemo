package ouyj.hyena.com.cursorsample;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ouyj.hyena.com.cursorsample.data.Person;

/**
 * 使用Loader异步加载ContentProvider中的内容
 */
public class AsyncSwapActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter adapter;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_swap);

        //定义适配器其数据源暂为null（用来绑定数据库里的数据集并指定布局资源文件）
        //FLAG_REGISTER_CONTENT_OBSERVER表示需用CursorLoader来加载数据
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item,
                null,
                new String[] {Person.NAME, Person.AGE},
                new int[] {R.id.txtName, R.id.txtAge},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        //获得列表视引用并设置适配器（将使用CursorLoader异步加载来的数据源）
        ListView listView = findViewById(R.id.lstView);
        listView.setAdapter(adapter);

        //列表视的项点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //附加上ID的URI
                Uri uri = ContentUris.withAppendedId(Person.CONTENT_URI, id);
                //查询当前ID的记录
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] {Person.AGE},
                        null,
                        null,
                        null
                );
                //游标移至起始处
                if (cursor.moveToFirst()) {
                    //得到指定字段值
                    int age = cursor.getInt(cursor.getColumnIndex(Person.AGE));
                    //更新数据库记录
                    ContentValues values = new ContentValues();
                    values.put(Person.AGE, age + 1);
                    getContentResolver().update(uri, values, null, null);
                }
            }
        });

        //创建异步加载管理器（指定加载器的ID并由当前活动来处理回调）
        //如果指定ID的加载器不存在，则立即触发onCreateLoader方法
        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);
    }



    /**
     * 创建出新加载器
     * 无需获取它的引用（LoaderManager将自动管理加载器的状态和生命周期）
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader！");

        //CursorLoader派生自Loader类（它将查询ContentResolver并返回一个Cursor）
        //CursorLoader构造方法内是对ContentProvider执行查询时的所需信息
        return new CursorLoader(
                this, 
                Person.CONTENT_URI, //用于检索的URI
                null,     //返回所有字段=null
                null,      //Where子句的字段
                null,  //Where子句的字段（值）
                null      //排序规则
        );
    }
    /**
     * 加载器加载数据或数据变化完成后回调（不应手动管理游标的关闭，加载器自会处理）
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished！");

        //关闭之前的Cursor，并使用新的Cursor对象
        adapter.swapCursor(data);
    }
    /**
     * 放弃所有当前数据，重启加载器（如用户查询的更改）
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset！");

        //关闭当前Cursor
        adapter.swapCursor(null);
    }
}
