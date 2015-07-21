package com.miya38.animation.slide;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * スライドイン（下）クラス
 */
public class SlideInDown extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        int distance = mView01.getTop() + mView01.getHeight();

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "translationY", -distance, 0);
        setDefaultObjectAnimator(objectAnimator01);
        animatorSet.play(objectAnimator01);
        return animatorSet;
    }
}
