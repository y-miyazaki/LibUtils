package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;

/**
 * カスタムListView
 * <p>
 * PullToRefreshGridViewを継承したクラス
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
@SuppressWarnings("deprecation")
public class CustomGallery extends Gallery {
    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomGallery(final Context context) {
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
    public CustomGallery(final Context context, final AttributeSet attrs) {
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
    public CustomGallery(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }
}
