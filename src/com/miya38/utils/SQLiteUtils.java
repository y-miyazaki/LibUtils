package com.miya38.utils;

import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database に関するユーティリティメソッドを提供します。
 * 
 * @author y-miyazaki
 */
public final class SQLiteUtils {

    private SQLiteUtils() {
    }

    /**
     * SQLをexecする
     * 
     * @param db
     * @param sqls
     */
    public static void execSQLs(final SQLiteDatabase db, final List<String> sqls) {
        for (final String sql : sqls) {
            db.execSQL(sql);
        }
    }

    /**
     * エラーを無視しながらSQLを実行します。
     * 
     * @param tag
     * @param db
     * @param sql
     */
    public static void forceSQL(final String tag, final SQLiteDatabase db, final String sql) {
        try {
            db.execSQL(sql);
        } catch (final Exception e) {
            LogUtils.w(tag, "error in sql.", e);
        }
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @return 指定カラム
     */
    public static String getString(final Cursor cursor, final String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @return 指定カラム
     */
    public static long getLong(final Cursor cursor, final String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @return 指定カラム
     */
    public static int getInt(final Cursor cursor, final String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @return 指定カラム
     */
    public static Date getDate(final Cursor cursor, final String columnName) {
        final String value = cursor.getString(cursor.getColumnIndex(columnName));
        if (null == value) {
            return null;
        }
        return DateUtils.parse(value);
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @param pattern
     * @return 指定カラム
     */
    public static Date getDate(final Cursor cursor, final String columnName, final String pattern) {
        final String value = cursor.getString(cursor.getColumnIndex(columnName));
        if (null == value) {
            return null;
        }
        return DateUtils.parse(value, pattern);
    }

    /**
     * 指定カラム取得
     * 
     * @param cursor
     * @param columnName
     * @return 指定カラム
     */
    public static boolean getBoolean(final Cursor cursor, final String columnName) {
        return 0 != getInt(cursor, columnName);
    }

    /**
     * 日付パース
     * 
     * @param value
     * @return パースされた日付
     */
    public static Date parseDate(final String value) {
        return DateUtils.parse(value);
    }

    /**
     * 日付パース
     * 
     * @param value
     * @return パースされた日付
     */
    public static String dateToString(final Date value) {
        return DateUtils.toString(value);
    }
}
