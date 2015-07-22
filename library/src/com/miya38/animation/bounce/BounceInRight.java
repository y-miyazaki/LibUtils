package com.miya38.animation.bounce;

import android.view.animation.BounceInterpolator;

import com.miya38.animation.slide.SlideInRight;

/**
 * バウンスイン（右）クラス
 */
public class BounceInRight extends SlideInRight {
    /**
     * コンストラクタ
     */
    public BounceInRight() {
        interpolator(new BounceInterpolator());
    }
}
