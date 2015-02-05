package com.miya38.exception;

/**
 * アカウントが見つからない場合の例外を表します。
 * 
 * @author y-miyazaki
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * AccountNotFoundException を初期化します。
     */
    public AccountNotFoundException() {
        super();
    }

    /**
     * AccountNotFoundException を初期化します。
     * 
     * @param message
     *            例外メッセージ。
     */
    public AccountNotFoundException(final String message) {
        super(message);
    }

    /**
     * AccountNotFoundException を初期化します。
     * 
     * @param e
     *            元の例外。
     */
    public AccountNotFoundException(final Throwable e) {
        super(e);
    }

    /**
     * AccountNotFoundException を初期化します。
     * 
     * @param message
     *            例外メッセージ。
     * @param e
     *            元の例外。
     */
    public AccountNotFoundException(final String message, final Throwable e) {
        super(message, e);
    }

}
