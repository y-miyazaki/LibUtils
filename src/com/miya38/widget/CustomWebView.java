package com.miya38.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.miya38.utils.StringUtils;

/**
 * カスタムWebViewクラス
 * <p>
 * フリックイベントによりバックフォーワードイベントを取得することが可能。
 * </p>
 *
 * @author y-miyazaki
 *
 */
public class CustomWebView extends WebView implements GestureDetector.OnGestureListener {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** フリック検知移動距離 */
    private static final int FLICK_X = 130;

    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** GestureDetector */
    private GestureDetector mGestureDetector;
    /** フリックリスナーインタフェース */
    private OnFlickListener mOnFlickListener;

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomWebView(Context context) {
        super(context);
        setGestureDetector(new GestureDetector(context, this));
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGestureDetector(new GestureDetector(context, this));
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
    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGestureDetector(new GestureDetector(context, this));
    }

    @Override
    public String getTitle() {
        String title = super.getTitle();
        // titleタグが未設定の場合about:blankになるのをnullにする対応
        if (StringUtils.equals(title, "about:blank")) {
            title = null;
        }
        return title;
    }

    /**
     * コールバックリスナー設定
     *
     * @param l
     */
    public final void setOnFlickListener(OnFlickListener l) {
        mOnFlickListener = l;
    }

    /**
     * GestureDetector設定
     *
     * @param gestureDetector
     */
    public void setGestureDetector(GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // 何もしない。
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // 何もしない。
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mOnFlickListener != null) {
            // X軸移動の方が長い場合はtrue Y軸移動の方が長い場合はfalse
            final boolean flingX = Math.abs(e2.getX() - e1.getX()) > Math.abs(e2.getY() - e1.getY()) * 4;
            if (flingX) {
                // バック(X座標左→右に移動)且つ、移動距離がYよりXの方が大きい場合
                if (e2.getX() - e1.getX() > FLICK_X) {
                    mOnFlickListener.onBack();
                } else if (e2.getX() - e1.getX() < -FLICK_X) {
                    mOnFlickListener.onForword();
                }
            }
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        mOnFlickListener = null;
        mGestureDetector = null;
        super.onDetachedFromWindow();
    }
}
