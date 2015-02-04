package com.miya38.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.miya38.R;
import com.miya38.utils.AplUtils;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.LogUtils;

/**
 * カスタムボタンクラス
 * <p>
 * 外部フォントを自動的に組み込む
 * </p>
 *
 * @author y-miyazaki
 */
public class CustomButton extends Button implements OnClickListener, OnTouchListener, TextWatcher {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomButton.class.getSimpleName();

    // ----------------------------------------------------------
    // キャッシュ
    // ----------------------------------------------------------
    /** デフォルトのカスタムタイプフェースindex */
    private static int sDefaultTypefaceIndex = -1;
    /** 基本のフォント */
    private static List<Typeface> sTypefaceList;
    /** styleable cache */
    private static volatile int[] sStyleable;
    /** id cache */
    private static volatile int sTextStyleId = -1;

    // ----------------------------------------------------------
    // カスタムプロパティ(same CustomTextView)
    // ----------------------------------------------------------
    /** 文字列のリサイズをするか？ */
    private boolean mIsResize;
    /** 明滅設定 */
    private boolean mIsBlink;
    /** 最後を省略するか？ */
    private boolean mIsEllipsize;
    /** 最後を省略に必要な最大行数 */
    private int mEllipsizeMaxlines = 1;
    /** 明滅間隔 */
    private int mBlinkDuration = 500;
    /** 明滅回数 */
    private int mBlinkCount = -1;

