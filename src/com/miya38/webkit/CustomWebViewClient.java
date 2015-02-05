package com.miya38.webkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.miya38.R;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;

/**
 * カスタムWebViewClient
 *
 * @author y-miyazaki
 *
 */
public class CustomWebViewClient extends WebViewClient {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = CustomWebViewClient.class.getSimpleName();
    /** TAG */
    private static final String PROTOCOL_MAILTO = "mailto:";
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** Activity */
    private final Activity mActivity;

    /**
     * コンストラクタ
     *
     * @param activity
     *            Activity
     */
    public CustomWebViewClient(final Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        LogUtils.d(TAG, "shouldOverrideUrlLoading url = %s", url);
        // ------------------------------------------------------------
        // mailtoチェック
        // ------------------------------------------------------------
        if (url.startsWith(PROTOCOL_MAILTO)) {
            final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            mActivity.startActivity(intent);
            view.reload();
            return true;
        }
        return false;
    }

    @Override
    public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtils.d(TAG, "onPageStarted: url = %s", url);
    }

    @Override
    public void onPageFinished(final WebView view, final String url) {
        super.onPageFinished(view, url);
        LogUtils.d(TAG, "onPageFinished: url = %s", url);
    }

    @Override
    public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
        LogUtils.d(TAG, "onReceivedError: errorCode = %d / description = %s / failingUrl = %s", errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
        super.onReceivedSslError(view, handler, error);
        LogUtils.d(TAG, "onReceivedSslError");
    }

    @Override
    public void onReceivedHttpAuthRequest(final WebView view, final HttpAuthHandler handler, final String host, final String realm) {
        LogUtils.d(TAG, "onReceivedHttpAuthRequest");

        String userName = null;
        String userPass = null;

        if (handler.useHttpAuthUsernamePassword() && view != null) {
            final String[] haup = view.getHttpAuthUsernamePassword(host, realm);
            if (haup != null && haup.length == 2) {
                userName = haup[0];
                userPass = haup[1];
            }
        }
        if (userName != null && userPass != null) {
            handler.proceed(userName, userPass);
        } else {
            showHttpAuthDialog(view, handler, host, realm);
        }
    }

    /**
     * Basic認証ダイアログ表示
     *
     * @param webView
     *            WebView
     * @param handler
     *            {@link HttpAuthHandler}
     * @param host
     *            ホスト
     * @param realm
     *            the realm for which authentication is required
     */
    private void showHttpAuthDialog(final WebView webView, final HttpAuthHandler handler, final String host, final String realm) {
        // Activityが死んでいる場合は、ダイアログを出さない。
        if (mActivity.isFinishing() || mActivity.isDestroyed()) {
            return;
        }
        final LayoutInflater layoutInflator = LayoutInflater.from(mActivity);
        final View view = layoutInflator.inflate(R.layout.common_dialog_basic, null);

        final AlertDialog.Builder mHttpAuthDialog = new AlertDialog.Builder(mActivity);
        mHttpAuthDialog.setTitle(mActivity.getString(R.string.common_dialog_basic_message, host, realm)).setView(view).setCancelable(true);
        mHttpAuthDialog.setPositiveButton(mActivity.getString(R.string.common_dialog_basic_button_login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton) {
                final EditText etUserName = ViewHelper.findView(view, R.id.CustomEditText01);
                final String userName = etUserName.getText().toString();
                final EditText etUserPass = ViewHelper.findView(view, R.id.CustomEditText02);
                final String userPass = etUserPass.getText().toString();

                webView.setHttpAuthUsernamePassword(host, realm, userName, userPass);
                handler.proceed(userName, userPass);
                dialog.dismiss();
            }
        });
        mHttpAuthDialog.setNegativeButton(mActivity.getString(R.string.common_dialog_basic_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton) {
                handler.cancel();
                dialog.dismiss();
            }
        });

        // backキー
        mHttpAuthDialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    handler.cancel();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        mHttpAuthDialog.create().show();
    }

    @Override
    public void onScaleChanged(final WebView view, final float oldScale, final float newScale) {
        if (view != null) {
            view.invalidate();
        }
    }
}
