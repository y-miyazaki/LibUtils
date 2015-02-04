package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * カスタムチェックボックスクラス
 *
 * @author y-miyazaki
 */
public class CustomCheckBox extends CheckBox {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomCheckBox.class.getSimpleName();

    public CustomCheckBox(Context context) {
        super(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
