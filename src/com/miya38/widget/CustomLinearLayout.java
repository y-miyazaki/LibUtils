package com.miya38.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.miya38.R;
import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;

/**
 * カスタムLinearLayout
 * <ul>
 * <li>背景自動ダウンロード</li>
 * <li>setDispatchPressedを拒絶する機能</li>
 * </ul>
 *
 * @author y-miyazaki
 */
public class CustomLinearLayout extends LinearLayout {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private final static String TAG = CustomLinearLayout.class.getSimpleName();
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** クリックリスナー */
    private OnLayoutListener mOnLayoutListener;
    /** パフォーマンスのためRectをインスタンス化 */
    private final Rect mRect = new Rect();
    /** 背景のリピート設定 */
    private boolean mIsRepeat;
    /** プレスイベントを子Viewに投げるか？ */
    private boolean mIsDispatchSetPressed = true;
    /** ソフトキーボードリスナー */
    private OnSoftKeyShownListener mOnSoftKeyShownListener;

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
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOnSoftKeyShownListener != null) {
            if (getContext() instanceof Activity) {
                // (a)Viewの高さ
                final int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
                // (b)ステータスバーの高さ
                final Activity activity = (Activity) getContext();

                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
                final int statusBarHeight = mRect.top;
                // (c)ディスプレイサイズ
                final int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
                // (a)-(b)-(c)>100ピクセルとなったらソフトキーボードが表示されてると判断
                // （ソフトキーボードはどんなものでも最低100ピクセルあると仮定）
                final int diff = screenHeight - statusBarHeight - viewHeight;
                mOnSoftKeyShownListener.onSoftKeyShown(diff > 100);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
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
        mOnSoftKeyShownListener = null;

        super.onDetachedFromWindow();
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        if (mIsDispatchSetPressed) {
            super.dispatchSetPressed(pressed);
        }
    }

    /**
     * ソフトキーボード表示リスナー設定
     *
     * @param onSoftKeyShownListener
     *            リスナー
     */
    public void setOnSoftKeyShownListener(OnSoftKeyShownListener onSoftKeyShownListener) {
        this.mOnSoftKeyShownListener = onSoftKeyShownListener;
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
}
