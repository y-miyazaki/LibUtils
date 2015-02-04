package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * カスタムHorizontalScrollView
 * <ul>
 * <li>レイアウト確定イベントリスナー追加</li>
 * </ul>
 *
 * @author y-miyazaki
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** クリックリスナー */
    private OnLayoutListener mOnLayoutListener;

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     * @param defStyle
     *            Default style for this View
     */
    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mOnLayoutListener != null) {
            mOnLayoutListener.onLayout();
        }
    }

    /**
     * レイアウトリスナー設定
     *
     * @param l
     *            {@link OnLayoutListener}
     */
    public final void setOnLayoutListener(OnLayoutListener l) {
        mOnLayoutListener = l;
    }
}
