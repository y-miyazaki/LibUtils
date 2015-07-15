package com.miya38.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.miya38.R;
import com.miya38.utils.AnimationUtils;
import com.miya38.utils.ClassUtils;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.callback.OnNetworkImageViewListener;
import com.miya38.widget.volley.NetworkImageView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * カスタムImageViewクラス
 * <p>
 * VolleyのNetwotkImageViewを継承しているクラス。<br>
 * それに加え、もしonClickListenerを指定している場合に選択時されたことをグレースケールでわかるようにしている。<br>
 * </p>
 * 
 * @author y-miyazaki
 */
public class CustomNetworkImageView extends NetworkImageView implements OnTouchListener {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomNetworkImageView.class.getSimpleName();

    /** field mUrl */
    private static final Field FIELD_URL = ClassUtils.getField(NetworkImageView.class, "mUrl");
    /** field mImageContainer */
    private static final Field FIELD_MIMAGE_CONTAINER = ClassUtils.getField(NetworkImageView.class, "mImageContainer");
    /** field mImageContainer */
    private static final Field FIELD_MCACHE = ClassUtils.getField(ImageLoader.class, "mCache");

    /** アニメーション時間(ms) */
    private static final int ANIMATION_DURATION = 250;
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
    /** 表示時にアルファをかけて表示するか？ */
    private boolean mIsAlphaAnimation;
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** OnClickListener */
    private OnClickListener mOnClickListener;
    /** OnNetworkImageViewListener */
    private OnNetworkImageViewListener mOnNetworkImageViewListener;
    /** animation duration */
    private int mAnimationDuration = ANIMATION_DURATION;
    /** クリック状態フラグ */
    private boolean mIsClick;

    // ----------------------------------------------------------
    // ImageView change
    // ----------------------------------------------------------
    /** URLのリスト */
    private List<String> mUrls;
    /** 現在読み込み中のURLインデックス */
    private int mIndex;
    /** タイマー */
    private Timer mTimer;

    /**
     * コンストラクタ
     * 
     * @param context
     *            Context for this View
     * @param attrs
     *            AttributeSet for this View. The attribute 'preset_size' is
     *            processed here
     */
    public CustomNetworkImageView(final Context context, final AttributeSet attrs) {
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
    public CustomNetworkImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(l);
        this.mOnClickListener = l;
    }

    @Override
    public void setImageResource(int resId) {
        try {
            super.setImageResource(resId);
        } catch (OutOfMemoryError e) {
            LogUtils.e(TAG, "setImageResouce OutOfMemoryError(%d)", resId);
        }
    }

    @Override
    public void setImageBitmap(final Bitmap bm) {
        setImageBitmap(bm, mIsAlphaAnimation);
    }

    @Override
    public void setImageUrl(final String url, final ImageLoader imageLoader) {
        setImageUrl(url, imageLoader, 0, 0, true);
    }

