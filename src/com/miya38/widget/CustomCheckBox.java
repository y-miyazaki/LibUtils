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
    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomCheckBox(final Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomCheckBox(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     * @param defStyle
     *            Default style for this View
     */
    public CustomCheckBox(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
}
