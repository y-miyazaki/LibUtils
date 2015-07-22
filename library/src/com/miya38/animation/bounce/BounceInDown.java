package com.miya38.animation.bounce;

import android.view.animation.BounceInterpolator;

import com.miya38.animation.slide.SlideInDown;

/**
 * バウンスイン（下）クラス
 */
public class BounceInDown extends SlideInDown {
    /**
     * コンストラクタ
     */
    public BounceInDown() {
        interpolator(new BounceInterpolator());
    }
}
