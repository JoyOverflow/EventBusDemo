package ouyj.hyena.com.learnpinyin;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Collections;

import ouyj.hyena.com.learnpinyin.data.PinYinContract;
import ouyj.hyena.com.learnpinyin.data.PinYinDbHelper;

public class PinYinProvider extends ContentProvider {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private PinYinDbHelper dbHelper;


    static final int LEARN_PINYIN_CHARACTER = 100;
    static final int LEARN_PINYIN_CHARACTER_WITH_NAME = 101;
    static final int LEARN_PINYIN_CHARACTER_WITH_DONE = 102;
    static final int LEARN_PINYIN_CHARACTER_WITH_ID = 103;
    static final int LEARN_PINYIN_CHARACTER_WITH_ID_LIST = 104;
    static final int LEARN_PINYIN_CHARACTER_WITH_NAME_LIST = 105;


    private static final SQLiteQueryBuilder sLearnPinyinQueryBuilder;
    static{
        sLearnPinyinQueryBuilder = new SQLiteQueryBuilder();
    }

    //在ContentProvider类中注册URI
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PinYinContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER, LEARN_PINYIN_CHARACTER);
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER + "/" + PinYinContract.Character.COLUMN_ID +"/*", LEARN_PINYIN_CHARACTER_WITH_ID);
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER + "/" + PinYinContract.Character.PATH_CHARACTER_ID_LIST +"/*", LEARN_PINYIN_CHARACTER_WITH_ID_LIST);
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER + "/" + PinYinContract.Character.COLUMN_NAME +"/*", LEARN_PINYIN_CHARACTER_WITH_NAME);
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER + "/" + PinYinContract.Character.PATH_CHARACTER_NAME_LIST +"/*", LEARN_PINYIN_CHARACTER_WITH_NAME_LIST);
        matcher.addURI(authority, PinYinContract.PATH_CHARACTER + "/" + PinYinContract.Character.COLUMN_DONE +"/*", LEARN_PINYIN_CHARACTER_WITH_DONE);
        return matcher;
    }



    public PinYinProvider() {
    }


    
    private static final String sCharacterByDoneSelection =
            PinYinContract.Character.TABLE_NAME +
                    "." +  PinYinContract.Character.COLUMN_DONE + " = ? ";
    //Character.name = ?
    private static final String sCharacterByNameSelection =
            PinYinContract.Character.TABLE_NAME +
                    "." +  PinYinContract.Character.COLUMN_NAME + " = ? ";
    //Character._id = ?
    private static final String sCharacterByIdSelection =
            PinYinContract.Character.TABLE_NAME +
                    "." +  PinYinContract.Character.COLUMN_ID + " = ? ";
    //Character._id IN (?,?,?...?)
    private static final String sCharacterByIdListSelection =
            PinYinContract.Character.TABLE_NAME +
                    "." + PinYinContract.Character.COLUMN_ID + " IN ";


    //Character.name IN (?,?,?...?)
    private static final String sCharacterByNameListSelection =
            PinYinContract.Character.TABLE_NAME +
                    "." + PinYinContract.Character.COLUMN_NAME + " IN ";
    private Cursor getCharacterByName(
            Uri uri, String[] projection, String sortOrder) {

        String name = PinYinContract.Character.getNameFromUri(uri);
        Log.e(LOG_TAG, PinYinContract.Character.COLUMN_NAME + " = " + name);
        sLearnPinyinQueryBuilder.setTables(PinYinContract.Character.TABLE_NAME);
        return sLearnPinyinQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                sCharacterByNameSelection,
                new String[]{name},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getCharacterByDone(
            Uri uri, String[] projection, String sortOrder) {

        String done = PinYinContract.Character.getTheSecondPara(uri);
        Log.e(LOG_TAG, PinYinContract.Character.COLUMN_DONE + " = " + done);
        sLearnPinyinQueryBuilder.setTables(PinYinContract.Character.TABLE_NAME);
        return sLearnPinyinQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                sCharacterByDoneSelection,
                new String[]{done},
                null,
                null,
                sortOrder
        );
    }
    private Cursor getCharactersByIdList(
            Uri uri, String[] projection, String sortOrder) {

        String idString = PinYinContract.Character.getTheSecondPara(uri).trim();
        String[] idArray = idString.split(",");
        sLearnPinyinQueryBuilder.setTables(PinYinContract.Character.TABLE_NAME);
        return sLearnPinyinQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                sCharacterByIdListSelection + "(" + TextUtils.join(",", Collections.nCopies(idArray.length, "?")) + ")",/*generate (?,?,?)*/
                idArray,
                null,
                null,
                sortOrder
        );
    }
    private Cursor getCharactersByNameList(
            Uri uri, String[] projection, String sortOrder) {

        String nameString = PinYinContract.Character.getTheSecondPara(uri).trim();
        String[] nameArray = nameString.split("");
        sLearnPinyinQueryBuilder.setTables(PinYinContract.Character.TABLE_NAME);
        return sLearnPinyinQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                sCharacterByNameListSelection + "(" + TextUtils.join(",", Collections.nCopies(nameArray.length, "?")) + ")",/*generate (?,?,?)*/
                nameArray,
                null,
                null,
                sortOrder
        );
    }

    private int UpdateCharacterById(Uri uri, ContentValues values) {
        String id = PinYinContract.Character.getTheSecondPara(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(PinYinContract.Character.TABLE_NAME, values, sCharacterByIdSelection,
                new String[]{id});
    }


    /**
     * 根据条件删除记录
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case LEARN_PINYIN_CHARACTER:
                rowsDeleted = db.delete(
                        PinYinContract.Character.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    /**
     * 根据URI返回MIME类型（MIME类型由主类型+子类型组成，如：text/html）
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LEARN_PINYIN_CHARACTER:
            case LEARN_PINYIN_CHARACTER_WITH_DONE:
            case LEARN_PINYIN_CHARACTER_WITH_ID_LIST:
            case LEARN_PINYIN_CHARACTER_WITH_NAME_LIST:
                return PinYinContract.Character.CONTENT_TYPE;
            case LEARN_PINYIN_CHARACTER_WITH_NAME:
            case LEARN_PINYIN_CHARACTER_WITH_ID:
                return PinYinContract.Character.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        Log.e(LOG_TAG, "insert uri = " + uri.toString());
        switch (match) {
            case LEARN_PINYIN_CHARACTER: {
                long _id = db.insert(PinYinContract.Character.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PinYinContract.Character.buildCharacterUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * 获取数据库连接（初始化）
     * @return
     */
    @Override
    public boolean onCreate() {
        dbHelper = new PinYinDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        Log.e(LOG_TAG, "query uri = " + uri.toString());
        switch (sUriMatcher.match(uri)) {
            case LEARN_PINYIN_CHARACTER_WITH_NAME: {
                retCursor = getCharacterByName(uri, projection, sortOrder);
                break;
            }
            case LEARN_PINYIN_CHARACTER_WITH_DONE: {
                retCursor = getCharacterByDone(uri, projection, sortOrder);
                break;
            }
            case LEARN_PINYIN_CHARACTER_WITH_ID_LIST: {
                retCursor = getCharactersByIdList(uri, projection, sortOrder);
                break;
            }
            case LEARN_PINYIN_CHARACTER_WITH_NAME_LIST: {
                retCursor = getCharactersByNameList(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * 更改数据库记录
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsUpdated;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LEARN_PINYIN_CHARACTER:
                rowsUpdated = db.update(
                        PinYinContract.Character.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case LEARN_PINYIN_CHARACTER_WITH_ID:
                rowsUpdated = UpdateCharacterById(uri, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case LEARN_PINYIN_CHARACTER:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(PinYinContract.Character.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }




}
