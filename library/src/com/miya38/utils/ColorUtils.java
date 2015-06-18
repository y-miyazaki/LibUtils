package com.miya38.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;

/**
 * カラーユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class ColorUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ColorUtils() {
    }

    /**
     * カラー設定 RGBA値できたものをARGB値のint型で返却する
     * 
     * @param color
     *            RGBA値
     * @return ARGB値を返却する
     */
    public static int setRGBAToARGB(@NonNull final String color) {
        final int red = Integer.parseInt(color.substring(0, 2), 16);
        final int green = Integer.parseInt(color.substring(2, 4), 16);
        final int blue = Integer.parseInt(color.substring(4, 6), 16);
        final int alpha = Integer.parseInt(color.substring(6, 8), 16);
        return Color.argb(alpha, red, green, blue);
    }
}
