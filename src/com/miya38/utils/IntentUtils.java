package com.miya38.utils;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * インテントユーティリティ
 *
 * @author y-miyazaki
 *
 */
public final class IntentUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private IntentUtils() {
    }

    // ---------------------------------------------------------------
    // Intentデータ取得
    // ---------------------------------------------------------------

    /**
     * bundleの取得が可能か？
     *
     * @param intent
     *            Intent
     * @return true:bundleがnullではない<br>
     *         false:bundleがnullもしくは引数のintentがnull
     */
    public static boolean isGetExtras(final Intent intent) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 前画面からデータ(int)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @return 取得値(intentをセットしていないもしくは値がnull)
     */
    public static Integer getInt(final Intent intent, final String key) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getInt(key);
            }
        }
        return null;
    }

    /**
     * 前画面からデータ(int)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static Integer getInt(final Intent intent, final String key, final int defaultValue) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getInt(key, defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * 前画面からデータ(String)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @return 取得値(intentをセットしていないもしくは値がnull)
     */
    public static String getString(final Intent intent, final String key) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getString(key);
            }
        }
        return null;
    }

    /**
     * 前画面からデータ(String)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    @SuppressLint("NewApi")
    public static String getString(final Intent intent, final String key, final String defaultValue) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getString(key, defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * 前画面からデータ(String)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static ArrayList<String> getStringArray(final Intent intent, final String key, final ArrayList<String> defaultValue) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getStringArrayList(key);
            }
        }
        return defaultValue;
    }

    /**
     * 前画面からデータ(Boolean)を取得する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static Boolean getBoolean(final Intent intent, final String key, final boolean defaultValue) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return extras.getBoolean(key, defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * 前画面からデータ(Serializable)を取得する
     *
     * @param <V>
     *            Serializable
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @return 取得値(intentをセットしていないもしくは値がnull)
     */
    @SuppressWarnings("unchecked")
    public static <V extends Serializable> V getSerializable(final Intent intent, final String key) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return (V) extras.getSerializable(key);
            }
        }
        return null;
    }

    /**
     * 前画面からデータ(Serializable)を取得する
     *
     * @param <V>
     *            Parcelable
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     * @return 取得値(intentをセットしていないもしくは値がnull)
     */
    @SuppressWarnings("unchecked")
    public static <V extends Parcelable> V getParcelable(final Intent intent, final String key) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(key)) {
                return (V) extras.getParcelable(key);
            }
        }
        return null;
    }

    /**
     * 前画面から取得したキーを削除する
     *
     * @param intent
     *            Intent
     * @param key
     *            取得対象のキー
     */
    public static void remove(final Intent intent, final String key) {
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                extras.remove(key);
            }
        }
    }

    // ---------------------------------------------------------------
    // Intent暗黙
    // ---------------------------------------------------------------
    /**
     * ブラウザを起動します
     *
     * @param context
     *            Context
     * @param uri
     *            URL
     */
    public static void startBrowser(final Context context, final Uri uri) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
