package com.miya38.validator.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.miya38.validator.AbstractValidator;

public class EmailValidator extends AbstractValidator {

    private final String mErrorMessage;

    private String mDomainName = "";

    public EmailValidator(final Context c, final int errorMessage) {
        super(c);
        mErrorMessage = mContext.getString(errorMessage);
    }

    public EmailValidator(final Context c, final String errorMessage) {
        super(c);
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(final String charseq) {
        if (charseq.length() > 0) {
            boolean matchFound = false;

            // Input the string for validation
            final String email = charseq;

            if (mDomainName != null && mDomainName.length() > 0) {
                // Test avec le domaine

                // Set the email pattern string
                final Pattern p = Pattern.compile(".+@" + mDomainName);
                // Match the given string with the pattern
                final Matcher m = p.matcher(email);
                // check whether match is found
                matchFound = m.matches();

                if (matchFound) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // test sans le domaine

                // Set the email pattern string
                final Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                // Match the given string with the pattern
                final Matcher m = p.matcher(email);
                // check whether match is found
                matchFound = m.matches();
            }

            if (matchFound) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }

    /**
     * Lets say that the email address must be valid for such domain. This
     * function only accepts strings of Regexp
     * 
     * @param name
     *            Regexp Domain Name
     * 
     *            example : gmail.com
     */
    public void setDomainName(final String name) {
        mDomainName = name;
    }
}
