package ouyj.hyena.com.cursorsample;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * LoaderManager，CursorLoader，Loader都使用v4版本
 * 片段类继承自ListFragment（无需布局）
 */
public class ContactFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //使用系统的适配器类
    SimpleCursorAdapter adapter;

    //要查看的数据列
    static final String[] CONTACTS_SUMMARY_PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.LAST_TIME_CONTACTED,
            ContactsContract.Contacts.LOOKUP_KEY,
    };
    /**
     * 构造方法
     */
    public ContactFragment() { }

    /**
     * 父活动已创建完成后的回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No contacts yet");

        //创建适配器（数据源暂为null）
        adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.LAST_TIME_CONTACTED
                },
                new int[]{
                        android.R.id.text1,
                        android.R.id.text2
                },
                0
        );
        //将适配器赋值给内置列表视
        setListAdapter(adapter);

        //创建加载器管理对象
        getLoaderManager().initLoader(0,
                null,
                this
        );
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
        + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
        + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

        //
        return new CursorLoader(getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                CONTACTS_SUMMARY_PROJECTION,
                select,
                null,
                null
        );
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
