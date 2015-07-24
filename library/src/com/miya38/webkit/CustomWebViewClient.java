package com.miya38.webkit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.miya38.R;
import com.miya38.utils.AplUtils;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.ViewHelper;

import java.util.List;

/**
 * カスタムWebViewClient
 *
 * @author y-miyazaki
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
    private Activity mActivity;
    /** Fragment */
    private Fragment mFragment;
    /** ホワイトホスト名 */
    private List<String> mWhiteHosts;

    /**
     * コンストラクタ
     *
     * @param activity
     *         {@link Activity}
     */
    public CustomWebViewClient(@NonNull final Activity activity) {
        super();
        mActivity = activity;
    }

    /**
     * コンストラクタ
     *
     * @param fragment
     *         {@link Fragment}
     */
    public CustomWebViewClient(@NonNull final Fragment fragment) {
        super();
        mFragment = fragment;
    }

    @Override
    public final boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        LogUtils.d(TAG, "shouldOverrideUrlLoading url = %s", url);
        if (isCallback(view)) {
            // ------------------------------------------------------------
            // mailtoチェック
            // ------------------------------------------------------------
            if (url.startsWith(PROTOCOL_MAILTO)) {
                final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                mActivity.startActivity(intent);
                view.reload();
                return true;
            }
            // ------------------------------------------------------------
            // ホワイトホストチェック
            // ------------------------------------------------------------
            else if (!isWhiteHosts(url)) {
                onReceivedWhiteHostsError(view, url);
                return true;
            }

            return shouldOverrideUrlLoadingCustom(view, url);
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * カスタマイズされたshouldOverrideUrlLoading
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param url
     *         The url to be loaded.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     */
    public boolean shouldOverrideUrlLoadingCustom(final WebView view, final String url) {
        return false;
    }

    @Override
    public final void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
        LogUtils.d(TAG, "onPageStarted: url = %s", url);
        if (isCallback(view)) {
            onPageStartedCustom(view, url, favicon);
        }
    }

    /**
     * カスタマイズされたonPageStarted
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param url
     *         The url to be loaded.
     * @param favicon
     *         The favicon for this page if it already exists in the
     *         database.
     * @return True if the host application wants to leave the current WebView
     * and handle the url itself, otherwise return false.
     */
    public void onPageStartedCustom(final WebView view, final String url, final Bitmap favicon) {
    }

    @Override
    public final void onPageFinished(final WebView view, final String url) {
        LogUtils.d(TAG, "onPageFinished: url = %s", url);
        if (isCallback(view)) {
            onPageFinishedCustom(view, url);
        }
    }

    /**
     * カスタマイズされたonPageFinished
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param url
     *         The url to be loaded.
     */
    public void onPageFinishedCustom(final WebView view, final String url) {
    }

    @Override
    public final void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
        LogUtils.d(TAG, "onReceivedError: errorCode = %d / description = %s / failingUrl = %s", errorCode, description, failingUrl);
        if (isCallback(view)) {
            onReceivedErrorCustom(view, errorCode, description, failingUrl);
        }
    }

    /**
     * カスタマイズされたonReceivedError
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param errorCode
     *         The error code corresponding to an ERROR_* value.
     * @param description
     *         A String describing the error.
     * @param failingUrl
     *         The url that failed to load.
     */
    public void onReceivedErrorCustom(final WebView view, final int errorCode, final String description, final String failingUrl) {
    }

    @Override
    public final void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
        LogUtils.d(TAG, "onReceivedSslError");
        if (isCallback(view)) {
            onReceivedSslErrorCustom(view, handler, error);
        } else {
            super.onReceivedSslError(view, handler, error);
        }
    }

    /**
     * カスタマイズされたonReceivedSslError
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param handler
     *         An SslErrorHandler object that will handle the user's
     *         response.
     * @param error
     *         The SSL error object.
     */
    public void onReceivedSslErrorCustom(final WebView view, final SslErrorHandler handler, final SslError error) {
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public final void onReceivedHttpAuthRequest(final WebView view, final HttpAuthHandler handler, final String host, final String realm) {
        LogUtils.d(TAG, "onReceivedHttpAuthRequest");
        if (isCallback(view)) {
            onReceivedHttpAuthRequestCustom(view, handler, host, realm);
        } else {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

    /**
     * @param view
     *         the WebView that is initiating the callback
     * @param handler
     *         the HttpAuthHandler used to set the WebView's response
     * @param host
     *         the host requiring authentication
     * @param realm
     *         the realm for which authentication is required
     * @see WebView#getHttpAuthUsernamePassword
     */
    public void onReceivedHttpAuthRequestCustom(final WebView view, final HttpAuthHandler handler, final String host, final String realm) {
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
     *         WebView
     * @param handler
     *         {@link HttpAuthHandler}
     * @param host
     *         ホスト
     * @param realm
     *         the realm for which authentication is required
     */
    public void showHttpAuthDialog(final WebView webView, final HttpAuthHandler handler, final String host, final String realm) {
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
        } else {
            return false;
        }
        if (view == null || view.getContext() == null) {
            return false;
        }
        return true;
    }

    /**
     * ホワイトホストエラー
     * <p>
     * {@link CustomWebViewClient#setWhiteHosts(List)}メソッドで指定されたホワイトホスト以外のURLが設定された場合に返却される。
     * </p>
     *
     * @param view
     *         The WebView that is initiating the callback.
     * @param url
     *         The url to be loaded.
     */
    public void onReceivedWhiteHostsError(WebView view, String url) {
    }

    /**
     * ホワイトリスト設定
     * <p>
     * 特定のホストしか遷移させたくない場合に使用する。
     * 設定後に本メソッドで指定したURLのホスト以外にアクセスすると{@link CustomWebViewClient#onReceivedWhiteHostsError}が呼ばれる。
     * 遷移先などのハンドリングを停止するため、自らハンドリングしなければならない。
     * 本メソッドでホワイトリストを指定しない場合は、通常通りに処理が行われる。
     * </p>
     *
     * @param whiteHosts
     *         ホワイトリスト対象のホストリスト
     */
    public void setWhiteHosts(List<String> whiteHosts) {
        mWhiteHosts = whiteHosts;
    }

    /**
     * ホワイトリストチェック
     * <p>
     * ホワイトリストドメインの場合はtrueを返却する
     * </p>
     *
     * @param url
     *         URL
     * @return true:ホワイトリスト or URL指定無し<br>
     * false:ホワイトリスト外
     */
    private boolean isWhiteHosts(String url) {
        if (url == null) {
            return true;
        } else {
            final Uri uri = Uri.parse(url);
            final String host = uri.getHost();
            if (StringUtils.isEmpty(host)) {
                return true;
            }
            // 念のためホワイトリストの有無をチェックするが、存在しない場合は、チェックを敢えてスルーする。
            if (!CollectionUtils.isNullOrEmpty(mWhiteHosts)) {
                for (final String whiteHost : mWhiteHosts) {
                    if (host.endsWith(whiteHost)) {
                        return true;
                    }
                }
            } else {
                // 取れていない場合は最悪正常として処理を行う。
                return true;
            }
        }
        return false;
    }
}
