package com.miya38.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;

import com.miya38.validator.AbstractValidator;
import com.miya38.validator.ValidatorException;

/**
 * This validator test value with custom Regex Pattern.
 */
public class RegExpValidator extends AbstractValidator {

    private Pattern mPattern;

    private final String mErrorMessage;

    public RegExpValidator(final Context c, final int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public RegExpValidator(final Context c, final String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    public void setPattern(final String pattern) {
        mPattern = Pattern.compile(pattern);
    }

    public void setPattern(final Pattern pattern) {
        mPattern = pattern;
    }

    @Override
    public boolean isValid(final String value) throws ValidatorException {
        if (mPattern != null) {
            return mPattern.matcher(value).matches();
        } else {
            throw new ValidatorException("You can set Regexp Pattern first");
        }
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }
}
