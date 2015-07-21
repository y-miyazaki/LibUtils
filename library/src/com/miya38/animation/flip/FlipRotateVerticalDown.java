package com.miya38.animation.flip;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * フリップ縦回転クラス
 */
public class FlipRotateVerticalDown extends AbstractAnimation {
    @Override
    public AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mView01, "rotationX", 0, -360);
        setDefaultObjectAnimator(objectAnimator);
        animatorSet.play(objectAnimator);
        return animatorSet;
    }
}
