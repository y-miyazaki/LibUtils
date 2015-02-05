package com.miya38.validator.validator;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class MinLengthValidator extends AbstractValidator {
    private final String mErrorMessage;
    private final int mMinLength;

    public MinLengthValidator(final Context c, final int errorMessage, final int minLenth) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
        mMinLength = minLenth;
    }

    public MinLengthValidator(final Context c, final String errorMessage, final int minLenth) {
        super(c);
        mErrorMessage = errorMessage;
        mMinLength = minLenth;
    }

    @Override
    public boolean isValid(final String value) {
        if (value != null) {
            if (value.length() < mMinLength) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }
}
