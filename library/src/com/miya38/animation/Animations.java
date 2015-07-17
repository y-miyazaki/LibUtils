package com.miya38.animation;

import com.miya38.animation.blink.Blink;
import com.miya38.animation.fade.FadeIn;
import com.miya38.animation.fade.FadeOut;
import com.miya38.animation.flip.FlipRotateYLeft;
import com.miya38.animation.flip.FlipRotateYLeftTo;
import com.miya38.animation.flip.FlipRotateYRight;
import com.miya38.animation.flip.FlipRotateYRightTo;
import com.miya38.animation.scale.ScaleIn;
import com.miya38.animation.scale.ScaleOut;

/**
 * Animation系のユーティリティクラス
 *
 * @author y-miyazaki
 */
public final class Animations {
    private static final String TAG = "Animations";

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
     * スケールインアニメーションで表示する。
     */
    public static ScaleIn scaleIn() {
        return new ScaleIn();
    }

    /**
     * スケールアウトアニメーションで表示する。
     */
    public static ScaleOut scaleOut() {
        return new ScaleOut();
    }

    /**
     * ブリンクアニメーションで表示する。
     */
    public static Blink blink() {
        return new Blink();
    }

    /**
     * フリップ回転アニメーションで表示する。
     */
    public static FlipRotateYLeft flipRotateLeft() {
        return new FlipRotateYLeft();
    }

    /**
     * フリップ回転アニメーションで表示する。
     */
    public static FlipRotateYRight flipRotateYRight() {
        return new FlipRotateYRight();
    }

    /**
     * フリップ回転アニメーションで表示する。
     */
    public static FlipRotateYLeftTo flipRotateYLeftTo() {
        return new FlipRotateYLeftTo();
    }

    /**
     * フリップ回転アニメーションで表示する。
     */
    public static FlipRotateYRightTo flipRotateYRightTo() {
        return new FlipRotateYRightTo();
    }
}
