package com.miya38.utils;

import java.text.NumberFormat;

/**
 * 数値操作ユーティリティ
 *
 * @author y-miyazaki
 */
public final class NumberUtils {

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private NumberUtils() {
    }

    /**
     * Integer型数値オブジェクトの空/0チェック
     *
     * @param number
     *         数値
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(final Integer number) {
        if (number == null || number == 0) {
            return true;
        }
        return false;
    }

    /**
     * Long型数値オブジェクトの空/0チェック
     *
     * @param number
     *         数値
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(final Long number) {
        if (number == null || number == 0) {
            return true;
        }
        return false;
    }

    /**
     * Double型数値オブジェクトの空/0チェック
     *
     * @param number
     *         数値
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(final Double number) {
        if (number == null || number == 0) {
            return true;
        }
        return false;
    }

    /**
     * 数値をカンマ区切りにする。
     * ex:1000000->100,0000
     *
     * @param number
     *         数値
     * @return カンマ区切りの数値
     */
    public static String addCanma(final String number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(number);
    }

    /**
     * 数値をカンマ区切りにする。
     * ex:1000000->100,0000
     *
     * @param number
     *         数値
     * @param defaultValue
     *         カンマ区切りできない場合の返却値
     * @return カンマ区切りの数値
     */
    public static String addCanma(final String number, String defaultValue) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            return numberFormat.format(number);
        } catch (Exception e) {
            // 何もしない。
        }
        return defaultValue;
    }

    /**
     * 数値をカンマ区切りにする。
     * ex:1000000->100,0000
     *
     * @param number
     *         数値
     * @return カンマ区切りの数値
     */
    public static String addCanma(final int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(number);
    }

    /**
     * カンマを取り除き、空文字場合は0を自動的に付与します。
     *
     * @param number
     *         テキスト
     * @return カンマ除去後の文字列
     */
    public static String removeCanma(final String number) {
        if (StringUtils.isBlank(number)) {
            return "0";
        }
        return number.replace(",", "");
    }
}
