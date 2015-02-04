package com.miya38.validator.validate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.miya38.validator.AbstractValidate;
import com.miya38.validator.AbstractValidator;

public class ConfirmValidate extends AbstractValidate {

    private TextView mField1;
    private TextView mField2;
    private Context mContext;
    private TextView source;
    private String mErrorMessage;

    public ConfirmValidate(TextView field1, TextView field2) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
    }

    public ConfirmValidate(TextView field1, TextView field2, int errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
        mErrorMessage = mContext.getString(errorMessage);
    }

    public ConfirmValidate(TextView field1, TextView field2, String errorMessage) {
        this.mField1 = field1;
        this.mField2 = field2;
        source = mField2;
        mContext = field1.getContext();
        mErrorMessage = errorMessage;
    }

    @Override
    public boolean isValid(String value) {
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
        List<String> result = new ArrayList<String>();
        result.add(mErrorMessage);
        return result;
    }

    @Override
    public void addValidator(AbstractValidator validator) {
    }

    @Override
    public TextView getSource() {
        return source;
    }
}
