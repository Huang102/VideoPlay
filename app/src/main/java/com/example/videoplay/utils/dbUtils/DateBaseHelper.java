package com.example.videoplay.utils.dbUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huangwy
 * @TIME 2016/3/30 20:53
 */
public class DateBaseHelper {

    private final static String SDCARD_PATH = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .getAbsolutePath();
    private final static String DB_NAME = "android_manual.db";
    private final static String DB_PATH = SDCARD_PATH + File.separator
            + DB_NAME;
    private SQLiteDatabase dbConn = null;

    public DateBaseHelper() {
        dbConn = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * @param sql
     * @param selectionArgs
     * @return Cursor
     * @作用：执行带占位符的select语句，查询数据，返回Cursor
     */
    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return dbConn.rawQuery(sql, selectionArgs);
    }

    /**
     * @param sql
     * @param selectionArgs
     * @return int
     * @作用：执行带占位符的select语句，返回结果集的个数
     */
    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } else {
            return 0;
        }
    }

    /**
     * @param sql
     * @param selectionArgs
     * @return List<Map<String, Object>>
     * @作用：执行带占位符的select语句，返回多条数据，放进List集合中。
     */
    public List<Map<String, Object>> selectList(String sql,
                                                String[] selectionArgs) {
        Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
        return cursorToList(cursor);
    }

    /**
     * @param cursor
     * @return List<Map<String, Object>>集合
     * @作用：将Cursor对象转成List集合
     */
    public List<Map<String, Object>> cursorToList(Cursor cursor) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String[] arrColumnName = cursor.getColumnNames();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < arrColumnName.length; i++) {
                Object cols_value = cursor.getString(i);
                map.put(arrColumnName[i], cols_value);
            }
            list.add(map);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * @param sql
     * @param bindArgs
     * @return boolean
     * @作用：执行带占位符的update、insert、delete语句，更新数据库，返回true或false
     */
    public boolean execData(String sql, Object[] bindArgs) {
        try {
            dbConn.execSQL(sql, bindArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 关闭数据库连接
    public void destroy() {
        if (dbConn != null) {
            dbConn.close();
        }
    }
}
