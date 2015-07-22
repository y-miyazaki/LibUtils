package com.miya38.animation.bounce;

import android.view.animation.BounceInterpolator;

import com.miya38.animation.slide.SlideInLeft;

/**
 * バウンスイン（左）クラス
 */
public class BounceInLeft extends SlideInLeft {
    /**
     * コンストラクタ
     */
    public BounceInLeft() {
        interpolator(new BounceInterpolator());
    }
}
