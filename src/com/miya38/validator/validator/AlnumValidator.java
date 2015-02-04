package com.miya38.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

/**
 * Validator to check if a field contains only numbers and letters. Avoids having special characters like accents.
 */
public class AlnumValidator extends AbstractValidator {

    /**
     * This si Alnum Pattern to verify value.
     */
    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    private String mErrorMessage;

    /**
     * contructor
     *
     * @param c
     * @param errorMessage
     */
    public AlnumValidator(Context c, int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    /**
     * contructor
     *
     * @param c
     * @param errorMessage
     */
    public AlnumValidator(Context c, String errorMessage) {
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
