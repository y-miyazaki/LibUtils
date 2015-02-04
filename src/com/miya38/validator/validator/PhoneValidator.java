package com.miya38.validator.validator;

import java.util.regex.Pattern;

import android.content.Context;
import android.util.Patterns;

import com.miya38.validator.AbstractValidator;
import com.miya38.validator.ValidatorException;

/**
 * Validator to check if Phone number is correct.
 * Created by throrin19 on 13/06/13.
 */
public class PhoneValidator extends AbstractValidator{

    private static final Pattern PATTERN = Patterns.PHONE;

    private String mErrorMessage;

    public PhoneValidator(Context c, int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public PhoneValidator(Context c, String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(String value) throws ValidatorException {
        return PATTERN.matcher(value).matches();
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }
}
