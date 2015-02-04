package com.miya38.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;
import android.util.Patterns;

import com.miya38.validator.AbstractValidator;

public class UrlValidator extends AbstractValidator {

    private static Pattern mPattern = Patterns.WEB_URL;

    private String mErrorMessage;

    public UrlValidator(Context c, int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public UrlValidator(Context c, String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(String url) {
        return mPattern.matcher(url).matches();
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }

}
