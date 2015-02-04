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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.miya38.R;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;

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
    private int mAnimationDuration = 500;
    /** クリック状態フラグ */
    private boolean mIsClick;

    /**
     * コンストラクタ
     *
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is processed here
     */
    public CustomImageView(Context context, AttributeSet attrs) {
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
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
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
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        int editSrc = ta.getResourceId(R.styleable.CustomImageView_edit_src, -1);
        this.mIsCorner = ta.getBoolean(R.styleable.CustomImageView_corner, false);
        this.mCornerRadius = ta.getDimension(R.styleable.CustomImageView_corner_radius, 10);
        this.mTint = ta.getColor(R.styleable.CustomImageView_tint, -1);

        if (mTint != -1) {
            final String poterduffMode = ta.getString(R.styleable.CustomImageView_porterduff_mode);
            if (poterduffMode == null) {
                // デフォルトでSRC_ATOPとしている
                mMode = PorterDuff.Mode.SRC_ATOP;
            } else {
                try {
                    mMode = (PorterDuff.Mode.valueOf(poterduffMode) == null) ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.valueOf(poterduffMode);
                } catch (Exception e) {
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
     * 指定のカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    private void setColorFilter() {
        if(mTint != -1){
            setColorFilter(new PorterDuffColorFilter(mTint, PorterDuff.Mode.SRC_ATOP));
        }
    }

    /**
     * グレースケールのカラーフィルターを掛ける<br>
     * クリアする場合は、clearColorFilter()をすること。
     */
    public void setGrayScaleColorFilter() {
        setColorFilter(new PorterDuffColorFilter(0x80000000, PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        this.mOnClickListener = l;
    }

    /**
     * 画像を消えるようなアルファアニメーションを行う。<br>
     *
     * @return Animation
     */
    public Animation alphaOutAnimation() {
        final AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(mAnimationDuration);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(true);
        startAnimation(alpha);
        return alpha;
    }

    /**
     * 画像を徐々に見えるようなアルファアニメーションを行う。<br>
     *
     * @return Animation
     */
    public Animation alphaInAnimation() {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(mAnimationDuration);
        // alpha.setFillEnabled(true);
        // alpha.setFillAfter(true);
        startAnimation(alpha);
        return alpha;
    }

    /**
     * 画像がポップアップアニメーションを行う。<br>
     *
     * @return Animation
     */
    public Animation popUpAnimation() {
        final ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mAnimationDuration);
        startAnimation(scale);
        return scale;
    }

    /**
     * アニメーション時間設定<br>
     * アニメーションにかかる時間をmsecで指定する。
     *
     * @param animationDuration
     *            msec
     */
    public void setAnimationDuration(int animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
    public void setCorner(boolean corner) {
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
    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = cornerRadius;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        try {
            if (mIsCorner) {
                super.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bm, (int) mCornerRadius));
            } else {
                super.setImageBitmap(bm);
            }
        } catch (OutOfMemoryError e) {
            LogUtils.e(TAG, "bitmap OutOfMemoryError", e);
        }
    }

    @Override
    public void setImageResource(int resId) {
        Bitmap bm = null;
        try {
            if (mIsCorner) {
                bm = BitmapFactory.decodeResource(getContext().getResources(), resId);
                super.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bm, (int) mCornerRadius));
            } else {
                super.setImageResource(resId);
            }
        } catch (OutOfMemoryError e) {
            LogUtils.e(TAG, "bitmap OutOfMemoryError", e);
            if (bm != null) {
                bm.recycle();
                bm = null;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        setImageBitmap(null);
        mOnClickListener = null;
        super.onDetachedFromWindow();
    }
}
