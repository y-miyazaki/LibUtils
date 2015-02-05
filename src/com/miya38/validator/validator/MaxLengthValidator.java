package com.miya38.validator.validator;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class MaxLengthValidator extends AbstractValidator {
    private final String mErrorMessage;
    private final int mMaxLength;

    public MaxLengthValidator(final Context c, final int errorMessage, final int maxLenth) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
        mMaxLength = maxLenth;
    }

    public MaxLengthValidator(final Context c, final String errorMessage, final int maxLenth) {
        super(c);
        mErrorMessage = errorMessage;
        mMaxLength = maxLenth;
    }

    @Override
    public boolean isValid(final String value) {
        if (value != null) {
            if (value.length() > mMaxLength) {
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
