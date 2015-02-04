package com.miya38.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomViewPager(Context context) {
        super(context);
        postInitViewPager();
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return super.canScroll(v, checkV, dx, x, y) || checkV && customCanScroll(v);
    }

    /**
     * ViewPagerの中にあるHorizontalScrollViewを優先する設定
     *
     * @param v
     * @return
     */
    protected boolean customCanScroll(View v) {
        if (v instanceof HorizontalScrollView) {
            final View hsvChild = ((HorizontalScrollView) v).getChildAt(0);
            if (hsvChild.getWidth() > v.getWidth()) {
                return true;
            }
        }
        return false;
    }

    private void postInitViewPager() {
        try {
            final Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            final Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
            // 握りつぶす
        }
    }

    /**
     * スクロールスピード設定
     *
     * @param scrollFactor
     *            1:Default、2:1/2のスピード(※数値が大きければスクロールが遅くなる)
     */
    public void setScrollDurationFactor(double scrollFactor) {
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
         */
        public ScrollerCustomDuration(Context context) {
            super(context);
        }

        /**
         * コンストラクタ
         *
         * @param context
         * @param interpolator
         */
        public ScrollerCustomDuration(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * コンストラクタ
         *
         * @param context
         * @param interpolator
         * @param flywheel
         */
        public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        /**
         * Set the factor by which the duration will change
         *
         * @param scrollFactor
         */
        public void setScrollDurationFactor(double scrollFactor) {
            mScrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
        }
    }
}
