package com.miya38.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SearchView;

/**
 * カスタムSearchView
 * 
 * @author y-miyazaki
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
