package com.miya38.widget;

import com.miya38.widget.callback.OnLayoutListener;

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
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomHorizontalScrollView(final Context context, final AttributeSet attrs) {
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
    public CustomHorizontalScrollView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
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
    public final void setOnLayoutListener(final OnLayoutListener l) {
        mOnLayoutListener = l;
    }
}
