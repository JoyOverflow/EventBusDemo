package ouyj.hyena.com.cursorsample.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * 内容提供器
 * （清单文件中声明为：ouyj.hyena.com.cursorsample.provider）
 */
public class PersonProvider extends ContentProvider {

    private DbHelper personOpenHelper;
    private static final int PERSONS = 1;
    private static final int PERSON_ID = 2;
    private static HashMap<String, String> personMap;

    /**
     * 构造方法
     */
    public PersonProvider() { }


    //在ContentProvider类中注册URI（注册后就可使用matcher.match(uri)方法对输入的Uri进行匹配）
    private static final UriMatcher matcher;
    static {
        //常量NO_MATCH表示暂不匹配任何路径的返回码
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //如果匹配"ouyj.hyena.com.cursorsample.provider/persons"则返回匹配码PERSONS
        matcher.addURI(Person.AUTHORITY, "persons", PERSONS);
        //如果匹配"ouyj.hyena.com.cursorsample.provider/persons/#"则返回匹配码PERSON_ID
        //#号为通配符可为任意值
        matcher.addURI(Person.AUTHORITY, "persons/#", PERSON_ID);

        //设置数据表字段的别名（映射中key和value可以完全相同）
        personMap = new HashMap<>();
        personMap.put(Person._ID, Person._ID);
        personMap.put(Person.NAME, Person.NAME);
        personMap.put(Person.AGE, Person.AGE);
    }





    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        int count;
        switch (matcher.match(uri)) {
            case PERSONS:
                count = db.delete(DbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case PERSON_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DbHelper.TABLE_NAME, Person._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case PERSONS:
                return Person.CONTENT_TYPE;
            case PERSON_ID:
                return Person.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (matcher.match(uri) != PERSONS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (values.containsKey(Person.NAME) == false) {
            values.put(Person.NAME, "詠み人知らず");
        }

        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        long rowId = db.insert(DbHelper.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(Person.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        personOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sb = new SQLiteQueryBuilder();
        sb.setTables(DbHelper.TABLE_NAME);
        switch (matcher.match(uri)) {
            case PERSONS:
                sb.setProjectionMap(personMap);
                break;
            case PERSON_ID:
                sb.setProjectionMap(personMap);
                sb.appendWhere(Person._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        //查询由SQLiteQueryBuilder来发起
        SQLiteDatabase db = personOpenHelper.getReadableDatabase();
        Cursor c = sb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        //通知数据已发生更改
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }





    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = personOpenHelper.getWritableDatabase();
        int count;
        switch (matcher.match(uri)) {
            case PERSONS:
                count = db.update(DbHelper.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PERSON_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(DbHelper.TABLE_NAME, values, Person._ID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
