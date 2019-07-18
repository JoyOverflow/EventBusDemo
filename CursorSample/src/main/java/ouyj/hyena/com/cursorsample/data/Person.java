package ouyj.hyena.com.cursorsample.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 数据表对应的实体类（继承自BaseColumns）
 */
final public class Person implements BaseColumns {

    //具体表的URI
    public static final String AUTHORITY = "ouyj.hyena.com.cursorsample.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/persons");


    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gudon.persons";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gudon.persons";
    public static final String NAME = "name";
    public static final String AGE = "age";
}



