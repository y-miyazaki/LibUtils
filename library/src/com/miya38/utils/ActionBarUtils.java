package com.miya38.utils;

import android.app.Activity;
import android.view.View;

/**
 * ActionBarに関するユーティリティクラス
 * <p>
 * support v7に対応ですのでご注意ください。<br>
 * デフォルトでアニメーションでのhide/showを外したものとなります。
 * 本クラス内のshow/hideを使用した場合はActionBar.sShowingが正常に返らなくなりますので必ず本ユーティリティを使用することとしてください。
 * </p>
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
     *         {@link Activity}
     */
    public static void hide(Activity activity) {
        View view = getActionBarView(activity);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * アニメーションなしでActionBarを開く
     *
     * @param activity
     *         {@link Activity}
     */
    public static void show(Activity activity) {
        View view = getActionBarView(activity);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ActionBarの表示状態
     *
     * @param activity
     *         {@link Activity}
     * @return true:表示中/false:非表示中
     */
    public static boolean isShowing(Activity activity) {
        View view = getActionBarView(activity);
        if (view != null && view.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    /**
     * ActionBarの表示状態
     *
     * @param activity
     *         {@link Activity}
     * @return ActionBarのRootView
     * Activityがnullの場合、ActionBarが存在しない場合はnullを返却する。
     */
    private static View getActionBarView(Activity activity) {
        if (activity != null) {
            View decorView = activity.getWindow().getDecorView();
            int resId;
            resId = activity.getResources().getIdentifier("action_bar_container", "id", activity.getPackageName());
            if (resId != 0) {
                return decorView.findViewById(resId);
            }
        }
        return null;
    }
}
