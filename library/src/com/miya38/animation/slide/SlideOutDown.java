package com.miya38.animation.slide;

import android.view.ViewGroup;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * スライドアウト（下）クラス
 */
public class SlideOutDown extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        ViewGroup parent = (ViewGroup) mView01.getParent();
        int distance = parent.getHeight() - mView01.getTop();

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mView01, "translationY", 0, distance);
        ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        setDefaultObjectAnimator(objectAnimator01);
        setDefaultObjectAnimator(objectAnimator02);
        animatorSet.playTogether(objectAnimator01, objectAnimator02);
        return animatorSet;
    }
}
