package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * カスタムListView
 * <p>
 * PullToRefreshListViewを継承したクラス
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public class CustomListView extends PullToRefreshListView {
    /** overScrollする値. */
    private int mOverScrollDistance = 80;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomListView(final Context context) {
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
    public CustomListView(final Context context, final AttributeSet attrs) {
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
    public CustomListView(final Context context, final Mode mode) {
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
    public CustomListView(final Context context, final Mode mode, final AnimationStyle style) {
        super(context, mode, style);
    }

    /**
     * OverScrollする値を設定します。(外部クラスから変更可能にする)
     * 
     * @param overScrollDistance
     *            OverScrollする値
     * @since 1.0.0
     */
    public void setOverScrollDistance(final int overScrollDistance) {
        this.mOverScrollDistance = overScrollDistance;
    }

    @Override
    protected boolean overScrollBy(final int deltaX, final int deltaY, final int scrollX, final int scrollY, final int scrollRangeX, final int scrollRangeY, final int maxOverScrollX, final int maxOverScrollY, final boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mOverScrollDistance, isTouchEvent);
    }
}
