package com.miya38.animation.flip;


import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * フリップ左回転からView変更クラス
 */
public class FlipRotateYLeftTo extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "rotationY", 0, 90);
        objectAnimator01.setDuration(mDuration);

        final ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        objectAnimator02.setDuration(1);
        objectAnimator02.setStartDelay(mDuration);

        final ObjectAnimator objectAnimator03 = ObjectAnimator.ofFloat(mView02, "rotationY", -90, 0);
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
}
