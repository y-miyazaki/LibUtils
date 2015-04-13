package com.miya38.utils;

import android.app.Activity;
import android.view.View;

/**
 * ActionBarに関するユーティリティクラス
 * support v7に対応ですのでご注意ください。
 *
 * @author y-miyazaki
 */
public final class ActionBarUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ActionBarUtils() {
    }

    /**
     * アニメーションなしでActionBarを閉じる
     *
     * @param activity
     *            {@link Activity}
     */
    public static void hide(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int resId;
        resId = activity.getResources().getIdentifier("action_bar_container", "id", activity.getPackageName());
        if (resId != 0) {
            decorView.findViewById(resId).setVisibility(View.GONE);
        }
    }

    /**
     * アニメーションなしでActionBarを開く
     *
     * @param activity
     *            {@link Activity}
     */
    public static void show(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int resId;
        resId = activity.getResources().getIdentifier("action_bar_container", "id", activity.getPackageName());
        if (resId != 0) {
            decorView.findViewById(resId).setVisibility(View.VISIBLE);
        }
    }
}
