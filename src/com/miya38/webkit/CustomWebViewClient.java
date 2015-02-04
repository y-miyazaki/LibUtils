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
    /** TAG */
    private static final String TAG = CustomWebViewClient.class.getSimpleName();
    /** Activity */
    private Activity mActivity;

    /**
     * コンストラクタ
     *
     * @param activity
     *            Activity
     */
    public CustomWebViewClient(Activity activity) {
        super();
        mActivity = activity;
    }

    /**
     * コンストラクタ
     *
     * @param activity
     *            Activity
     */
    public CustomWebViewClient(Activity activity, boolean isDebugable) {
        super();
        mActivity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtils.d(TAG, "shouldOverrideUrlLoading url = %s", url);
        // ------------------------------------------------------------
        // mailtoチェック
        // ------------------------------------------------------------
        if (url.substring(0, 7).equals("mailto:")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            mActivity.startActivity(intent);
            view.reload();
            return true;
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogUtils.d(TAG, "onPageStarted: url = %s", url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtils.d(TAG, "onPageFinished: url = %s", url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        LogUtils.d(TAG, "onReceivedError: errorCode = %d / description = %s / failingUrl = %s", errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        LogUtils.d(TAG, "onReceivedSslError");
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, final HttpAuthHandler handler, final String host, final String realm) {
        LogUtils.d(TAG, "onReceivedHttpAuthRequest");

        String userName = null;
        String userPass = null;

        if (handler.useHttpAuthUsernamePassword() && view != null) {
            String[] haup = view.getHttpAuthUsernamePassword(host, realm);
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
     * @param handler
     * @param host
     * @param realm
     */
    private void showHttpAuthDialog(final WebView webView, final HttpAuthHandler handler, final String host, final String realm) {
        LayoutInflater layoutInflator = LayoutInflater.from(mActivity);
        final View view = layoutInflator.inflate(R.layout.common_dialog_basic, null);

        final AlertDialog.Builder mHttpAuthDialog = new AlertDialog.Builder(mActivity);
        mHttpAuthDialog.setTitle(mActivity.getString(R.string.common_dialog_basic_message, host, realm)).setView(view).setCancelable(true);
        mHttpAuthDialog.setPositiveButton(mActivity.getString(R.string.common_dialog_basic_button_login), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText etUserName = ViewHelper.findView(view, R.id.CustomEditText01);
                String userName = etUserName.getText().toString();
                EditText etUserPass = ViewHelper.findView(view, R.id.CustomEditText02);
                String userPass = etUserPass.getText().toString();

                webView.setHttpAuthUsernamePassword(host, realm, userName, userPass);
                handler.proceed(userName, userPass);
                dialog.dismiss();
            }
        });
        mHttpAuthDialog.setNegativeButton(mActivity.getString(R.string.common_dialog_basic_button_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                handler.cancel();
                dialog.dismiss();
            }
        });

        // backキー
        mHttpAuthDialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (view != null) {
            view.invalidate();
        }
    }
}
