package com.miya38.utils;

import android.util.Base64;

/**
 * Base64 に関するユーティリティを提供します。
 * 
 * @author y-miyazaki
 * 
 */
public final class Base64Utils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private Base64Utils() {
    }

    /**
     * base64エンコード
     * 
     * @param data
     *            データ
     * @return base64データ
     */
    public static String base64Encode(final String data) {
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64エンコード
     * 
     * @param data
     *            データ
     * @return base64データ
     */
    public static String base64Encode(final byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * base64デコード
     * 
     * @param data
     *            エンコードデータ
     * @return データ
     */
    public static String base64Decode(final String data) {
        return new String(Base64.decode(data.getBytes(), Base64.DEFAULT));
    }

    /**
     * base64デコード
     * 
     * @param data
     *            エンコードデータ
     * @return データ
     */
    public static String base64Decode(final byte[] data) {
        return new String(Base64.decode(data, Base64.DEFAULT));
    }

    /**
     * base64エンコード
     * 
     * @param data
     *            データ
     * @return base64データ
     */
    public static byte[] base64EncodeByte(final String data) {
        return Base64.encode(data.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64エンコード
     * 
     * @param data
     *            データ
     * @return base64データ
     */
    public static byte[] base64EncodeByte(final byte[] data) {
        return Base64.encode(data, Base64.DEFAULT);
    }

    /**
     * base64デコード
     * 
     * @param data
     *            エンコードデータ
     * @return データ
     */
    public static byte[] base64DecodeByte(final String data) {
        return Base64.decode(data.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64デコード
     * 
     * @param data
     *            エンコードデータ
     * @return データ
     */
    public static byte[] base64DecodeByte(final byte[] data) {
        return Base64.decode(data, Base64.DEFAULT);
    }
}
