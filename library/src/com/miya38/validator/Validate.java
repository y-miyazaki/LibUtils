package com.miya38.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.widget.TextView;

public class Validate extends AbstractValidate {
    /** Validator chain */
    protected List<AbstractValidator> mValidators = new ArrayList<AbstractValidator>();
    /** Validation failure messages */
    protected List<String> mMessages;
    /** TextView */
    protected TextView mSource;

    /**
     * Constructor
     * 
     * @param source
     */
    public Validate(final TextView source) {
        this.mSource = source;
    }

    /**
     * Adds a validator to the end of the chain
     * 
     * @param validator
     */
    @Override
    public void addValidator(final AbstractValidator validator) {
        this.mValidators.add(validator);
    }

    @Override
    public boolean isValid(final String value) {
        final Iterator<AbstractValidator> it = this.mValidators.iterator();
        mMessages = new ArrayList<String>();
        while (it.hasNext()) {
            final AbstractValidator validator = it.next();
            try {
                if (!validator.isValid(value)) {
                    mMessages.add(validator.getMessage());
                }
            } catch (final ValidatorException e) {
                mMessages.add(e.getMessage());
            }
        }

        return mMessages.isEmpty();
    }

    @Override
    public String getMessage() {
        return mMessages.get(0);
    }

    @Override
    public List<String> getMessages() {
        return this.mMessages;
    }

    @Override
    public TextView getSource() {
        return this.mSource;
    }
}
