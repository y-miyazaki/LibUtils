package com.miya38.animation;

import com.miya38.animation.blink.Blink;
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
import com.miya38.animation.scale.ScaleDownIn;
import com.miya38.animation.scale.ScaleDownOut;
import com.miya38.animation.scale.ScaleUpIn;
import com.miya38.animation.scale.ScaleUpOut;

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
    public static ScaleDownIn scaleDownIn() {
        return new ScaleDownIn();
    }

    /**
     * スケールアウトアニメーションで表示する。
     */
    public static ScaleDownOut scaleDownOut() {
        return new ScaleDownOut();
    }

    /**
     * スケールインアニメーションで表示する。
     */
    public static ScaleUpIn scaleUpIn() {
        return new ScaleUpIn();
    }

    /**
     * スケールアウトアニメーションで表示する。
     */
    public static ScaleUpOut scaleUpOut() {
        return new ScaleUpOut();
    }
}
