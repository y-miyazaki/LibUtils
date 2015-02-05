package com.miya38.validator;

public class ValidatorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ValidatorException() {
        super();
    }

    /**
     * @param detailMessage
     * @param throwable
     */
    public ValidatorException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * @param detailMessage
     */
    public ValidatorException(final String detailMessage) {
        super(detailMessage);
    }

    /**
     * @param throwable
     */
    public ValidatorException(final Throwable throwable) {
        super(throwable);
    }
}
