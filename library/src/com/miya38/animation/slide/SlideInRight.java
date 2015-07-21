package com.miya38.animation.slide;

import android.view.ViewGroup;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * スライドイン（右）クラス
 */
public class SlideInRight extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        ViewGroup parent = (ViewGroup)mView01.getParent();
        int distance = parent.getWidth() - mView01.getLeft();

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "translationX", distance, 0);
        setDefaultObjectAnimator(objectAnimator01);
        animatorSet.play(objectAnimator01);
        return animatorSet;
    }
}
