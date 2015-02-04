package com.miya38.utils;

/**
 * 数値操作ユーティリティ
 *
 * @author y-miyazaki
 *
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
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(Integer number) {
        if (number == null || number == 0) {
            return true;
        }
        return false;
    }

    /**
     * Long型数値オブジェクトの空/0チェック
     *
     * @param number
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(Long number) {
        if (number == null || number == 0) {
            return true;
        }
        return false;
    }
}
