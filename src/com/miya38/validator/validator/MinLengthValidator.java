package com.miya38.validator.validator;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class MinLengthValidator extends AbstractValidator {
    private String mErrorMessage;
    private int mMinLength;

    public MinLengthValidator(Context c, int errorMessage, int minLenth) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
        mMinLength = minLenth;
    }

    public MinLengthValidator(Context c, String errorMessage, int minLenth) {
        super(c);
        mErrorMessage = errorMessage;
        mMinLength = minLenth;
    }

    @Override
    public boolean isValid(String value) {
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
