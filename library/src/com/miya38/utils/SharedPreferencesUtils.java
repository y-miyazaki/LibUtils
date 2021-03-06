package com.miya38.utils;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharePreferencesユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class SharedPreferencesUtils {
    /** PRIVATE KEY */
    private static final String PERERENCE_KEY = "PRIVATE";
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private SharedPreferencesUtils() {

    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     * 
     * @param context
     *            {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    /**
     * SharedPreferencesにStringを保存します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    @SuppressLint("CommitPrefEdits")
    public static void putString(final String key, final String value) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.putString(key, value);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからStringを取得します。
     * 
     * @param key
     *            キー
     * @return String
     */
    public static String getString(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    /**
     * SharedPreferencesにintを保存します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    @SuppressLint("CommitPrefEdits")
    public static void putInt(final String key, final Integer value) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.putInt(key, value);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからintを取得します。
     * 
     * @param key
     *            キー
     * @return int
     */
    public static int getInt(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    /**
     * SharedPreferencesにLongを保存します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    @SuppressLint("CommitPrefEdits")
    public static void putLong(final String key, final Long value) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.putLong(key, value);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからLongを取得します。
     * 
     * @param key
     *            キー
     * @return long
     */
    public static long getLong(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    /**
     * SharedPreferencesにFloatを保存します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    @SuppressLint("CommitPrefEdits")
    public static void putFloat(final String key, final Float value) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからFloatを取得します。
     * 
     * @param key
     *            キー
     * @return float
     */
    public static float getFloat(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        return preferences.getFloat(key, 0);
    }

    /**
     * SharedPreferencesにBooleanを保存します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    @SuppressLint("CommitPrefEdits")
    public static void putBoolean(final String key, final Boolean value) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからBooleanを取得します。
     * 
     * @param key
     *            キー
     * @return boolean
     */
    public static boolean getBoolean(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    /**
     * SharedPreferencesから指定のキーを削除します。
     * 
     * @param key
     *            キー
     */
    @SuppressLint("CommitPrefEdits")
    public static void remove(final String key) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.remove(key);
        editorCommit(editor);
    }

    /**
     * SharedPreferencesからすべてのキーを削除します。
     */
    @SuppressLint("CommitPrefEdits")
    public static void clear() {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Editor editor = preferences.edit();
        editor.clear();
        editorCommit(editor);
    }

    /**
     * SharedPreferencesから指定されたExcludeのものを除いて全て削除します。
     * 
     * @param excludeList
     *            削除対象外リストキー
     */
    @SuppressLint("CommitPrefEdits")
    public static void clear(final List<String> excludeList) {
        final SharedPreferences preferences = sContext.getSharedPreferences(PERERENCE_KEY, Context.MODE_PRIVATE);
        final Map<String, ?> list = preferences.getAll();
        final Editor editor = preferences.edit();

        for (final Map.Entry<String, ?> e : list.entrySet()) {
            boolean checkExclude = false;
            for (final String exclude : excludeList) {
                if (e.getKey().equals(exclude)) {
                    checkExclude = true;
                    break;
                }
            }
            if (!checkExclude) {
                editor.remove(e.getKey());
            }
        }
        editorCommit(editor);
    }

    /**
     * Editer commit/applyメソッド
     * 
     * @param editor
     *            {@link Editor}
     */
    private static void editorCommit(final Editor editor) {
        if (AplUtils.hasGingerbread()) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
