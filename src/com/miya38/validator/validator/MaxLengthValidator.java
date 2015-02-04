package com.miya38.validator.validator;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class MaxLengthValidator extends AbstractValidator {
    private String mErrorMessage;
    private int mMaxLength;

    public MaxLengthValidator(Context c, int errorMessage, int maxLenth) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
        mMaxLength = maxLenth;
    }

    public MaxLengthValidator(Context c, String errorMessage, int maxLenth) {
        super(c);
        mErrorMessage = errorMessage;
        mMaxLength = maxLenth;
    }

    @Override
    public boolean isValid(String value) {
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
