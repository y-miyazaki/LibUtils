package com.miya38.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Form Validation Class
 *
 * Immediately, only works with EditText
 *
 * @author throrin19
 *
 * @version 1.0
 *
 */
public class Form {
    /** Validation */
    protected List<AbstractValidate> mValidates = new ArrayList<AbstractValidate>();

    /**
     * Function adding Validates to our form
     *
     * @param validate
     *            {@link AbstractValidate} Validate to add
     */
    public void addValidates(AbstractValidate validate) {
        this.mValidates.add(validate);
    }

    /**
     * Called to validate our form. If an error is found, it will be displayed in the corresponding field.
     *
     * @return boolean : true if the form is valid false if the form is invalid
     */
    public boolean validate() {
        boolean result = true;
        int validator = 0;
        final Iterator<AbstractValidate> it = this.mValidates.iterator();
        while (it.hasNext()) {
            if (!it.next().isValid()) {
                validator++;
            }
        }
        if (validator > 0) {
            result = false;
        }
        return result;
    }
}
