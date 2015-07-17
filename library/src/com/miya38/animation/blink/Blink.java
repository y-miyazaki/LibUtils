package com.miya38.animation.blink;

import android.view.animation.Animation;

import com.miya38.animation.AbstractAnimation;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 *
 */
public class Blink extends AbstractAnimation {
    /** リピート回数 */
    private static final int REPEAT_COUNT = 3;

    /**
     * コンストラクタ
     */
    public Blink() {
        repeatCount(REPEAT_COUNT);
    }

    @Override
    protected AnimatorSet prepare() {
        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mView01, "alpha", 0, 1);
        setDefaultObjectAnimator(objectAnimator);
        objectAnimator.setRepeatMode(Animation.REVERSE);
        animatorSet.play(objectAnimator);
        return animatorSet;
    }
}
