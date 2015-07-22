package com.miya38.animation;

import com.miya38.animation.blink.Blink;
import com.miya38.animation.bounce.BounceInDown;
import com.miya38.animation.bounce.BounceInLeft;
import com.miya38.animation.bounce.BounceInRight;
import com.miya38.animation.bounce.BounceInUp;
import com.miya38.animation.fade.FadeIn;
import com.miya38.animation.fade.FadeOut;
import com.miya38.animation.flip.FlipRotateHorizontalLeft;
import com.miya38.animation.flip.FlipRotateHorizontalLeftTo;
import com.miya38.animation.flip.FlipRotateHorizontalRight;
import com.miya38.animation.flip.FlipRotateHorizontalRightTo;
import com.miya38.animation.flip.FlipRotateVerticalDown;
import com.miya38.animation.flip.FlipRotateVerticalDownTo;
import com.miya38.animation.flip.FlipRotateVerticalUp;
import com.miya38.animation.flip.FlipRotateVerticalUpTo;
import com.miya38.animation.scale.ScaleInDown;
import com.miya38.animation.scale.ScaleInUp;
import com.miya38.animation.scale.ScaleOutDown;
import com.miya38.animation.scale.ScaleOutUp;
import com.miya38.animation.slide.SlideInDown;
import com.miya38.animation.slide.SlideInLeft;
import com.miya38.animation.slide.SlideInRight;
import com.miya38.animation.slide.SlideInUp;
import com.miya38.animation.slide.SlideOutDown;
import com.miya38.animation.slide.SlideOutLeft;
import com.miya38.animation.slide.SlideOutRight;
import com.miya38.animation.slide.SlideOutUp;

/**
 * Animation系のユーティリティクラス
 *
 * @author y-miyazaki
 */
public final class Animations {
    private static final String TAG = "Animations";

    /**
     * ブリンクアニメーションで表示する。
     */
    public static Blink blink() {
        return new Blink();
    }

    /**
     * バウンスアニメーションで表示する。
     */
    public static BounceInDown bounceInDown() {
        return new BounceInDown();
    }

    /**
     * バウンスアニメーションで表示する。
     */
    public static BounceInLeft bounceInLeft() {
        return new BounceInLeft();
    }

    /**
     * バウンスアニメーションで表示する。
     */
    public static BounceInRight bounceInRight() {
        return new BounceInRight();
    }

    /**
     * バウンスアニメーションで表示する。
     */
    public static BounceInUp bounceInUp() {
        return new BounceInUp();
    }

    /**
     * アルファアニメーションで表示する。
     */
    public static FadeIn fadeIn() {
        return new FadeIn();
    }

    /**
     * アルファアニメーションで表示する。
     */
    public static FadeOut fadeOut() {
        return new FadeOut();
    }

    /**
     * フリップ左回転アニメーションで表示する。
     */
    public static FlipRotateHorizontalLeft flipRotateHorizontalLeft() {
        return new FlipRotateHorizontalLeft();
    }

    /**
     * フリップ左回転からView変更アニメーションで表示する。
     */
    public static FlipRotateHorizontalLeftTo flipRotateHorizontalLeftTo() {
        return new FlipRotateHorizontalLeftTo();
    }

    /**
     * フリップ右回転アニメーションで表示する。
     */
    public static FlipRotateHorizontalRight flipRotateHorizontalRight() {
        return new FlipRotateHorizontalRight();
    }

    /**
     * フリップ右回転からView変更アニメーションで表示する。
     */
    public static FlipRotateHorizontalRightTo flipRotateHorizontalRightTo() {
        return new FlipRotateHorizontalRightTo();
    }

    /**
     * フリップ縦回転アニメーションで表示する。
     */
    public static FlipRotateVerticalDown flipRotateVerticalDown() {
        return new FlipRotateVerticalDown();
    }

    /**
     * フリップ縦回転からView変更アニメーションで表示する。
     */
    public static FlipRotateVerticalDownTo flipRotateVerticalDownTo() {
        return new FlipRotateVerticalDownTo();
    }

    /**
     * フリップ縦回転アニメーションで表示する。
     */
    public static FlipRotateVerticalUp flipRotateVerticalUp() {
        return new FlipRotateVerticalUp();
    }

    /**
     * フリップ縦回転からView変更アニメーションで表示する。
     */
    public static FlipRotateVerticalUpTo flipRotateVerticalUpTo() {
        return new FlipRotateVerticalUpTo();
    }

    /**
     * スケールインアニメーションで表示する。
     */
    public static ScaleInDown scaleInDown() {
        return new ScaleInDown();
    }

    /**
     * スケールインアニメーションで表示する。
     */
    public static ScaleInUp scaleInUp() {
        return new ScaleInUp();
    }

    /**
     * スケールアウトアニメーションで表示する。
     */
    public static ScaleOutDown scaleOutDown() {
        return new ScaleOutDown();
    }

    /**
     * スケールアウトアニメーションで表示する。
     */
    public static ScaleOutUp scaleOutUp() {
        return new ScaleOutUp();
    }

    /**
     * スライドイン(下)アニメーションで表示する。
     */
    public static SlideInDown slideInDown() {
        return new SlideInDown();
    }

    /**
     * スライドイン(左)アニメーションで表示する。
     */
    public static SlideInLeft slideInLeft() {
        return new SlideInLeft();
    }

    /**
     * スライドイン(右)アニメーションで表示する。
     */
    public static SlideInRight slideInRight() {
        return new SlideInRight();
    }

    /**
     * スライドイン(上)アニメーションで表示する。
     */
    public static SlideInUp slideInUp() {
        return new SlideInUp();
    }


    /**
     * スライドアウト(下)アニメーションで表示する。
     */
    public static SlideOutDown slideOutDown() {
        return new SlideOutDown();
    }

    /**
     * スライドアウト(左)アニメーションで表示する。
     */
    public static SlideOutLeft slideOutLeft() {
        return new SlideOutLeft();
    }

    /**
     * スライドアウト(右)アニメーションで表示する。
     */
    public static SlideOutRight slideOutRight() {
        return new SlideOutRight();
    }

    /**
     * スライドアウト(上)アニメーションで表示する。
     */
    public static SlideOutUp slideOutUp() {
        return new SlideOutUp();
    }
}
