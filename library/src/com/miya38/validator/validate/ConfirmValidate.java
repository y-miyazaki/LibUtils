package com.miya38.validator.validate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.miya38.validator.AbstractValidate;
import com.miya38.validator.AbstractValidator;

public class ConfirmValidate extends AbstractValidate {

    private final TextView mField1;
    private final TextView mField2;
    private final Context mContext;
    private final TextView source;
    private String mErrorMessage;

    public ConfirmValidate(final TextView field1, final TextView field2) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
    }

    public ConfirmValidate(final TextView field1, final TextView field2, final int errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
        mErrorMessage = mContext.getString(errorMessage);
    }

    public ConfirmValidate(final TextView field1, final TextView field2, final String errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(final String value) {
        if (mField1.getText().toString().length() > 0 && mField1.getText().toString().equals(mField2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
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
        return source;
    }
}
