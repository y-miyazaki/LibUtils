package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SearchView;

/**
 * カスタムSearchView
 * 
 * @author y-miyazaki
 */
public class CustomSearchView extends SearchView {

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomSearchView(final Context context) {
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
    public CustomSearchView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
}
