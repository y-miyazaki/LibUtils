package com.miya38.animation.flip;


import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * フリップ左回転クラス
 */
public class FlipRotateYLeft extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mView01, "rotationY", 0, 360);
        setDefaultObjectAnimator(objectAnimator);
        animatorSet.play(objectAnimator);
        return animatorSet;
    }
}
