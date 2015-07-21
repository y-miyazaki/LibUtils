package com.miya38.animation.scale;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * スケールアウトクラス
 */
public class ScaleOutUp extends AbstractAnimation {
    private float mScaleFrom = 1f;
    private float mScaleTo = 2f;

    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "scaleX", mScaleFrom, mScaleTo);
        ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mView01, "scaleY", mScaleFrom, mScaleTo);
        ObjectAnimator objectAnimator03 = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        setDefaultObjectAnimator(objectAnimator01);
        setDefaultObjectAnimator(objectAnimator02);
        setDefaultObjectAnimator(objectAnimator03);
        animatorSet.playTogether(objectAnimator01, objectAnimator02, objectAnimator03);
        return animatorSet;
    }

    /**
     * スケール値を設定する。
     *
     * @param to
     *         スケールサイズ 1にするとターゲットの同一サイズとなる。
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation scale(float to) {
        mScaleTo = to;
        return this;
    }

    /**
     * スケール値を設定する。
     *
     * @param from
     *         スケールサイズ 1にするとターゲットの同一サイズとなる。
     * @param to
     *         スケールサイズ 1にするとターゲットの同一サイズとなる。
     * @return {@link AbstractAnimation}
     */
    public AbstractAnimation scale(float from, float to) {
        mScaleFrom = from;
        mScaleTo = to;
        return this;
    }
}
