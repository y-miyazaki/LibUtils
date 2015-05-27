package com.miya38.utils.guava;

import android.support.annotation.Nullable;

public final class Preconditions {
    /**
     * プライベートコンストラクタ
     */
    private Preconditions() {

    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     * 
     * @param reference
     *            an object reference
     * @param errorMessage
     *            the exception message to use if the check fails; will be
     *            converted to a string using String.valueOf(Object)
     * 
     * @return the non-null reference that was validated
     * @throws NullPointerException
     *             if reference is null
     */
    public static <T> T checkNotNull(final T reference, @Nullable final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

}
