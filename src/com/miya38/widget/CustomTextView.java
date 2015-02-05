package com.miya38.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.miya38.R;
import com.miya38.utils.CollectionUtils;

/**
 * カスタムテキストビュークラス
 * <p>
 * 指定した width に従って、文字列が収まるようにフォントサイズを自動縮小するカスタム TextView。 点滅も可能。
 * </p>
 *
 * @author y-miyazaki
 */
public class CustomTextView extends TextView implements TextWatcher {
    // ----------------------------------------------------------
    // デファイン
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomTextView.class.getSimpleName();
    /** 最小の文字サイズ */
    private static final float MIN_TEXT_SIZE = 1.0f;

    // ----------------------------------------------------------
    // キャッシュ
    // ----------------------------------------------------------
    /** デフォルトのカスタムタイプフェースindex */
    private static int sDefaultTypefaceIndex = -1;
    /** 基本のフォント */
    private static List<Typeface> sTypefaceList;
    /** styleable cache */
    private static volatile int[] sStyleable;
    /** textStyle id cache */
    private static volatile int sTextStyleId = -1;

    // ----------------------------------------------------------
    // カスタムプロパティ
    // ----------------------------------------------------------
    /** 文字サイズの初期値 */
    private float mDefaultTextSize = 0.0f;
    /** 調整後の文字サイズ */
    private float mResizeTextSize = 0.0f;
    /** 文字列のリサイズをするか？ */
    private boolean mIsResize;
    /** 最後を省略するか？ */
    private boolean mIsEllipsize;
    /** 最後を省略に必要な最大行数 */
    private int mEllipsizeMaxlines = 1;

    // ----------------------------------------------------------
    // リスナー
    // ----------------------------------------------------------
    /** {@link OnClickLinkListener} */
    private OnClickLinkListener mOnClickLinkListener;
    /** テキスト幅計測用のペイント */
    private final Paint mPaintForMeasure = new Paint();

    /**
     * クリックリンクリスナークラス
     *
     * @author y-miyazaki
     *
     */
    public interface OnClickLinkListener {
        /**
         * リンクがクリックされた際のコールバックメソッド
         *
         * @param view
         *            View
         */
        void onClick(View view);
    }

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     */
    public CustomTextView(final Context context) {
        super(context);
        // テキストサイズ
        mDefaultTextSize = getTextSize();
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
    public CustomTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        addTextChangedListener(this);
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
    public CustomTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * init
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    private void init(final Context context, final AttributeSet attrs) {
        // テキストサイズ
        mDefaultTextSize = getTextSize();

        final TypedArray ta1 = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        final String htmlText = ta1.getString(R.styleable.CustomTextView_html_text);
        final String editText = ta1.getString(R.styleable.CustomTextView_edit_text);
        final int typeFace = ta1.getInteger(R.styleable.CustomTextView_type_face, sDefaultTypefaceIndex);
        this.mIsResize = ta1.getBoolean(R.styleable.CustomTextView_resize, false);
        this.mIsEllipsize = ta1.getBoolean(R.styleable.CustomTextView_ellipsize, false);
        this.mEllipsizeMaxlines = ta1.getInteger(R.styleable.CustomTextView_ellipsize_maxlines, 1);
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
                    sTextStyleId = Class.forName("com.android.internal.R$styleable").getField("TextView_textStyle").getInt(null);
                }
            }
            final TypedArray ta2 = context.obtainStyledAttributes(attrs, sStyleable);

