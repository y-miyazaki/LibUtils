package com.miya38.animation.flip;


import android.view.View;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * フリップ縦回転からView変更クラス
 */
public class FlipRotateVerticalDownTo extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "rotationX", 0, -90);
        objectAnimator01.setDuration(mDuration);

        final ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        objectAnimator02.setDuration(1);
        objectAnimator02.setStartDelay(mDuration);

        final ObjectAnimator objectAnimator03 = ObjectAnimator.ofFloat(mView02, "rotationX", 90, 0);
        objectAnimator03.setDuration(mDuration);
        objectAnimator03.setStartDelay(mDuration);

        final ObjectAnimator objectAnimator04 = ObjectAnimator.ofFloat(mView02, "alpha", 1, 0);
        objectAnimator04.setDuration(1);
        objectAnimator04.setStartDelay(0);

        final ObjectAnimator objectAnimator05 = ObjectAnimator.ofFloat(mView02, "alpha", 0, 1);
        objectAnimator05.setDuration(1);
        objectAnimator05.setStartDelay(mDuration);

        animatorSet.playTogether(
                objectAnimator01, objectAnimator02, objectAnimator03, objectAnimator04, objectAnimator05
        );
        return animatorSet;
    }

    @Override
    public AbstractAnimation view(final View v) {
        throw new IllegalArgumentException("you should use view(from, to);");
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
}
