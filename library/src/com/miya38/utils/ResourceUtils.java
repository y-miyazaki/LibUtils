package com.miya38.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;

import com.miya38.exception.ApplicationException;

/**
 * リソースユーティリティ
 *
 * @author y-miyazaki
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
     *         {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *         リソースID
     * @return 文字列
     */
    public static int[] getIntArray(@ArrayRes final int arrayId) {
        return sContext.getResources().getIntArray(arrayId);
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *         リソースID
     * @param index
     *         インデックス配列
     * @return 文字列
     */
    public static int getIntArray(@ArrayRes final int arrayId, final int index) {
        final int[] names = sContext.getResources().getIntArray(arrayId);
        try {
            return names[index];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        }
    }

    /**
     * array.xmlのString配列データを取得する
     *
     * @param arrayId
     *         リソースID
     * @return 文字列
     */
    public static String[] getStringArray(@ArrayRes final int arrayId) {
        return sContext.getResources().getStringArray(arrayId);
    }

    /**
     * array.xmlのString配列データから文字列を取得する
     *
     * @param arrayId
     *         配列リソースID
     * @param index
     *         添え字
     * @return 文字列
     */
    public static String getStringArray(@ArrayRes final int arrayId, final int index) {
        final String[] names = sContext.getResources().getStringArray(arrayId);
        try {
            return names[index];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        }
    }

    /**
     * array.xmlのTypedArray配列データを取得する
     *
     * @param arrayId
     *         配列リソースID
     * @return 文字列
     */
    public static TypedArray getObtainTypedArray(@ArrayRes final int arrayId) {
        return sContext.getResources().obtainTypedArray(arrayId);
    }

    /**
     * array.xmlのTypedArray配列データからresourceIdを取得する
     *
     * @param arrayId
     *         配列リソースID
     * @param index
     *         添え字
     * @return 文字列
     */
    public static int getObtainTypedArray(@ArrayRes final int arrayId, final int index) {
        final TypedArray names = getObtainTypedArray(arrayId);
        try {
            return names.getResourceId(index, -1);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ApplicationException("ArrayIndexOutOfBoundsException", e);
        } finally {
            names.recycle();
        }
    }

    /**
     * drawableIdを文字列取得する。
     *
     * @param drawable
     *         画像名
     * @return R.drwable.{引数で設定したdrawable名}のID
     */
    public static int getDrawableId(final String drawable) {
        return sContext.getResources().getIdentifier(drawable, "drawable", sContext.getPackageName());
    }

    /**
     * カラーコード取得
     *
     * @param id
     *         カラーリソースID
     * @return カラーコード
     */
    public static int getColor(@ColorRes final int id) {
        return sContext.getResources().getColor(id);
    }
}
