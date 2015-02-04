package com.miya38.validator;

import java.util.List;

import android.widget.TextView;

public abstract class AbstractValidate {

    /**
     * Add a new validator for fields attached
     *
     * @param validator
     *            {@link AbstractValidator} : The validator to attach
     */
    public abstract void addValidator(AbstractValidator validator);

    /**
     * Function called when the {@link Form} validation
     *
     * @param value
     *            {@link String} : value to validate
     * @return true if all validators are valid false if a validator is invalid
     */
    public abstract boolean isValid(String value);

    /**
     * Returns the error message displayed on the connected component
     *
     * @return {@link String} : the message to display
     */

    /**
     * Set error in getSource
     *
     * @return true if all validators are valid false if a validator is invalid
     */
    public final boolean isValid() {
        final boolean valid = isValid(getSource().getText().toString());
        if (valid) {
            getSource().setError(null);
        } else {
            getSource().setError(getMessage());
        }
        return valid;
    }

    /**
     * エラーメッセージ
     *
     * @return エラーメッセージの1件目のみ返却
     */
    public abstract String getMessage();

    /**
     * エラーメッセージ
     *
     * @return エラーメッセージを全て返却
     */
    public abstract List<String> getMessages();

    /**
     * Function recovering the field attached to our validator
     *
     * @return {@link TextView} : The fields attached
     */
    public abstract TextView getSource();
}
