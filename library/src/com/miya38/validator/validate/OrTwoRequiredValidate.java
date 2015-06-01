package com.miya38.validator.validate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.miya38.validator.AbstractValidate;
import com.miya38.validator.AbstractValidator;
import com.miya38.validator.Validate;
import com.miya38.validator.validator.NotEmptyValidator;

/**
 * Validator class to validate if the fields are empty fields of 2 or not. If
 * one of them is null, no error. If both are
 * nulls: Error
 * 
 * @author throrin19
 * 
 */
public class OrTwoRequiredValidate extends AbstractValidate {

    private final TextView mField1;
    private final TextView mField2;
    private final Context mContext;
    private String mErrorMessage;
    private final TextView mSource;
    private String mErrorMessage2;

    public OrTwoRequiredValidate(final TextView field1, final TextView field2) {
        this.mField1 = field1;
        this.mField2 = field2;
        mSource = mField1;
        mContext = field1.getContext();
    }

    public OrTwoRequiredValidate(final TextView field1, final TextView field2, final int errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        mSource = mField1;
        mContext = field1.getContext();
        mErrorMessage = mContext.getString(errorMessage);
    }

    public OrTwoRequiredValidate(final TextView field1, final TextView field2, final String errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        mSource = mField1;
        mContext = field1.getContext();
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(final String value) {
        final Validate field1Validator = new Validate(mField1);
        field1Validator.addValidator(new NotEmptyValidator(mContext, mErrorMessage));

        final Validate field2Validator = new Validate(mField2);
        field2Validator.addValidator(new NotEmptyValidator(mContext, mErrorMessage));

        if (field1Validator.isValid(mField1.getText().toString()) || field2Validator.isValid(mField2.getText().toString())) {
            return true;
        } else {
            mErrorMessage2 = field1Validator.getMessage();
            return false;
        }
    }

    @Override
    public String getMessage() {
        return mErrorMessage2;
    }

    @Override
    public List<String> getMessages() {
        final List<String> result = new ArrayList<String>();
        result.add(mErrorMessage);
        return result;
    }

    @Override
    public void addValidator(final AbstractValidator validator) {
    }

    @Override
    public TextView getSource() {
        return mSource;
    }
}
