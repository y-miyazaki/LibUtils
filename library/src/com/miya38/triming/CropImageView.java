package com.miya38.triming;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;

import java.util.ArrayList;

/**
 * トリミング用ImageView
 */
public class CropImageView extends ImageViewTouchBase {
    /**
     * ログタグ
     */
    private static final String TAG = "CropImageView";

    /**
     * HighlightViewリスト
     */
    public ArrayList<HighlightView> mHighlightViews = new ArrayList<HighlightView>();

    /**
     * mMotionHighlightView
     */
    HighlightView mMotionHighlightView = null;

    /**
     * 前回タップ位置
     */
    float mLastX, mLastY;

    /**
     * 切り取り枠が移動中か判別するフラグ
     */
    int mMotionEdge;
    /**
     * CropImageViewクラス内ログフラグ
     */
    private final boolean isLog = false;

    private final Context mContext;

    /**
     * コンストラクタ
     * 
     * @param context
     * @param attrs
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CropImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (AplUtils.hasHoneycomb()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.view.ImageViewTouchBase#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mBitmapDisplayed.getBitmap() != null) {
            for (final HighlightView hv : mHighlightViews) {
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.view.ImageViewTouchBase#zoomTo(float, float, float)
     */
    @Override
    protected void zoomTo(final float scale, final float centerX, final float centerY) {
        super.zoomTo(scale, centerX, centerY);
        for (final HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.view.ImageViewTouchBase#zoomIn()
     */
    @Override
    protected void zoomIn() {
        super.zoomIn();
        for (final HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.view.ImageViewTouchBase#zoomOut()
     */
    @Override
    protected void zoomOut() {
        super.zoomOut();
        for (final HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.view.ImageViewTouchBase#postTranslate(float, float)
     */
    @Override
    protected void postTranslate(final float deltaX, final float deltaY) {
        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            final HighlightView hv = mHighlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    /*
     * According to the event's position, change the focus to the first
     * hitting cropping rectangle.
     */
    private void recomputeFocus(final MotionEvent event) {
        for (int i = 0; i < mHighlightViews.size(); i++) {
            final HighlightView hv = mHighlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }

        for (int i = 0; i < mHighlightViews.size(); i++) {
            final HighlightView hv = mHighlightViews.get(i);
            final int edge = hv.getHit(event.getX(), event.getY());
            if (edge != HighlightView.GROW_NONE) {
                if (!hv.hasFocus()) {
                    hv.setFocus(true);
                    hv.invalidate();
                }
                break;
            }
        }
        invalidate();
    }

    /*
     * (非 Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final TrimingActivity cropImage = (TrimingActivity) mContext;
        if (cropImage.mSaving) {
            return false;
        }

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (cropImage.mWaitingToPick) {
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_DOWN mWaitingToPick is True");
                }
                recomputeFocus(event);
            } else {
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_DOWN mWaitingToPick is Flase");
                }
                for (int i = 0; i < mHighlightViews.size(); i++) {
                    final HighlightView hv = mHighlightViews.get(i);
                    final int edge = hv.getHit(event.getX(), event.getY());
                    if (edge != HighlightView.GROW_NONE) {
                        mMotionEdge = edge;
                        mMotionHighlightView = hv;
                        mLastX = event.getX();
                        mLastY = event.getY();
                        mMotionHighlightView.setMode((edge == HighlightView.MOVE) ? HighlightView.ModifyMode.Move : HighlightView.ModifyMode.Grow);
                        break;
                    }
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            if (cropImage.mWaitingToPick) {
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_UP mWaitingToPick is True");
                }
                for (int i = 0; i < mHighlightViews.size(); i++) {
                    final HighlightView hv = mHighlightViews.get(i);
                    if (hv.hasFocus()) {
                        cropImage.mCrop = hv;
                        for (int j = 0; j < mHighlightViews.size(); j++) {
                            if (j == i) {
                                continue;
                            }
                            mHighlightViews.get(j).setHidden(true);
                        }
                        centerBasedOnHighlightView(hv);
                        ((TrimingActivity) mContext).mWaitingToPick = false;

                        return true;
                    }
                }
            } else if (mMotionHighlightView != null) {
                // 切り抜きサイズが変わった場合でタップが離れた場合
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_UP mWaitingToPick is False mMotionHighlightView is not Null");
                }

                // イメージのズーム変更
                centerBasedOnHighlightView(mMotionHighlightView);

                mMotionHighlightView.setMode(HighlightView.ModifyMode.None);
            }
            mMotionHighlightView = null;
            break;
        case MotionEvent.ACTION_MOVE:
            if (cropImage.mWaitingToPick) {
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_MOVE mWaitingToPick is True");
                }
                recomputeFocus(event);
            } else if (mMotionHighlightView != null) {

                // 移動、サイズ変更時
                if (isLog) {
                    LogUtils.d(TAG, "ACTION_MOVE mWaitingToPick is True mMotionHighlightView is not Null");
                }

                mMotionHighlightView.handleMotion(mMotionEdge, event.getX() - mLastX, event.getY() - mLastY);
                mLastX = event.getX();
                mLastY = event.getY();

                if (true) {
                    // This section of code is optional. It has some user
                    // benefit in that moving the crop rectangle against
                    // the edge of the screen causes scrolling but it means
                    // that the crop rectangle is no longer fixed under
                    // the user's finger.
                    ensureVisible(mMotionHighlightView);
                }
            }
            break;
        }

        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            center(true, true);
            break;
        case MotionEvent.ACTION_MOVE:
            // if we're not zoomed then there's no point in even allowing
            // the user to move the image around. This call to center puts
            // it back to the normalized location (with false meaning don't
            // animate).
            if (getScale() == 1F) {
                center(true, true);
            }
            break;
        }

        return true;
    }

    /**
     * ensureVisible
     * 
     * @param hv
     *            Pan the displayed image to make sure the cropping rectangle is
     *            visible.
     */
    private void ensureVisible(final HighlightView hv) {
        final Rect r = hv.mDrawRect;

        final int panDeltaX1 = Math.max(0, mLeft - r.left);
        final int panDeltaX2 = Math.min(0, mRight - r.right);

        final int panDeltaY1 = Math.max(0, mTop - r.top);
        final int panDeltaY2 = Math.min(0, mBottom - r.bottom);

        final int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        final int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    /**
     * centerBasedOnHighlightView
     * 
     * @param hv
     *            If the cropping rectangle's size changed significantly, change
     *            the
     *            view's center and scale according to the cropping rectangle.
     * 
     */
    private void centerBasedOnHighlightView(final HighlightView hv) {
        final Rect drawRect = hv.mDrawRect;

        final float width = drawRect.width();
        final float height = drawRect.height();

        final float thisWidth = getWidth();
        final float thisHeight = getHeight();

        final float z1 = thisWidth / width * .6F;
        final float z2 = thisHeight / height * .6F;

        float zoom = Math.min(z1, z2);
        zoom = zoom * this.getScale();
        zoom = Math.max(1F, zoom);
        if ((Math.abs(zoom - getScale()) / zoom) > .1) {
            final float[] coordinates = new float[] { hv.mCropRect.centerX(), hv.mCropRect.centerY() };
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300F);
        }

        ensureVisible(hv);
    }

    /*
     * (非 Javadoc)
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            mHighlightViews.get(i).draw(canvas);
        }
    }

    /**
     * add
     * 
     * @param hv
     */
    public void add(final HighlightView hv) {
        mHighlightViews.add(hv);
        invalidate();
    }
}
