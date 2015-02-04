package com.miya38.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.miya38.exception.ApplicationException;

/**
 * アプリケーションユーティリティ
 *
 * @author y-miyazaki
 *
 */
public final class ResourceUtils {
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ResourceUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param context
     *            {@link Context}
     */
    public static void configure(Context context) {
        sContext = context;
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *            リソースID
     * @return 文字列
     */
    public static int[] getIntArray(int arrayId) {
        return sContext.getResources().getIntArray(arrayId);
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *            リソースID
     * @return 文字列
     */
    public static int getIntArray(int arrayId, int index) {
        final int[] names = sContext.getResources().getIntArray(arrayId);
        try {
            return names[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        }
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *            リソースID
     * @return 文字列
     */
    public static String[] getStringArray(int arrayId) {
        return sContext.getResources().getStringArray(arrayId);
    }

    /**
     * array.xmlのString配列データから文字列を取得する
     *
     * @param arrayId
     *            リソースID
     * @param index
     *            添え字
     * @return 文字列
     */
    public static String getStringArray(int arrayId, int index) {
        final String[] names = sContext.getResources().getStringArray(arrayId);
        try {
            return names[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        }
    }

    /**
     * array.xmlのTypedArray配列データを取得する
     *
     * @param arrayId
     *            リソースID
     * @return 文字列
     */
    public static TypedArray getObtaionTypedArray(int arrayId) {
        return sContext.getResources().obtainTypedArray(arrayId);
    }

    /**
     * array.xmlのTypedArray配列データからresourceIdを取得する
     *
     * @param arrayId
     *            リソースID
     * @param index
     *            添え字
     * @return 文字列
     */
    public static int getObtaionTypedArray(int arrayId, int index) {
        final TypedArray names = sContext.getResources().obtainTypedArray(arrayId);
        try {
            return names.getResourceId(index, -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        }
    }

    /**
     * drawableIdを文字列取得する。
     *
     * @param drawable
     *            画像名
     * @return R.drwable.{引数で設定したdrawable名}のID
     */
    public static int getDrawableId(String drawable) {
        return sContext.getResources().getIdentifier(drawable, "drawable", sContext.getPackageName());
    }

    /**
     * カラーコード取得
     *
     * @param id
     *            R.color.???
     * @return カラーコード
     */
    public static int getColor(int id) {
        return sContext.getResources().getColor(id);
    }
}
