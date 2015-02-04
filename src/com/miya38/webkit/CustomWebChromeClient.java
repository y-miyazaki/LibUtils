package com.miya38.webkit;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

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

    /**
     * コンストラクタ
     */
    public CustomWebChromeClient() {
        super();
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogUtils.d(TAG, "onReceivedTitle: title = %s", title);
    }
}
