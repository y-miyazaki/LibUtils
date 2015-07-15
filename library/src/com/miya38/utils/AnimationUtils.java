package com.miya38.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Animation系のユーティリティクラス
 *
 * @author y-miyazaki
 */
public final class AnimationUtils {
    private static final String TAG = "AnimationUtils";


    /**
     * アルファアニメーションで表示する。
     *
     * @param v
     *         View
     * @param duration
     *         アニメーション時間
     */
    public static void setAlphaInAnimation(final View v, final int duration) {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        v.startAnimation(animation);
    }

    /**
     * アルファアニメーションで非表示にする。
     *
     * @param v
     *         View
     * @param duration
     *         アニメーション時間
     */
    public static void setAlphaOutAnimation(final View v, final int duration) {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        v.startAnimation(animation);
    }

    /**
     * 文字を指定回数で点滅させる
     *
     * @param v
     *         View
     * @param duration
     *         点滅間隔
     * @param blinkCount
     *         点滅回数
     */
    public static void setBlink(final View v, final int duration, final int blinkCount) {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(duration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(blinkCount);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        v.startAnimation(alpha);
    }

    /**
     * 無限に文字を点滅させる
     *
     * @param v
     *         View
     * @param duration
     *         点滅間隔
     */
    public static void setBlink(final View v, final int duration) {
        final AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(duration);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(Animation.INFINITE);
        alpha.setFillEnabled(true);
        alpha.setFillAfter(true);
        alpha.setFillBefore(false);
        v.startAnimation(alpha);
    }
}
