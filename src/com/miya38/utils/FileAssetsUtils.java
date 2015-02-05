package com.miya38.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.google.common.io.Closeables;

/**
 * assetsファイル処理クラス<br>
 * このクラスは、assets領域の読み込みを対応する。
 */
public final class FileAssetsUtils {
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private FileAssetsUtils() {
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
     * assetsファイル内容取得
     * 
     * @param filePath
     *            パス
     * @return String
     */
    public static String read(final String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = sContext.getResources().getAssets().open(filePath);
            final byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return new String(data);
        } catch (final IOException e) {
            return null;
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    /**
     * assetsファイル内容取得
     * 
     * @param filePath
     *            パス
     * @return byte
     */
    public static byte[] readByte(final String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = sContext.getResources().getAssets().open(filePath);
            final byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (final IOException e) {
            // 握りつぶしてnullで返す
        } finally {
            Closeables.closeQuietly(inputStream);
        }
        return new byte[0];
    }

    /**
     * assetsファイル内容取得
     * 
     * @param filePath
     *            パス
     * @return InputStream
     */
    public static InputStream readInputStream(final String filePath) {
        try {
            return sContext.getResources().getAssets().open(filePath);
        } catch (final IOException e) {
            // 握りつぶしてnullを返す
        }
        return null;
    }
}
