package com.miya38.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * ユーティリティクラス
 */
public final class LogUtils {
    /** デバッグ状態であるか？ */
    private static boolean sIsDebuggable;
    /** logcatのための文字列分割数 */
    private static final int CUT_BYTES = 3000;
    /** ログレベル */
    private static final int LOG_LEVEL_DEBUG = 1;
    /** ログレベル */
    private static final int LOG_LEVEL_INFO = 2;
    /** ログレベル */
    private static final int LOG_LEVEL_VERBOSE = 3;
    /** ログレベル */
    private static final int LOG_LEVEL_WARNING = 4;
    /** ログレベル */
    private static final int LOG_LEVEL_ERROR = 5;
    /** ログレベル */
    private static final int LOG_LEVEL_WTF = 6;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private LogUtils() {
    }

    /**
     * ログユーティリティを初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param isDebugable
     *            debug有無<br>
     *            true:デバッグモード false:非デバッグモード
     */
    public static void configure(final boolean isDebugable) {
        sIsDebuggable = isDebugable;
    }

    /**
     * ログ出力(debug)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void d(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_DEBUG, tag, message, null, params);
    }

    /**
     * ログ出力(debug)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void d(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_DEBUG, tag, message, e, params);
    }

    /**
     * ログ出力(info)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void i(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_INFO, tag, message, null, params);
    }

    /**
     * ログ出力(info)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void i(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_INFO, tag, message, e, params);
    }

    /**
     * ログ出力(error)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void e(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_ERROR, tag, message, null, params);
    }

    /**
     * ログ出力(error)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void e(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_ERROR, tag, message, e, params);
    }

    /**
     * ログ出力(verbose)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void v(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_VERBOSE, tag, message, null, params);
    }

    /**
     * ログ出力(verbose)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void v(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_VERBOSE, tag, message, e, params);
    }

    /**
     * ログ出力(warning)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void w(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_WARNING, tag, message, null, params);
    }

    /**
     * ログ出力(warning)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void w(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_WARNING, tag, message, e, params);
    }

    /**
     * ログ出力(what a terrible error)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void wtf(final String tag, final String message, final Object... params) {
        init(LOG_LEVEL_WTF, tag, message, null, params);
    }

    /**
     * ログ出力(warning)
     *
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    public static void wtf(final String tag, final String message, final Throwable e, final Object... params) {
        init(LOG_LEVEL_WTF, tag, message, e, params);
    }

    /**
     * 初期処理
     *
     * @param logLevel
     *            ログレベル
     * @param tag
     *            タグ
     * @param message
     *            ログ出力内容(String.formatで指定される%s,%d等が使用可能)
     * @param e
     *            Exception ログ
     * @param params
     *            ログ出力用の変数(String.formatと同じように複数の変数が設定可能)
     */
    private static void init(final int logLevel, final String tag, final String message, final Throwable e, final Object... params) {
        if (sIsDebuggable) {
            // カウンタ
            int count = 1;
            // ログを取得
            final String log = 0 < params.length ? String.format(message, params) : message;
            // 文字列カットリスト取得
            final List<String> lines = cut(log, CUT_BYTES);
            final int size = lines.size();

            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    switch (logLevel) {
                    case LOG_LEVEL_DEBUG:
                        if ((i + 1) != size || e == null) {
                            Log.d(tag + ":" + count, lines.get(i));
                        } else {
                            Log.d(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    case LOG_LEVEL_INFO:
                        if ((i + 1) != size || e == null) {
                            Log.i(tag + ":" + count, lines.get(i));
                        } else {
                            Log.i(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    case LOG_LEVEL_VERBOSE:
                        if ((i + 1) != size || e == null) {
                            Log.v(tag + ":" + count, lines.get(i));
                        } else {
                            Log.v(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    case LOG_LEVEL_WARNING:
                        if ((i + 1) != size || e == null) {
                            Log.w(tag + ":" + count, lines.get(i));
                        } else {
                            Log.w(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    case LOG_LEVEL_ERROR:
                        if ((i + 1) != size || e == null) {
                            Log.e(tag + ":" + count, lines.get(i));
                        } else {
                            Log.e(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    case LOG_LEVEL_WTF:
                        if ((i + 1) != size || e == null) {
                            Log.wtf(tag + ":" + count, lines.get(i));
                        } else {
                            Log.wtf(tag + ":" + count, lines.get(i), e);
                        }
                        break;
                    default:
                        break;
                    }
                    count++;
                }
            } else {
                switch (logLevel) {
                case LOG_LEVEL_DEBUG:
                    if (e == null) {
                        Log.d(tag, log);
                    } else {
                        Log.d(tag, log, e);
                    }
                    break;
                case LOG_LEVEL_INFO:
                    if (e == null) {
                        Log.i(tag, log);
                    } else {
                        Log.i(tag, log, e);
                    }
                    break;
                case LOG_LEVEL_VERBOSE:
                    if (e == null) {
                        Log.v(tag, log);
                    } else {
                        Log.v(tag, log, e);
                    }
                    break;
                case LOG_LEVEL_WARNING:
                    if (e == null) {
                        Log.w(tag, log);
                    } else {
                        Log.w(tag, log, e);
                    }
                    break;
                case LOG_LEVEL_ERROR:
                    if (e == null) {
                        Log.e(tag, log);
                    } else {
                        Log.e(tag, log, e);
                    }
                    break;
                case LOG_LEVEL_WTF:
                    if (e == null) {
                        Log.wtf(tag, log);
                    } else {
                        Log.wtf(tag, log, e);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    /**
     * カット処理
     *
     * @param target
     *            ターゲットの文字列
     * @param cutBytes
     *            文字列分割単位バイト数
     * @return 分割された文字列のリスト
     */
    private static List<String> cut(final String target, final int cutBytes) {
        final List<String> returnString = new ArrayList<String>();
        if (target == null || cutBytes <= 0 || target.length() <= cutBytes) {
            returnString.add(target);
        } else {
            final int length = target.length();

            // カット数算出
            int cutnum = length / cutBytes;
            if (length % cutBytes > 0) {
                cutnum++;
            }

            for (int i = 0; i < cutnum; i++) {
                if (((i + 1) * cutBytes) <= length) {
                    returnString.add(target.substring(i * cutBytes, i * cutBytes + cutBytes));
                } else {
                    returnString.add(target.substring(i * cutBytes, i * cutBytes + (length - i * cutBytes) - 1));
                }
            }
        }
        return returnString;
    }
}
