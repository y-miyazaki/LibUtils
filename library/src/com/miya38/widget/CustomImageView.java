package com.miya38.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.miya38.R;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;

/**
 * カスタムImageViewクラス
 * <p>
 * ImageViewを継承しているクラス。<br>
 * それに加え、もしonClickListenerを指定している場合に選択時されたことをグレースケールでわかるようにしている。<br>
 * </p>
 * 
 * @author y-miyazaki
 */
public class CustomImageView extends ImageView implements OnTouchListener {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomImageView.class.getSimpleName();
    /** アニメーション時間(ms) */
    private static final int ANIMATION_DURATION = 500;
    /** グレースケールカラー */
    private static final int COLOR_GRAYSCALE = 0x80000000;
    /** 角丸サイズ */
    private static final int CORNER_RADIUS = 10;

    // ----------------------------------------------------------
    // attribute
    // ----------------------------------------------------------
    /** コーナー属性 */
    private boolean mIsCorner;
    /** コーナーdimension */
    private float mCornerRadius;
    /** tint attribute */
    private int mTint = -1;
    /** PorterDuff attribute */
    private PorterDuff.Mode mMode;

    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** OnClickListener */
    private OnClickListener mOnClickListener;
    /** animation duration */
    private int mAnimationDuration = ANIMATION_DURATION;
    /** クリック状態フラグ */
    private boolean mIsClick;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
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
    public CustomImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(l);
        this.mOnClickListener = l;
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (mMode != null) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 押した時に指定した色とモードでフィルターをかける
                if (mOnClickListener != null) {
                    mIsClick = true;
                    setColorFilter();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                // 元の画像に戻す
                clearColorFilter();
                mIsClick = false;
                return false;
            case MotionEvent.ACTION_UP:
                setClickable(false);
                // 元の画像に戻す
                clearColorFilter();
                if (mOnClickListener != null) {
                    if (mIsClick) {
                        mOnClickListener.onClick(v);
                    }
                }
                setClickable(true);
                return false;
            case MotionEvent.ACTION_OUTSIDE:
                // 元の画像に戻す
                clearColorFilter();
                mIsClick = false;
                return false;
            default:
                break;
            }
        }
        return true;
    }

    @Override
    public void setImageBitmap(final Bitmap bm) {
        try {
            if (mIsCorner) {
                setImageCorner(bm, (int) mCornerRadius);
            } else {
                super.setImageBitmap(bm);
            }
        } catch (final OutOfMemoryError e) {
            LogUtils.e(TAG, "bitmap OutOfMemoryError", e);
        }
    }

    @Override
    public void setImageResource(final int resId) {
        Bitmap bm = null;
        try {
            if (mIsCorner) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), resId);
                setImageCorner(bm, (int) mCornerRadius);
            } else {
                super.setImageResource(resId);
            }
        } catch (final OutOfMemoryError e) {
            LogUtils.e(TAG, "bitmap OutOfMemoryError", e);
            if (bm != null) {
                bm.recycle();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        setImageBitmap(null);
        mOnClickListener = null;
        super.onDetachedFromWindow();
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
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        final int editSrc = ta.getResourceId(R.styleable.CustomImageView_edit_src, -1);
        this.mIsCorner = ta.getBoolean(R.styleable.CustomImageView_corner, false);
        this.mCornerRadius = ta.getDimension(R.styleable.CustomImageView_corner_radius, CORNER_RADIUS);
        this.mTint = ta.getColor(R.styleable.CustomImageView_tint, -1);

        if (mTint != -1) {
            final String poterduffMode = ta.getString(R.styleable.CustomImageView_porterduff_mode);
            if (poterduffMode == null) {
                // デフォルトでSRC_ATOPとしている
                mMode = PorterDuff.Mode.SRC_ATOP;
            } else {
                try {
                    mMode = (PorterDuff.Mode.valueOf(poterduffMode) == null) ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.valueOf(poterduffMode);
                } catch (final Exception e) {
                    // 何もしない。
                }
            }
            setOnTouchListener(this);
        }
        ta.recycle();

        // エディターモードの場合
        if (isInEditMode()) {
            if (editSrc != -1) {
                final Drawable drawable = getContext().getResources().getDrawable(editSrc);
                if (getDrawable() == null) {
                    setImageDrawable(drawable);
                }
            }
        }
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
     *            tint attribute
     */
    public void setTint(final int tint) {
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
     * 指定のカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    private void setColorFilter() {
        if (mTint != -1) {
            setColorFilter(new PorterDuffColorFilter(mTint, PorterDuff.Mode.SRC_ATOP));
        }
    }

    /**
     * グレースケールのカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    public void setGrayScaleColorFilter() {
        setColorFilter(new PorterDuffColorFilter(COLOR_GRAYSCALE, PorterDuff.Mode.SRC_ATOP));
    }

    /**
     * 画像を消えるようなアルファアニメーションを行う。<br>
     */
    public void setAnimationAlphaOut() {
        ViewHelper.setAlphaOutAnimation(this, mAnimationDuration);
    }

    /**
     * 画像を徐々に見えるようなアルファアニメーションを行う。<br>
     */
    public void setAnimationAlphaIn() {
        ViewHelper.setAlphaInAnimation(this, mAnimationDuration);
    }

    /**
     * 画像がポップアップアニメーションを行う。<br>
     * 
     * @return Animation
     */
    public Animation setAnimationPopUp() {
        final ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mAnimationDuration);
        startAnimation(scale);
        return scale;
    }

    /**
     * 画像を角丸にする。
     * 
     * @param bm
     *            bitmap
     * @param cornerRadius
     *            コーナーのサイズ
     */
    public void setImageCorner(Bitmap bm, int cornerRadius) {
        super.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bm, cornerRadius));
    }

    /**
     * アニメーション時間設定<br>
     * アニメーションにかかる時間をmsecで指定する。
     * 
     * @param animationDuration
     *            msec
     */
    public void setAnimationDuration(final int animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    /**
     * コーナー設定を取得する。
     * 
     * @return コーナー設定<br>
     *         true:有り false:無し
     */
    public boolean isCorner() {
        return mIsCorner;
    }

    /**
     * コーナー設定
     * 
     * @param corner
     *            コーナを表示するか？<br>
     *            true:表示する。/false:表示しない。
     */
    public void setCorner(final boolean corner) {
        this.mIsCorner = corner;
    }

    /**
     * コーナー設定を取得する。
     * 
     * @return コーナー設定
     */
    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    /**
     * コーナー設定
     * 
     * @param cornerRadius
     *            コーナーの角丸のサイズ
     */
    public void setCornerRadius(final int cornerRadius) {
        this.mCornerRadius = cornerRadius;
    }

}
