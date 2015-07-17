package com.miya38.animation.fade;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * フェードアウトクラス
 */
public class FadeOut extends AbstractAnimation {
    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mView01, "alpha", 1, 0);
        setDefaultObjectAnimator(objectAnimator);
        animatorSet.play(objectAnimator);
        return animatorSet;
    }
}
