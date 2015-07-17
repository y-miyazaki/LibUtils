package com.miya38.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * アニメーションオブジェクト抽象化クラス
 */
public abstract class AbstractAnimation {
    protected final String TAG = getClass().getSimpleName();

    /** View */
    protected View mView01;
    /** View */
    protected View mView02;
    /** アニメーションの時間(ms) */
    protected int mDuration = 1000;
    /** アニメーションの開始時間 */
    protected int mStartOffset;
    /** アニメーションのリピート回数 */
    protected int mRepeatCount;
    /** アニメーションのInterpolator */
    protected Interpolator mInterpolator = new LinearInterpolator();
    /** アニメーションコールバックリスナー */
    protected Animator.AnimatorListener mAnimatorListener;

    protected abstract AnimatorSet prepare();

    /**
     * View設定
     *
     * @param v
     *         {@link View}
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation view(View v) {
        mView01 = v;
        return this;
    }

    /**
     * View設定
     *
     * @param from
     *         {@link View}
     * @param to
     *         {@link View}
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation view(View from, View to) {
        mView01 = from;
        mView02 = to;
        return this;
    }

    /**
     * アニメーションの時間(ms)設定
     *
     * @param duration
     *         アニメーションの開始時間
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation duration(final int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * アニメーションの開始時間設定
     *
     * @param startOffset
     *         アニメーションの開始時間
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation startOffset(final int startOffset) {
        mStartOffset = startOffset;
        return this;
    }

    /**
     * アニメーションの開始時間設定
     *
     * @param repeatCount
     *         アニメーションのリピート回数
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation repeatCount(int repeatCount) {
        mRepeatCount = repeatCount;
        return this;
    }

    /**
     * アニメーションのInterpolator設定
     *
     * @param interpolator
     *         {@link Interpolator}
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation interpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    /**
     * コールバック設定
     *
     * @param l
     *         {@link Animation.AnimationListener}
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation callback(Animator.AnimatorListener l) {
        mAnimatorListener = l;
        return this;
    }

    public void start() {
        AnimatorSet animatorSet = prepare();
        if (mAnimatorListener != null) {
            animatorSet.addListener(mAnimatorListener);
        }
        animatorSet.start();
    }

    protected View getView() {
        return mView01;
    }

    protected int getDuration() {
        return mDuration;
    }

    protected int getStartOffset() {
        return mStartOffset;
    }

    protected int getRepeatCount() {
        return mRepeatCount;
    }

    public Animator.AnimatorListener getAnimatorListener() {
        return mAnimatorListener;
    }

    /**
     * Animationデフォルト設定
     *
     * @param objectAnimator
     *         {@link Animation}
     */
    public void setDefaultObjectAnimator(ObjectAnimator objectAnimator) {
        objectAnimator.setDuration(mDuration);
        objectAnimator.setStartDelay(mStartOffset);
        objectAnimator.setRepeatCount(mRepeatCount);
        objectAnimator.setInterpolator(mInterpolator);
    }

    /**
     * AnimatorSetデフォルト設定
     *
     * @param animatorSet
     *         {@link AnimatorSet}
     */
    public void setDefaultAnimatorSet(AnimatorSet animatorSet) {
        animatorSet.setDuration(mDuration);
        animatorSet.setStartDelay(mStartOffset);
        animatorSet.setInterpolator(mInterpolator);
    }
}