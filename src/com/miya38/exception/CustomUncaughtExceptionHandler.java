package com.miya38.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.miya38.utils.SharedPreferencesUtils;

/**
 *
 * 非キャッチ例外取得用ExceptionHandler
 *
 * @author y-miyazaki
 *
 */
public class CustomUncaughtExceptionHandler implements UncaughtExceptionHandler {
    /** stackTrace key*/

    /** UncaughtExceptionHandler */
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    /**
     * コンストラクタ
     */
    public CustomUncaughtExceptionHandler() {
        // デフォルト例外ハンドラを保持する。
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // スタックトレースを文字列にします。
        final StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        final String stackTrace = stringWriter.toString();

        // スタックトレースを SharedPreferences に保存します。
        SharedPreferencesUtils.putString("stackTrace", stackTrace);
        // デフォルト例外ハンドラを実行し、強制終了します。
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }
}