    // ----------------------------------------------------------
    // カスタムプロパティ
    // ----------------------------------------------------------
    /** tint attribute */
    private int mTint = -1;
    /** PorterDuff attribute */
    private PorterDuff.Mode mMode;

    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** クリックリスナー */
    private OnClickListener mOnClickListener;
    /** TouchState */
    private boolean mIsTouchState;

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
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
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * init
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    private void init(Context context, AttributeSet attrs) {
        final TypedArray ta1 = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        final String htmlText = ta1.getString(R.styleable.CustomTextView_html_text);
        final String editText = ta1.getString(R.styleable.CustomTextView_edit_text);
        final int typeFace = ta1.getInteger(R.styleable.CustomTextView_type_face, sDefaultTypefaceIndex);
        this.mIsResize = ta1.getBoolean(R.styleable.CustomTextView_resize, false);
        this.mIsEllipsize = ta1.getBoolean(R.styleable.CustomTextView_ellipsize, false);
        this.mEllipsizeMaxlines = ta1.getInteger(R.styleable.CustomTextView_ellipsize_maxlines, 1);
        this.mIsBlink = ta1.getBoolean(R.styleable.CustomTextView_blink, false);
        this.mBlinkCount = ta1.getInt(R.styleable.CustomTextView_blink_count, -1);
        this.mBlinkDuration = ta1.getInt(R.styleable.CustomTextView_blink_duration, 500);
        ta1.recycle();

        try {
            // パフォーマンス改善のため、static化
            if (sStyleable == null) {
                synchronized (this) {
                    sStyleable = (int[]) Class.forName("com.android.internal.R$styleable").getField("TextView").get(null);
                }
            }
            if (sTextStyleId == -1) {
                synchronized (this) {
                    sTextStyleId = (Integer) Class.forName("com.android.internal.R$styleable").getField("TextView_textStyle").getInt(null);
                }
            }
            final TypedArray ta3 = context.obtainStyledAttributes(attrs, sStyleable);

            // フォント変更
            if (!CollectionUtils.isNullOrEmpty(sTypefaceList)) {
                setTypeface(sTypefaceList.get(typeFace), ta3.getInt(sTextStyleId, 0));
            }
            ta3.recycle();
        } catch (Exception e) {
            // 握りつぶす
        }

        final TypedArray ta4 = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        this.mTint = ta4.getInt(R.styleable.CustomImageView_tint, -1);

        if (mTint != -1) {
            final String poterduffMode = ta4.getString(R.styleable.CustomImageView_porterduff_mode);
            if (poterduffMode == null) {
                // デフォルトでSRC_ATOPとしている
                mMode = PorterDuff.Mode.SRC_ATOP;
            } else {
                try {
                    mMode = (PorterDuff.Mode.valueOf(poterduffMode) == null) ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.valueOf(poterduffMode);
                } catch (Exception e) {
                    // 握りつぶす
                }
            }
            setOnTouchListener(this);
        }
        ta4.recycle();

        // HTMLで記載を行う
        if (!TextUtils.isEmpty(htmlText)) {
            setHtml(htmlText);
        }
        // エディターモードの場合
        if (isInEditMode()) {
            // テキストが空で、エディターモード用テキストがある場合
            if (TextUtils.isEmpty(getText())) {
                if (editText != null) {
                    setText(editText);
                }
            }
        }
        super.setOnClickListener(this);
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsResize) {
            fitTextSize();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 1行を超える場合は、...を付与する
        setEllipSize();
        // 画面にフィットしたサイズにする
        fitTextSize();
        if (this.mIsBlink) {
            setBlink();
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(charSequence, start, lengthBefore, lengthAfter);
        if (mIsResize) {
            fitTextSize();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        // 何もしない。
    }

    @Override
    public void afterTextChanged(Editable paramEditable) {
        // 何もしない。
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        setBackgroundDrawable(null);
        setOnClickListener(null);
        removeTextChangedListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mMode != null) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouchState = true;
                // 押した時に指定した色とモードでフィルターをかける
                if (mOnClickListener != null) {
                    setColorFilter(v);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final Rect r = new Rect();
                v.getLocalVisibleRect(r);
                if (!r.contains((int) event.getX(), (int) event.getY())) {
                    clearColorFilter(v);
                    mIsTouchState = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                // 元の画像に戻す
                clearColorFilter(v);
                mIsTouchState = false;
                return false;
            case MotionEvent.ACTION_UP:
                // 元の画像に戻す
                clearColorFilter(v);
                if (mOnClickListener != null) {
                    if (mIsTouchState) {
                        mOnClickListener.onClick(v);
                    }
                }
                mIsTouchState = false;
                return false;
            default:
                break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        setClickable(false);
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
        AplUtils.closeKeyboard(getContext(), v);
        setClickable(true);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Arrows対応
        if (!isEnabled()) {
            setPressed(true);
        }
        super.onDraw(canvas);
    }

    private TextPaint getTextPaint() {
        try {
            final Field mTextPaint = TextView.class.getDeclaredField("mTextPaint");
            mTextPaint.setAccessible(true);
            return (TextPaint) mTextPaint.get(this);
        } catch (Exception e) {
            LogUtils.e(TAG, "error", e);
        }
        return null;
    }

    /**
     * Androidのellipsizeが使えないので独自に作成
     */
    private void setEllipSize() {
        try {
            if (mIsEllipsize) {
                if (getLineCount() > mEllipsizeMaxlines) {
                    final int lineEndIndex = getLayout().getLineEnd(mEllipsizeMaxlines - 1);
                    final StringBuilder text = new StringBuilder();

                    // 省略する文字数が3文字より大きくなければ、省略しない
                    if (lineEndIndex > 3) {
                        text.append(getText().subSequence(0, lineEndIndex - 3));
                        text.append("...");
                        setText(text);
                        setMaxLines(mEllipsizeMaxlines);
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            // 握りつぶす
        }
    }

    /**
     * 割り当てられた領域に対して適切なテキストサイズに補正します。
     */
    private void fitTextSize() {
        if (!mIsResize) {
            return;
        }
        int textSize = (int) getTextSize();
        final Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);

        // 適切なサイズになるまで小さくしていきます。(paddingも考慮します。)
        final int targetWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        // 改行対応のため、改行でスプリットする
        float stringWidth = 0.f;
        final String[] stringList = getText().toString().split("\r?\n");
        String string = "";

        // 最大幅と対象文字列を保存
        for (final String str : stringList) {
            stringWidth = stringWidth > textPaint.measureText(str) ? stringWidth : textPaint.measureText(str);
            string = textPaint.measureText(string) > textPaint.measureText(str) ? string : str;
        }

        // 幅に合うように文字列を縮小する
        int count = 0;
        while (targetWidth < stringWidth) {
            count++;
            textPaint.setTextSize(--textSize);
            stringWidth = textPaint.measureText(string);

            // 永久ループ対策
            if (100 < count) {
                break;
            }
        }
        setTextSize(textSize);
        getTextPaint().setTextSize(textSize);
    }

    /**
     * HTMLフォーマットのTextに変換する。
     *
     * @param text
     *            HTML文字列
     */
    public void setHtml(CharSequence text) {
        if (text == null) {
            setText(null);
        } else {
            ImageGetterImpl imageGetter = new ImageGetterImpl(getContext().getApplicationContext());
            setText(Html.fromHtml(text.toString(), imageGetter, null));
        }
    }

    /**
     * 文字を明滅させる
     *
     * @param blinkCount
     *            明滅回数
     * @param blinkDuration
     *            明滅間隔(msec)
     *
     */
    public void setBlink(int blinkCount, int blinkDuration) {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(blinkDuration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(blinkCount);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        startAnimation(alpha);
    }

    /**
     * 文字を明滅させる
     *
     */
    public void setBlink() {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(mBlinkDuration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(mBlinkCount);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        startAnimation(alpha);
    }

    /**
     * タイプフェースで追加したものをindex指定する。
     */
    public static int getCustomTypefaceCount() {
        return sTypefaceList == null ? 0 : sTypefaceList.size();
    }

    /**
     * タイプフェースで追加したものをindex指定する。
     *
     * @param index
     *            タイプフェース取得のためのインデックス(0から指定)
     */
    public static void setDefaultCustomTypeface(int index) {
        sDefaultTypefaceIndex = index;
    }

    /**
     * タイプフェース追加
     *
     * @param typeface
     *            {@link Typeface}
     */
    public synchronized static void addCustomTypeface(Typeface typeface) {
        if (sTypefaceList == null) {
            sTypefaceList = new ArrayList<Typeface>();
        }
        sTypefaceList.add(typeface);
    }

    /**
     * 追加したタイプフェースを指定する
     *
     * @param index
     *            タイプフェース設定のためのインデックス(0から指定)
     */
    public void setCustomTypeface(int index) {
        try {
            if (!CollectionUtils.isNullOrEmpty(sTypefaceList)) {
                setTypeface(sTypefaceList.get(index));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // 握りつぶす
        }
    }

    /**
     * 追加したタイプフェースを取得する。
     *
     * @param index
     *            タイプフェース設定のためのインデックス(0から指定)
     * @return
     */
    public static Typeface getCustomTypeface(int index) {
        try {
            if (sTypefaceList != null) {
                return sTypefaceList.get(index);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * tint設定を取得する。
     *
     * @return tint設定<br>
     */
    public int getTint() {
        return this.mTint;
    }

    /**
     * コーナー設定
     *
     * @param tint
     */
    public void setTint(int tint) {
        this.mTint = tint;

        if (tint == -1) {
            this.mMode = null;
            setOnTouchListener(null);
        } else {
            // デフォルトでSRC_ATOPとしている
            this.mMode = PorterDuff.Mode.SRC_ATOP;
            setOnTouchListener(this);
        }
    }

    /**
     * カラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    private void setColorFilter(View v) {
        if (v != null && v.getBackground() != null) {
            if(mTint != -1){
                v.getBackground().setColorFilter(new PorterDuffColorFilter(mTint, PorterDuff.Mode.SRC_ATOP));
                v.getBackground().invalidateSelf();
            }
        }
    }

    /**
     * グレースケールのカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    private void clearColorFilter(View v) {
        if (v != null && v.getBackground() != null) {
            v.getBackground().clearColorFilter();
            v.getBackground().invalidateSelf();
        }
    }

    /**
     * グレースケールのカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    public void setGrayScaleColorFilter() {
        final Drawable drawable = getBackground();
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(0x80000000, PorterDuff.Mode.SRC_ATOP));
            drawable.invalidateSelf();
        }
    }

    /**
     * グレースケールのカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    public void clearColorFilter() {
        final Drawable drawable = getBackground();
        if (drawable != null) {
            drawable.clearColorFilter();
            drawable.invalidateSelf();
        }
    }
}
