package com.miya38.validator.validator;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class NotEmptyValidator extends AbstractValidator {

    private final String mErrorMessage;

    public NotEmptyValidator(final Context c, final int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public NotEmptyValidator(final Context c, final String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(final String value) {
        if (value != null) {
            if (value.length() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }

}