            // フォント変更
            if (!CollectionUtils.isNullOrEmpty(sTypefaceList)) {
                setTypeface(sTypefaceList.get(typeFace), ta2.getInt(sTextStyleId, 0));
            }
            ta2.recycle();
        } catch (final Exception e) {
            // 握りつぶす
        }

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
        addTextChangedListener(this);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsResize) {
            fitTextSize();
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 1行を超える場合は、...を付与する
        setEllipSize();
        // 画面にフィットしたサイズにする
        fitTextSize();
    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int start, final int lengthBefore, final int lengthAfter) {
        super.onTextChanged(charSequence, start, lengthBefore, lengthAfter);
        // 1行を超える場合は、...を付与する
        setEllipSize();
        // 画面にフィットしたサイズにする
        fitTextSize();
    }

    @Override
    public void beforeTextChanged(final CharSequence paramCharSequence, final int paramInt1, final int paramInt2, final int paramInt3) {
        // 何もしない。
    }

    @Override
    public void afterTextChanged(final Editable paramEditable) {
        // 何もしない。
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        setBackgroundDrawable(null);
        setOnClickListener(null);
        removeTextChangedListener(this);
        mOnClickLinkListener = null;
        super.onDetachedFromWindow();
    }

    /**
     * HTMLフォーマットのTextに変換する。
     *
     * @param text
     *            HTML文字列
     */
    public void setHtml(final CharSequence text) {
        if (text == null) {
            setText(null);
        } else {
            final ImageGetterImpl imageGetter = new ImageGetterImpl(getContext().getApplicationContext());
            setText(Html.fromHtml(text.toString(), imageGetter, null));
        }
    }

    /**
     * タイプフェースで追加したものをindex指定する。
     *
     * @return タイプフェースカウント数
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
    public static void setDefaultCustomTypeface(final int index) {
        sDefaultTypefaceIndex = index;
    }

    /**
     * タイプフェース追加
     *
     * @param typeface
     *            {@link Typeface}
     */
    public static synchronized void addCustomTypeface(final Typeface typeface) {
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
    public void setCustomTypeface(final int index) {
        try {
            if (!CollectionUtils.isNullOrEmpty(sTypefaceList)) {
                setTypeface(sTypefaceList.get(index));
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            // 握りつぶす
        }
    }

    /**
     * 追加したタイプフェースを取得する。
     *
     * @param index
     *            タイプフェース設定のためのインデックス(0から指定)
     * @return Typeface
     */
    public static Typeface getCustomTypeface(final int index) {
        try {
            if (sTypefaceList != null) {
                return sTypefaceList.get(index);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * テキストリンククリックリスナー
     * <p>
     * テキスト中の文字列の一部をAタグのようにリンクし、{@link OnClickLinkListener}でクリックしたことを受け取ることができる。
     * <br>
     * リンクの文字列は引数linkで設定する。
     * </p>
     *
     * @param link
     *            リンク部分文字列
     * @param l
     *            {@link OnClickLinkListener}
     */
    public void setOnClickLinkListener(final String link, final OnClickLinkListener l) {
        mOnClickLinkListener = l;
        final SpannableString spannable = SpannableString.valueOf(getText());
        final Pattern pattern = Pattern.compile(link);
        final Matcher matcher = pattern.matcher(spannable);

        // Create FragmentSpans for each match
        while (matcher.find()) {
            spannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(final View view) {
                    if (mOnClickLinkListener != null) {
                        mOnClickLinkListener.onClick(view);
                    }
                }
            }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // Set new spans in CustomTextView
        setText(spannable);

        // Listen for spannable clicks, if not already
        final MovementMethod m = getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (getLinksClickable()) {
                setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
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
        } catch (final StringIndexOutOfBoundsException e) {
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
        // 設定された文字サイズで初期化する
        mResizeTextSize = mDefaultTextSize;
        mPaintForMeasure.setTextSize(mResizeTextSize);

        // 適切なサイズになるまで小さくしていきます。(paddingも考慮します。)
        final int targetWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        // 改行対応のため、改行でスプリットする
        float stringWidth = 0.f;
        final String[] stringList = getText().toString().split("\r?\n");
        String string = "";

        // 最大幅と対象文字列を保存
        for (final String str : stringList) {
            stringWidth = stringWidth > mPaintForMeasure.measureText(str) ? stringWidth : mPaintForMeasure.measureText(str);
            string = mPaintForMeasure.measureText(string) > mPaintForMeasure.measureText(str) ? string : str;
        }

        // 幅に合うように文字列を縮小する
        while (targetWidth < mPaintForMeasure.measureText(string)) {
            mPaintForMeasure.setTextSize(--mResizeTextSize);

            // 最小文字サイズより小さくなった場合は、文字サイズをセットして終了する
            if (mResizeTextSize <= MIN_TEXT_SIZE) {
                mResizeTextSize = MIN_TEXT_SIZE;
                break;
            }
        }
        super.setTextSize(mResizeTextSize);
    }

    /**
     * テキスト幅を取得
     *
     * @param textSize
     *            文字サイズ
     * @param text
     *            文字列
     * @param typeFace
     *            文字種
     * @return テキスト幅
     */
    private float getTextWidth(final float textSize, final String text, final Typeface typeFace) {
        mPaintForMeasure.setTextSize(textSize);
        mPaintForMeasure.setTypeface(typeFace);
        return mPaintForMeasure.measureText(text);
    }
}
