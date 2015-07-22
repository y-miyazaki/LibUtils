package com.miya38.animation.bounce;

import android.view.animation.BounceInterpolator;

import com.miya38.animation.slide.SlideInUp;

/**
 * バウンスイン（上）クラス
 */
public class BounceInUp extends SlideInUp {

    /**
     * コンストラクタ
     */
    public BounceInUp() {
        interpolator(new BounceInterpolator());
    }
}
