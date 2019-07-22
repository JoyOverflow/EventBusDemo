package ouyj.hyena.com.learnpinyin.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
/**
 * Created by xuzhi on 2016/8/25.
 */
public class PinYinContract {

    //字段值
    public static final String NO = "no";
    //URI路径
    public static final String CONTENT_AUTHORITY = "ouyj.hyena.com.learnpinyin.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //数据表名称
    public static final String PATH_CHARACTER = Character.TABLE_NAME;





    private final String LOG_TAG = this.getClass().getSimpleName();



    public static final String YES = "yes";

    public static final String FINISHED = "finished";




    public static final class Character implements BaseColumns {

        //表名称
        public static final String TABLE_NAME = "Characters";
        //字段名称
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DONE = "done";
        public static final String COLUMN_PRONUNCIATION = "pronunciation";
        public static final String COLUMN_DISPLAY_SEQUENCE = "display_sequence";
        //指定数据表的URI访问路径
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHARACTER).build();








        private final String LOG_TAG = this.getClass().getSimpleName();

        //多条记录（集合）
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;
        //单条记录
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;







        public static final String PATH_CHARACTER_ID_LIST = "idList";
        public static final String PATH_CHARACTER_NAME_LIST = "nameList";

        public static Uri buildCharacterUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildCharacterUriByDone(String done) {
            return CONTENT_URI.buildUpon().appendPath(COLUMN_DONE).appendPath(done).build();
        }

        public static Uri buildCharacterUriById(int id) {
            Log.e("BY ID","ID  = " + id);
            return CONTENT_URI.buildUpon().appendPath(COLUMN_ID).appendPath(Integer.toString(id)).build();
        }

        public static Uri buildCharacterUriByIdList(String idListString)
        {
            Log.v("Id", "idString = " + idListString);
            return CONTENT_URI.buildUpon().appendPath(PATH_CHARACTER_ID_LIST).appendPath(idListString).build();
        }
        public static Uri buildCharacterUriByNameList(String nameListString)
        {
            Log.v("nameList", "nameListString = " + nameListString);
            return CONTENT_URI.buildUpon().appendPath(PATH_CHARACTER_NAME_LIST).appendPath(nameListString).build();
        }


        public static String getTheSecondPara(Uri uri) {
            return uri.getPathSegments().get(2);
        }
        public static String getTheThirdPara(Uri uri) {
            return uri.getPathSegments().get(3);
        }
        public static String getNameFromUri(Uri uri) {
            return getTheSecondPara(uri);
        }
    }

}
