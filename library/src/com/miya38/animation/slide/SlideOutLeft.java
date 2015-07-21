package com.miya38.animation.slide;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * スライドアウト（左）クラス
 */
public class SlideOutLeft extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "translationX", 0, -mView01.getRight());
        ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        setDefaultObjectAnimator(objectAnimator01);
        setDefaultObjectAnimator(objectAnimator02);
        animatorSet.playTogether(objectAnimator01, objectAnimator02);
        return animatorSet;
    }
}
