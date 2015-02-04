package com.miya38.utils;

import java.text.DecimalFormat;

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

    /**
     * 数値をカンマ区切りにする。
     * ex:1000000->100,0000
     *
     * @param tmpNum
     *            数値
     * @param defaultValue
     *            数値が空の場合のデフォルト文字列
     * @param isDecimalOne
     *            true:小数点1桁移行は切り捨てる.
     *            false:小数点1桁移行は切り捨てる.
     * @return カンマ区切りの数値
     */
    public static String addCanma(final String num, final String defaultValue, final boolean isDecimalOne) {
        String text = "";
        String tmpNum = new String(num);

        if (!StringUtils.isEmpty(tmpNum)) {
            try {
                tmpNum = numMicrometer(tmpNum);
                if (tmpNum.startsWith(".")) {
                    if (tmpNum.length() > 1 && tmpNum.charAt(1) - 48 > 0) {
                        text = "0." + (tmpNum.charAt(1) - 48);
                    } else {
                        text = tmpNum;
                    }
                } else {
                    if (tmpNum.contains(".")) {
                        final int start = tmpNum.indexOf(".");
                        String numTemp = "";
                        if (start + 1 < tmpNum.length()) {
                            // ---------------------------------------------------------------
                            // 小数点1桁以降は切り捨てる場合
                            // ---------------------------------------------------------------
                            if (isDecimalOne) {
                                numTemp = tmpNum.substring(start + 1, start + 2);
                            }
                            // ---------------------------------------------------------------
                            // 小数点1桁以降は切り捨てない場合
                            // ---------------------------------------------------------------
                            else {
                                numTemp = tmpNum.substring(start + 1);
                            }
                            if (Integer.parseInt(numTemp) > 0) {
                                numTemp = "." + numTemp;
                            } else {
                                numTemp = "";
                            }
                        }
                        final Long number = Long.parseLong(tmpNum.substring(0, start));
                        if (number >= 1000) {
                            final DecimalFormat df = new DecimalFormat(",###");
                            text = df.format(number);
                        } else {
                            text = String.valueOf(number);
                        }
                        text += numTemp;
                    } else {
                        final Long number = Long.parseLong(tmpNum);
                        if (number > 999) {
                            final DecimalFormat df = new DecimalFormat(",###");
                            text = String.valueOf(df.format(number));
                        } else {
                            text = tmpNum;
                        }
                    }

                }
            } catch (final Exception e) {
                // 入力値によりアプリが落ちないようエラーはスルーする。
            }
        } else {
            text = defaultValue;
        }
        return text;
    }

    /**
     * カンマを取り除き、空文字場合は0を自動的に付与します。
     *
     * @param text
     *            テキスト
     * @return カンマ除去後の文字列
     */
    public static String numMicrometer(final String text) {
        if (StringUtils.isBlank(text)) {
            return "0";
        }
        return text.replace(",", "");
    }

}
