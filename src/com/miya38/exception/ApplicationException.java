package com.miya38.exception;

import com.miya38.utils.LogUtils;

/**
 * アプリケーション例外
 *
 * @author y-miyazaki
 */
public class ApplicationException extends RuntimeException {
    /**
     * コンストラクタ
     *
     * @param message
     *            エラーメッセージ
     * @param cause
     *            起因例外
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        // スタックトレースを文字列にします。
        LogUtils.e("error", message, cause);
    }

    /**
     * コンストラクタ
     *
     * @param message
     *            起因例外
     */
    public ApplicationException(String message) {
        super(message);
        LogUtils.e("error", message);
    }

    /**
     * コンストラクタ
     *
     * @param cause
     *            起因例外
     */
    public ApplicationException(Throwable cause) {
        super(cause);
        LogUtils.e("error", "", cause);
    }
}
