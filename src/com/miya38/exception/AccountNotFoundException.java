package com.miya38.exception;

/**
 * アカウントが見つからない場合の例外を表します。
 *
 * @author y-miyazaki
 */
public class AccountNotFoundException extends RuntimeException {

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
    public AccountNotFoundException(String message) {
        super(message);
    }

    /**
     * AccountNotFoundException を初期化します。
     *
     * @param e
     *            元の例外。
     */
    public AccountNotFoundException(Throwable e) {
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
    public AccountNotFoundException(String message, Throwable e) {
        super(message, e);
    }

}
