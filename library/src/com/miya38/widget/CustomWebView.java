package com.miya38.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.miya38.utils.StringUtils;
import com.miya38.widget.callback.OnFlickListener;

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
    /** スクロール変更リスナーインタフェース */
    private OnScrollChangedListener mOnScrollChangedListener;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     */
    public CustomWebView(final Context context) {
        super(context);
        setGestureDetector(new GestureDetector(context, this));
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
    public CustomWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setGestureDetector(new GestureDetector(context, this));
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
    public CustomWebView(final Context context, final AttributeSet attrs, final int defStyle) {
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
     *            {@link OnFlickListener}
     */
    public final void setOnFlickListener(final OnFlickListener l) {
        mOnFlickListener = l;
    }

    /**
     * GestureDetector設定
     * 
     * @param gestureDetector
     *            {@link GestureDetector}
     */
    public void setGestureDetector(final GestureDetector gestureDetector) {
        this.mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(final MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(final MotionEvent e) {
        // 何もしない。
    }

    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(final MotionEvent e) {
        // 何もしない。
    }

    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
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
        mOnScrollChangedListener = null;
        setWebChromeClient(null);
        setWebViewClient(null);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    /**
     * スマートフォンにおけるデフォルト設定を行うメソッド
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setSmartPhoneDefaultSetting() {
        //スクロールバーの隙間を空かないようにする。
        setVerticalScrollbarOverlay(true);

        // webViewの設定
        final WebSettings settings = getSettings();
        // マルチウィンドウモードOFF
        settings.setSupportMultipleWindows(false);
        settings.setLoadsImagesAutomatically(true);
        // 読み込み時にページ横幅を画面幅に合わせる
        settings.setLoadWithOverviewMode(true);
        // ワイドビューポートの有効・無効の指定(trueならページ全体表示)
        settings.setUseWideViewPort(false);
        // JS利用
        settings.setJavaScriptEnabled(true);
        // キャッシュ設定
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    /**
     * スクロール変更リスナー設定
     * 
     * @param l
     *            {@link OnScrollChangedListener}
     */
    public void setOnScrollChangedListener(OnScrollChangedListener l) {
        mOnScrollChangedListener = l;
    }

    /**
     * スクロール変更リスナーインタフェース
     * X/Y座標を返却する。
     * 
     * @author y-miyazaki
     */
    public interface OnScrollChangedListener {
        /**
         * This is called in response to an internal scroll in this view (i.e., the view scrolled its own contents). This is typically as a result of scrollBy(int, int) or scrollTo(int, int) having been called.
         * 
         * @param l
         *            Current horizontal scroll origin.
         * @param t
         *            Current vertical scroll origin.
         * @param oldl
         *            Previous horizontal scroll origin.
         * @param oldt
         *            Previous vertical scroll origin.
         */
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
