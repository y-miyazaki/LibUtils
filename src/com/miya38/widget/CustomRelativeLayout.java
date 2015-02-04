package com.miya38.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.miya38.R;
import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;

/**
 * カスタムRelativeLayout
 * <ul>
 * <li>背景自動ダウンロード</li>
 * <li>setDispatchPressedを拒絶する機能</li>
 * </ul>
 *
 * @author y-miyazaki
 */
public class CustomRelativeLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomLinearLayout.class.getSimpleName();
    /** フリック検知移動距離 */
    private static final int FLICK_X = 130;

    // ----------------------------------------------------------
    // swipe
    // ----------------------------------------------------------
    /** GestureDetector */
    private GestureDetector mGestureDetector;
    /** フリックリスナーインタフェース */
    private OnFlickListener mOnFlickListener;

    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** クリックリスナー */
    private OnLayoutListener mOnLayoutListener;
    /** 背景のリピート設定 */
    private boolean mIsRepeat;
    /** プレスイベントを子Viewに投げるか？ */
    private boolean mIsDispatchSetPressed = true;
    /** OnGlobalLayoutListener2 */
    private OnGlobalLayoutListener2 mOnGlobalLayoutListener2;
    private OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mOnGlobalLayoutListener2 != null) {
                mOnGlobalLayoutListener2.onGlobalLayout();
            }
        }
    };

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomRelativeLayout(Context context) {
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
    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGestureDetector(new GestureDetector(context, this));

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout);
        this.mIsRepeat = ta.getBoolean(R.styleable.CustomLinearLayout_repeat, false);
        this.mIsDispatchSetPressed = ta.getBoolean(R.styleable.CustomLinearLayout_dispatch_set_pressed, true);

        if (mIsRepeat) {
            ViewHelper.fixBackgroundRepeat(this);
        }
        ta.recycle();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mIsRepeat) {
            ViewHelper.fixBackgroundRepeat(this);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        if (mIsRepeat) {
            ViewHelper.fixBackgroundRepeat(this);
        }
    }

    /*
     * (非 Javadoc)
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        LogUtils.d(TAG, "onDetachedFromWindow");
        setBackgroundDrawable(null);
        setOnClickListener(null);
        setOnLayoutListener(null);
        removeOnGlobalLayoutListener(mOnGlobalLayoutListener);

        super.onDetachedFromWindow();
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        if (mIsDispatchSetPressed) {
            super.dispatchSetPressed(pressed);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mOnLayoutListener != null) {
            mOnLayoutListener.onLayout();
        }
    }

    /**
     * 自動的にRelativeLayout内にあるコンテンツをつめて表示する。
     * <p>
     * <ul>
     * <li>自動的に左詰めで右までいったら自動的に改行を行う。</li>
     * <li>onWindowFocusChangedの後に呼ぶこと。(Viewが確定しないと幅等が取得できない。</li>
     * </p>
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public boolean setAutoFitView(int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return false;
        }
        final int maxWidth = getWidth();
        // 一番最初は基点となるので何もしない
        View pline = getChildAt(0);
        final RelativeLayout.LayoutParams prmRoot = (RelativeLayout.LayoutParams) pline.getLayoutParams();
        prmRoot.setMargins(left, top, right, bottom);

        // margin取得
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pline.getLayoutParams();
        final int margin = layoutParams.leftMargin + layoutParams.rightMargin;
        // 現在の幅
        int total = pline.getWidth() + margin;

        final int count = getChildCount();
        for (int i = 1; i < count; i++) {
            int width = getChildAt(i).getWidth();
            if (width == 0) {
                return false;
            }
            final int wm = width + margin;
            final RelativeLayout.LayoutParams prm = (RelativeLayout.LayoutParams) getChildAt(i).getLayoutParams();
            // 横幅を超えないなら前のボタンの右に出す
            if (maxWidth > total + wm) {
                total += wm;
                prm.addRule(RelativeLayout.ALIGN_TOP, getChildAt(i - 1).getId());
                prm.addRule(RelativeLayout.RIGHT_OF, getChildAt(i - 1).getId());
                prm.setMargins(left, top, right, bottom);
            }
            // 超えたら下に出す
            else {
                prm.addRule(RelativeLayout.BELOW, pline.getId());
                prm.setMargins(left, top, right, bottom);
                // 基点を変更
                pline = getChildAt(i);
                // 長さをリセット
                total = pline.getWidth();
            }
        }
        return true;
    }

    /**
     * 自動的にRelativeLayout内にあるコンテンツをつめて表示する。
     * <p>
     * <ul>
     * <li>自動的に左詰めで右までいったら自動的に改行を行う。</li>
     * <li>onWindowFocusChangedの後に呼ぶこと。(Viewが確定しないと幅等が取得できない。</li>
     * </p>
     *
     * @return
     */
    public boolean setAutoFitView() {
        return setAutoFitView(0, 0, 0, 0);
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

    /**
     * グローバルレイアウトリスナー設定
     *
     * @param l
     *            {@link OnGlobalLayoutListener2}
     */
    public final void setOnGlobalLayoutListener2(OnGlobalLayoutListener2 l) {
        // リスナー複数登録回避
        ViewTreeObserver observer = getViewTreeObserver();
        removeOnGlobalLayoutListener(mOnGlobalLayoutListener);

        mOnGlobalLayoutListener2 = l;
        observer.addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    /**
     * グローバルリスナー削除
     *
     * @param l
     *            {@link OnGlobalLayoutListener}
     */
    @SuppressWarnings("deprecation")
    private void removeOnGlobalLayoutListener(OnGlobalLayoutListener l) {
        ViewTreeObserver observer = getViewTreeObserver();
        if (AplUtils.hasJellyBean()) {
            observer.removeOnGlobalLayoutListener(l);
        } else {
            observer.removeGlobalOnLayoutListener(l);
        }
        mOnGlobalLayoutListener2 = null;
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
}
