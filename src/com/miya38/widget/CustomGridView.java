package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * カスタムListView
 * <p>
 * PullToRefreshGridViewを継承したクラス
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public class CustomGridView extends PullToRefreshGridView {
    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomGridView(final Context context) {
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
    public CustomGridView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     * @param mode
     *            Mode
     */
    public CustomGridView(final Context context, final Mode mode) {
        super(context, mode);
    }

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     * @param mode
     *            Mode
     * @param style
     *            アニメーションスタイル
     */
    public CustomGridView(final Context context, final Mode mode, final AnimationStyle style) {
        super(context, mode, style);
    }
}
