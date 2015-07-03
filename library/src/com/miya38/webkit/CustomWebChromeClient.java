package com.miya38.webkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;

/**
 * カスタムWebViewClient
 *
 * @author y-miyazaki
 */
public class CustomWebChromeClient extends WebChromeClient {
    /** TAG */
    private static final String TAG = CustomWebChromeClient.class.getSimpleName();

    /** Activity */
    private Activity mActivity;
    /** Fragment */
    private Fragment mFragment;

    /**
     * コンストラクタ
     *
     * @param activity
     *         {@link Activity}
     */
    public CustomWebChromeClient(@NonNull final Activity activity) {
        super();
        if (activity == null) {
            throw new IllegalArgumentException("must be set activity.");
        }
        mActivity = activity;
    }

    /**
     * コンストラクタ
     *
     * @param fragment
     *         {@link Fragment}
     */
    public CustomWebChromeClient(@NonNull final Fragment fragment) {
        super();
        if (fragment == null) {
            throw new IllegalArgumentException("must be set activity.");
        }
        mFragment = fragment;
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
     *         {@link WebView}
     * @param title
     *         タイトル
     */
    public void onReceivedTitleCustom(final WebView view, final String title) {
    }

    /**
     * コールバックするかをチェックする。
     *
     * @param view
     *         {@link WebView}
     * @return true:コールバックする/false:コールバックしない
     */
    @SuppressLint("NewApi")
    private boolean isCallback(final WebView view) {
        if (mActivity != null) {
            if (mActivity.isFinishing()) {
                return false;
            }
            if (AplUtils.hasJellyBeanMR1()) {
                if (mActivity.isDestroyed()) {
                    return false;
                }
            }
        } else if (mFragment != null) {
            if (mFragment.getView() == null || mFragment.isDetached() || mFragment.isRemoving()) {
                return false;
            }
        }
        if (view == null || view.getContext() == null) {
            return false;
        }
        return true;
    }
}
