package com.miya38.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import android.text.TextUtils;

/**
 * 文字列操作ユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class StringUtils {
    /** UTF-8 */
    public static final String UTF_8 = "UTF-8";
    /** Used to build output as Hex */
    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /** Used to build output as Hex */
    private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private StringUtils() {
    }

    /**
     * 文字列の前後からスペース(全角半角、改行、タブなど)を削除した文字列を取得
     * 
     * @param string
     *            文字列
     * @return 前後のスペースを削除した文字列
     */
    public static String trim(final String string) {
        if (isEmpty(string)) {
            return string;
        }
        final String trim = string;
        return trim.replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", "");
    }

    /**
     * 指定文字列数で以降の文字列を削除し、代わりに…で埋める。
     * 
     * @param string
     *            文字列
     * @param count
     *            文字数
     * @return 変換した文字列
     */
    public static String cut(final String string, final int count) {
        if (!isEmpty(string) && string.length() > count) {
            final String replace = string.substring(0, count);
            if (!string.equals(replace)) {
                return appendBuilder(replace, "…");
            }
        }
        return string;
    }

    /**
     * 文字列の空/nullチェック
     * 
     * @param string
     *            文字列
     * @return true:空/null false:それ以外
     */
    public static boolean isEmpty(final CharSequence string) {
        return TextUtils.isEmpty(string);
    }

    /**
     * カンマ設定 例:999999999->999,999,999
     * 
     * @param number
     *            対象数値
     * @return カンマを設定した文字列
     */
    public static String getCanma(final int number) {
        return String.format(Locale.getDefault(), "%1$,3d", number);
    }

    /**
     * 改行文字をsplitした配列を返却する
     * 
     * @param string
     *            対象数値
     * @return カンマを設定した文字列
     */
    public static String[] splitReturn(final String string) {
        return string.split("\r?\n");
    }

    /**
     * 文字列の配列を展開します。
     * 
     * @param delimiter
     *            連結文字。
     * @param strings
     *            連結する文字列配列。
     * @return 連結済みの文字列。
     */
    public static String explode(final String delimiter, final String[] strings) {
        return explode(delimiter, Arrays.asList(strings));
    }

    /**
     * 文字列のコレクションを展開します。
     * 
     * @param delimiter
     *            連結文字。
     * @param strings
     *            連結する文字列一覧。
     * @return 連結済みの文字列。
     */
    public static String explode(final String delimiter, final Collection<String> strings) {
        if (strings == null) {
            return null;
        }
        final int size = strings.size();
        int index = 0;
        final StringBuilder sb = new StringBuilder();

        for (final String str : strings) {
            sb.append(str);
            if (index < size) {
                sb.append(delimiter);
            }
            ++index;
        }
        return sb.toString();
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it
     * takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it
     * takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toLowerCase
     *            {@code true} converts to lowercase, {@code false} to uppercase
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it
     * takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    public static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal
     * values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two
     * characters to represent any given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.4
     */
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    /**
     * Converts an array of characters representing hexidecimal values into an
     * array of bytes of those same values. The
     * returned array will be half the length of the passed array, as it takes
     * two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number
     * of elements.
     * 
     * @param data
     *            An array of characters containing hexidecimal digits
     * @return A byte array containing binary data decoded from the supplied
     *         char array.
     */
    public static byte[] decodeHex(final char[] data) {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     * 
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source
     * @return An integer
     * @throws DecoderException
     *             Thrown if ch is an illegal hex character
     */
    public static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * 数値チェック(少数も含む)
     * 
     * @param str
     *            数値文字列
     * @return true:数値、false：数値でない
     */
    public static boolean isNumber(final String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    /**
     * 文字列比較
     * <p>
     * text1,text2がnullであってもNullPointerExceptionで落ちずに比較可能なメソッド
     * </p>
     * 
     * @param text1
     *            文字列1
     * @param text2
     *            文字列2
     * @return true:一致している<br>
     *         false:一致していない
     */
    public static boolean equals(final String text1, final String text2) {
        if (text1 == null) {
            if (text2 == null) {
                return true;
            }
        } else {
            return text1.equals(text2);
        }
        return false;
    }

    /**
     * 文字列連結処理(StringBuilder版)
     * 
     * @param texts
     *            連結対象文字列
     * @return 連結した文字列
     */
    public static String appendBuilder(final String... texts) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String text : texts) {
            if (text != null) {
                stringBuilder.append(text);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 文字列連結処理(StringBuilder版)
     * 
     * @param src
     *            連結元
     * @param texts
     *            連結対象文字列
     */
    public static void appendBuilder(final StringBuilder src, final String... texts) {
        for (final String text : texts) {
            if (text != null) {
                src.append(text);
            }
        }
    }

    /**
     * 文字列連結処理(StringBuffer版)
     * 
     * @param texts
     *            テキストリスト
     * @return 連結した文字列
     */
    public static String appendBuffer(final String... texts) {
        final StringBuffer stringBuffer = new StringBuffer();
        for (final String text : texts) {
            if (text != null) {
                stringBuffer.append(text);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 文字列連結処理(StringBuffer版)
     * 
     * @param src
     *            連結元
     * @param texts
     *            連結対象文字列
     */
    public static void appendBuffer(final StringBuffer src, final String... texts) {
        for (final String text : texts) {
            if (text != null) {
                src.append(text);
            }
        }
    }

    /**
     * 文字列連結処理+String.format(StringBuffer版)
     * 
     * @param src
     *            連結元
     * @param format
     *            フォーマット
     * @param args
     *            フォーマットに適用する変数
     */
    public static void appendBufferFormat(final StringBuffer src, final String format, final Object... args) {
        if (format != null) {
            src.append(String.format(format, args));
        }
    }

    /**
     * 文字列連結処理+String.format(StringBuffer版)
     * 
     * @param src
     *            連結元
     * @param format
     *            フォーマット
     * @param args
     *            フォーマットに適用する変数
     */
    public static void appendBuilderFormat(final StringBuilder src, final String format, final Object... args) {
        if (format != null) {
            src.append(String.format(format, args));
        }
    }

    /**
     * 文字列の指定区切りをArrayListに変換する
     * 
     * @param text
     *            文字列
     * @param split
     *            区切り文字
     * @return ArrayList
     */
    public static ArrayList<String> toArray(final String text, final String split) {
        final ArrayList<String> results = new ArrayList<String>();
        if (!StringUtils.isEmpty(text)) {
            if (StringUtils.contains(text, split)) {
                final String[] splitString = text.split(split);
                // 配列をListに変換
                for (final String data : splitString) {
                    results.add(data);
                }
            } else {
                results.add(text);
            }
        }
        return results;
    }

    /**
     * startWithメソッド呼ぶ
     * 
     * @param text
     *            文字列
     * @param prefix
     *            the string to look for.
     * @return {@code true} if the specified string is a prefix of this string, {@code false} otherwise
     */
    public static boolean startsWith(final String text, final String prefix) {
        if (text == null) {
            return false;
        }
        return text.startsWith(prefix);
    }

    /**
     * 文字列が含まれているかチェックします。
     * 
     * @param str
     *            対象文字列
     * @param containStr
     *            含まれているかチェックする文字列
     * @return true:含まれている/false:含まれていない
     */
    public static boolean contains(final String str, final String containStr) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.contains(containStr);
    }

    /**
     * ブランクかどうか返します。
     * 
     * @param str
     *            文字列
     * @return ブランクかどうか
     */
    public static boolean isBlank(final CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