    @Override
    public void setImageUrl(final String url, final ImageLoader imageLoader, final int maxWidth, final int maxHeight) {
        setImageUrl(url, imageLoader, maxWidth, maxHeight, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        mUrls = null;
        mOnClickListener = null;
        mOnNetworkImageViewListener = null;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.setImageBitmap(null);
        super.onDetachedFromWindow();
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
        this.mIsAlphaAnimation = ta.getBoolean(R.styleable.CustomImageView_is_alpha_animation, false);
        this.mTint = ta.getInt(R.styleable.CustomImageView_tint, -1);
        if (mTint != -1) {
            final String poterduffMode = ta.getString(R.styleable.CustomImageView_porterduff_mode);
            if (poterduffMode == null) {
                // デフォルトでSRC_ATOPとしている
                mMode = PorterDuff.Mode.SRC_ATOP;
            } else {
                try {
                    mMode = (PorterDuff.Mode.valueOf(poterduffMode) == null) ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.valueOf(poterduffMode);
                } catch (final Exception e) {
                    // 握りつぶす
                }
            }
            setOnTouchListener(this);
        }
        ta.recycle();
        // エディターモードの場合
        if (isInEditMode()) {
            if (editSrc != -1) {
                setImageResource(editSrc);
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
     *            colorFilter
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
        setColorFilter(new PorterDuffColorFilter(mTint, PorterDuff.Mode.SRC_ATOP));
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
        AnimationUtils.setAlphaOutAnimation(this, mAnimationDuration);
    }

    /**
     * 画像を徐々に見えるようなアルファアニメーションを行う。<br>
     */
    public void setAnimationAlphaIn() {
        AnimationUtils.setAlphaInAnimation(this, mAnimationDuration);
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

    /**
     * setImageBitmap＋アルファアニメーション&コーナー設定
     * 
     * @param bm
     *            Bitmap
     * @param isAlphaAnimation
     *            true:アルファアニメーションON/false:アルファアニメーションOFF
     */
    public void setImageBitmap(final Bitmap bm, final boolean isAlphaAnimation) {
        try {
            if (mIsCorner) {
                super.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bm, (int) mCornerRadius));
            } else {
                super.setImageBitmap(bm);
            }

            // Bitmap設定
            if (mOnNetworkImageViewListener != null) {
                mOnNetworkImageViewListener.onSetImageBitmap(bm);
            }

//            if (bm != null) {
//                 if (isAlphaAnimation) {
//                     setAnimationAlphaIn();
//                 }
//            }
        } catch (final OutOfMemoryError e) {
            LogUtils.e(TAG, "setImageBitmap bitmap OutOfMemoryError", e);
        }
    }

    /**
     * イメージチェンジURL設定処理
     * 
     * @param urls
     *            URLのリスト
     * @param period
     *            画像の切り替え時間
     * @param imageLoader
     *            {@link ImageLoader}
     */
    public void setImageUrl(final List<String> urls, final int period, final ImageLoader imageLoader) {
        mUrls = urls;
        mIndex = 0;

        if (mTimer != null) {
            mTimer.cancel();
        }
        final Handler handler = new Handler();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setImageUrl(mUrls.get(mIndex++), imageLoader, 0, 0, false);
                        } catch (final IndexOutOfBoundsException e) {
                            mIndex = 0;
                            setImageUrl(mUrls.get(mIndex++), imageLoader, 0, 0, false);
                        } catch (final NullPointerException e) {
                            // 何もしない。
                        }
                    }
                });
            }
        }, 0, period);
    }

    /**
     * 
     * @param url
     *            URL
     * @param imageLoader
     *            {@link ImageLoader}
     * @param maxWidth
     *            最大横幅(アスペクト比考慮した最大長)<br>
     *            0以下を指定した場合は、そのままの画像サイズを設定
     * @param maxHeight
     *            最大縦幅(アスペクト比考慮した最大長)<br>
     *            0以下を指定した場合は、そのままの画像サイズを設定
     * @param isTimerCancel
     *            true:タイマーをキャンセルする。<br>
     *            false:タイマーをキャンセルしない。
     */
    private void setImageUrl(final String url, final ImageLoader imageLoader, final int maxWidth, final int maxHeight, final boolean isTimerCancel) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (isTimerCancel && mTimer != null) {
            mTimer.cancel();
        }
        try {
            if (ImageUtils.isDataUriScheme(url)) {
                final ImageCache imageCache = (ImageCache) FIELD_MCACHE.get(imageLoader);
                final Bitmap cacheBitmap = imageCache.getBitmap(getCacheKey(url, maxWidth, maxHeight));
                if (cacheBitmap == null) {
                    final Bitmap bitmap = ImageUtils.getBitmap(url, maxWidth, maxHeight);
                    imageCache.putBitmap(url, bitmap);
                    setImageBitmap(bitmap, false);
                } else {
                    setImageBitmap(cacheBitmap, false);
                }
            } else {
                super.setImageUrl(url, imageLoader, maxWidth, maxHeight);
            }
        } catch (final OutOfMemoryError e) {
            LogUtils.e(TAG, "setImageUrl bitmap OutOfMemoryError", e);
            setImageBitmap(null);
        } catch (final IllegalAccessException e) {
            // 何もしない。
        } catch (final IllegalArgumentException e) {
            // 何もしない。
        }
    }

    /**
     * リクエストをキャンセルする。
     */
    public void cancelRequest() {
        try {
            FIELD_URL.set(this, null);
            final ImageContainer imageContainer = (ImageContainer) FIELD_MIMAGE_CONTAINER.get(this);
            if (imageContainer != null) {
                imageContainer.cancelRequest();
                FIELD_MIMAGE_CONTAINER.set(this, null);
            }
        } catch (final Exception e) {
            // 握りつぶす
        }
    }

    /**
     * OnNetworkImageViewListener設定
     * 
     * @param l
     *            {@link OnNetworkImageViewListener}
     */
    public void setOnNetworkImageViewListener(OnNetworkImageViewListener l) {
        this.mOnNetworkImageViewListener = l;
    }

    /**
     * Creates a cache key for use with the L1 cache.
     * 
     * @param url
     *            The URL of the request.
     * @param maxWidth
     *            The max-width of the output.
     * @param maxHeight
     *            The max-height of the output.
     */
    public static String getCacheKey(final String url, final int maxWidth, final int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }
}
