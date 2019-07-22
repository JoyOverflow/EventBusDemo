package ouyj.hyena.com.learnpinyin.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 工具类
 */
public class Utility {

    /**
     * 复制数据库文件
     * @param c
     * @throws IOException
     */
    public static void copyDataBase(Context c) throws IOException {

        //打开Assets目录内的文件
        String dbname = "PinyinCharacters.db";
        InputStream input = c.getAssets().open(dbname);

        //创建目录路径
        String packageName="ouyj.hyena.com.learnpinyin";
        String filePath=String.format("/data/data/%s/databases",packageName);
        final File dir = new File(c.getFilesDir() + filePath);
        dir.mkdirs();

        //打开数据库
        PinYinDbHelper myDbHelper = new PinYinDbHelper(c);
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        db.close();

        //复制到目标文件
        String dbPath=String.format("/data/data/%s/databases/%s",packageName,"PinyinCharacters.db");
        OutputStream output = new FileOutputStream(dbPath);
        byte[] buffer = new byte[10240];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        //关闭流
        output.flush();
        output.close();
        input.close();
    }

    /**
     * 检测数据库文件是否存在
     * @param context
     * @param dbName
     * @return
     */
    public static boolean doesDatabaseExist(Context context, String dbName) {
        File file = context.getDatabasePath(dbName);
        return file.exists();
    }
}
