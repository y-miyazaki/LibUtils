package com.miya38.widget;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.miya38.R;
import com.miya38.utils.ClassUtils;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;

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
    private static final Field F_URL = ClassUtils.getField(NetworkImageView.class, "mUrl");
    /** field mImageContainer */
    private static final Field F_MIMAGE_CONTAINER = ClassUtils.getField(NetworkImageView.class, "mImageContainer");
    /** field mImageContainer */
    private static final Field F_MCACHE = ClassUtils.getField(ImageLoader.class, "mCache");

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

    @Override
    public void setOnClickListener(final OnClickListener l) {
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
    public void setAnimationDuration(final int animationDuration) {
        this.mAnimationDuration = animationDuration;
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

    @Override
    public void setImageBitmap(final Bitmap bm) {
        try {
            if (mIsCorner) {
                super.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bm, (int) mCornerRadius));
            } else {
                super.setImageBitmap(bm);
            }
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

    @Override
    public void setImageUrl(final String url, final ImageLoader imageLoader) {
        setImageUrl(url, imageLoader, 0, 0, true);
    }

    @Override
    public void setImageUrl(final String url, final ImageLoader imageLoader, final int maxWidth, final int maxHeight) {
        setImageUrl(url, imageLoader, maxWidth, maxHeight, true);
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
                final ImageCache imageCache = (ImageCache) F_MCACHE.get(imageLoader);
                final Bitmap cacheBitmap = imageCache.getBitmap(url);
                if (cacheBitmap == null) {
                    final Bitmap bitmap = ImageUtils.getBitmap(url, maxWidth, maxHeight);
                    imageCache.putBitmap(url, bitmap);
                    setImageBitmap(bitmap);
                } else {
                    setImageBitmap(cacheBitmap);
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
            F_URL.set(this, null);
            final ImageContainer imageContainer = (ImageContainer) F_MIMAGE_CONTAINER.get(this);
            if (imageContainer != null) {
                imageContainer.cancelRequest();
                F_MIMAGE_CONTAINER.set(this, null);
            }
        } catch (final Exception e) {
            // 握りつぶす
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mUrls = null;
        mOnClickListener = null;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.setImageBitmap(null);
        super.onDetachedFromWindow();
    }
}
