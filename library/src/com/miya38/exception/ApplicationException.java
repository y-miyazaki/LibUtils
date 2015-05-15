package com.miya38.exception;

import com.miya38.utils.LogUtils;

/**
 * アプリケーション例外
 * 
 * @author y-miyazaki
 */
public class ApplicationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     * 
     * @param message
     *            エラーメッセージ
     * @param cause
     *            起因例外
     */
    public ApplicationException(final String message, final Throwable cause) {
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
    public ApplicationException(final String message) {
        super(message);
        LogUtils.e("error", message);
    }

    /**
     * コンストラクタ
     * 
     * @param cause
     *            起因例外
     */
    public ApplicationException(final Throwable cause) {
        super(cause);
        LogUtils.e("error", "", cause);
    }
}
