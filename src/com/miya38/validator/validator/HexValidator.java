package com.miya38.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;

import com.miya38.validator.AbstractValidator;


public class HexValidator extends AbstractValidator {

    /**
     * This is Hex Pattern to verify value.
     */
    private static final Pattern PATTERN = Pattern.compile("^(#|)[0-9A-Fa-f]+$");

    private String mErrorMessage;

    public HexValidator(Context c, int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public HexValidator(Context c, String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(String value) {
        return PATTERN.matcher(value).matches();
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }
}
