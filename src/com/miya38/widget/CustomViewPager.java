package com.miya38.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

/**
 * カスタムViewPagerクラス
 * 
 * @author y-miyazaki
 */
public class CustomViewPager extends ViewPager {
    /** ScrollerCustomDuration */
    private ScrollerCustomDuration mScroller;
    /** ページング制御状態 */
    private boolean mIsPagingEnabled = true;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomViewPager(final Context context) {
        super(context);
        postInitViewPager();
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
    public CustomViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    @Override
    protected boolean canScroll(final View v, final boolean checkV, final int dx, final int x, final int y) {
        return super.canScroll(v, checkV, dx, x, y) || checkV && customCanScroll(v);
    }

    /**
     * フリックでのページングを制御する
     * 
     * @param isEnabled
     *            true:ページング許可/false:ページング不可
     */
    public void setPagingEnabled(boolean isEnabled) {
        this.mIsPagingEnabled = isEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsPagingEnabled ? super.onInterceptTouchEvent(event) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsPagingEnabled ? super.onTouchEvent(event) : false;
    }

    /**
     * ViewPagerの中にあるHorizontalScrollViewを優先する設定
     * 
     * @param v
     *            View
     * @return
     *         true:HorizontalScrollViewが画面より広い場合は、
     *         HorizontalScrollViewを優先してスクロールする。
     *         false:HorizontalScrollViewが画面より狭い場合は、ViewPagerを優先してスクロールする。
     */
    protected boolean customCanScroll(final View v) {
        if (v instanceof HorizontalScrollView) {
            final View hsvChild = ((HorizontalScrollView) v).getChildAt(0);
            if (hsvChild.getWidth() > v.getWidth()) {
                return true;
            }
        }
        return false;
    }

    /**
     * スクロールイベントを取得する。
     */
    private void postInitViewPager() {
        try {
            final Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            final Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (final Exception e) {
            // 握りつぶす
        }
    }

    /**
     * スクロールスピード設定
     * 
     * @param scrollFactor
     *            1:Default、2:1/2のスピード(※数値が大きければスクロールが遅くなる)
     */
    public void setScrollDurationFactor(final double scrollFactor) {
        if (mScroller != null) {
            mScroller.setScrollDurationFactor(scrollFactor);
        }
    }

    /**
     * カスタムスクロールスピード調整クラス
     * 
     * @author y-miyazaki
     * 
     */
    private class ScrollerCustomDuration extends Scroller {
        /** Scroll delay */
        private double mScrollFactor = 1;

        /**
         * コンストラクタ
         * 
         * @param context
         *            Context
         */
        public ScrollerCustomDuration(final Context context) {
            super(context);
        }

        /**
         * コンストラクタ
         * 
         * @param context
         *            Context
         * @param interpolator
         *            {@link Interpolator}
         */
        public ScrollerCustomDuration(final Context context, final Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * コンストラクタ
         * 
         * @param context
         *            Context
         * @param interpolator
         *            {@link Interpolator}
         * @param flywheel
         *            flywheel
         */
        public ScrollerCustomDuration(final Context context, final Interpolator interpolator, final boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Set the factor by which the duration will change
         * 
         * @param scrollFactor
         *            スクロールファクター
         */
        public void setScrollDurationFactor(final double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(final int startX, final int startY, final int dx, final int dy, final int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }
    }
}
