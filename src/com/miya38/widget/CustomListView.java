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
    public CustomListView(Context context) {
        super(context);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomListView(Context context, AttributeSet attrs) {
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
    public CustomListView(Context context, Mode mode) {
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
    public CustomListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    /**
     * OverScrollする値を設定します。(外部クラスから変更可能にする)
     *
     * @param overScrollDistance
     *            OverScrollする値
     * @since 1.0.0
     */
    public void setOverScrollDistance(int overScrollDistance) {
        this.mOverScrollDistance = overScrollDistance;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mOverScrollDistance, isTouchEvent);
    }
}
