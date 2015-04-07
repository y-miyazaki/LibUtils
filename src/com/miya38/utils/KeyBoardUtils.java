package com.miya38.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * キーボードユーティリティクラス
 * 
 * @author y-miyazaki
 * 
 */
public final class KeyBoardUtils {

    /**
     * キーボードユーティリティ
     */
    private KeyBoardUtils() {

    }

    /**
     * キーボード非表示
     * 
     * @param context
     *            Context
     * @param view
     *            対応View
     */
    public static void hide(final Context context, final View view) {
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    /**
     * キーボード非表示
     * 
     * @param activity
     *            Activity
     */
    public static void hide(final Activity activity) {
        hide(activity, activity.getCurrentFocus());
    }

    /**
     * 非表示
     * 
     * @param activity
     *            Activity
     */
    public static void initHidden(final Activity activity) {
        activity.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * キーボード表示
     * 
     * @param context
     *            Context
     * @param editText
     *            EditText
     */
    public static void show(final Context context, final EditText editText) {
        show(context, editText, 0);
    }

    /**
     * キーボード表示
     * 
     * @param context
     *            Context
     * @param editText
     *            EditText
     * @param delayTime
     *            遅延時間(ms)
     */
    public static void show(final Context context, final EditText editText, final int delayTime) {
        final Runnable showKeyboardDelay = new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    final InputMethodManager imm = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        };
        new Handler().postDelayed(showKeyboardDelay, delayTime);
    }
}