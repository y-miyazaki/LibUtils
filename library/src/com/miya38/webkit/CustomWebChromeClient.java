package com.miya38.webkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;

/**
 * カスタムWebViewClient
 * 
 * @author y-miyazaki
 * 
 */
public class CustomWebChromeClient extends WebChromeClient {
    /** TAG */
    private static final String TAG = CustomWebChromeClient.class.getSimpleName();

    /** Activity */
    private final Activity mActivity;

    /**
     * コンストラクタ
     * 
     * @param activity
     *            Activity
     */
    public CustomWebChromeClient(final Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public final void onReceivedTitle(final WebView view, final String title) {
        LogUtils.d(TAG, "onReceivedTitle: title = %s", title);
        if (isCallback(view)) {
            onReceivedTitleCustom(view, title);
        }
    }

    /**
     * カスタマイズされたonReceivedTitle
     * 
     * @param view
     *            {@link WebView}
     * @param title
     *            タイトル
     */
    public void onReceivedTitleCustom(final WebView view, final String title) {
    }

    /**
     * コールバックするかをチェックする。
     * 
     * @param view
     *            {@link WebView}
     * @return true:コールバックする/false:コールバックしない
     */
    @SuppressLint("NewApi")
    private boolean isCallback(final WebView view) {
        if (mActivity.isFinishing()) {
            return false;
        }
        if (AplUtils.hasJellyBeanMR1()) {
            if (mActivity.isDestroyed()) {
                return false;
            }
        }
        if (view == null || view.getContext() == null) {
            return false;
        }
        return true;
    }
}
