package com.miya38.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.miya38.BuildConfig;
import com.miya38.connection.AbstractVolleySetting;
import com.miya38.utils.AplUtils;
import com.miya38.utils.ClipboardUtils;
import com.miya38.utils.ConnectionUtils;
import com.miya38.utils.ContextHelper;
import com.miya38.utils.CookieUtils;
import com.miya38.utils.FileApplicationUtils;
import com.miya38.utils.FileAssetsUtils;
import com.miya38.utils.FileSdCardUtils;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ResourceUtils;
import com.miya38.utils.SharedPreferencesUtils;
import com.miya38.utils.ZipUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 共通アプリケーションクラス
 * <p>
 * ユーティリティクラスの初期化処理を行う。
 * </p>
 *
 * @author y-miyazaki
 */
public abstract class CommonApplication extends Application {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();

    /**
     * 証明書のハッシュ値を取得する。
     *
     * @return ハッシュ値
     */
    protected abstract String getSignatureHash();

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        LogUtils.d(TAG, "onCreate");
        // ---------------------------------------------------------------
        // configure(utils)
        // ---------------------------------------------------------------
        AbstractVolleySetting.configure(getApplicationContext());
        AplUtils.configure(getApplicationContext());
        ConnectionUtils.configure(getApplicationContext());
        ContextHelper.configure(getApplicationContext());
        ClipboardUtils.configure(getApplicationContext());
        CookieUtils.configure(getApplicationContext());
        FileSdCardUtils.configure(getApplicationContext());
        FileApplicationUtils.configure(getApplicationContext());
        FileAssetsUtils.configure(getApplicationContext());
        ImageUtils.configure(getApplicationContext());
        SharedPreferencesUtils.configure(getApplicationContext());
        ResourceUtils.configure(getApplicationContext());
        ZipUtils.configure(getApplicationContext());
        if (!AplUtils.isSignatureHash(getSignatureHash())) {
            // throw new ApplicationException("");
        }

        // ---------------------------------------------------------------
        // Memory Leak Check
        // ---------------------------------------------------------------
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        // Connection pooling/Keep-alive bug対応
        // libcore.io.Streams.readAsciiLine対応
        System.setProperty("http.keepAlive", "false");
    }

    @Override
    public void onTerminate() {
        LogUtils.d(TAG, "onTerminate");
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        LogUtils.d(TAG, "onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        LogUtils.d(TAG, "onLowMemory");
    }

    /**
     * RefWatcher取得
     *
     * @param context
     *         {@link Context}
     * @return {@link RefWatcher}
     */
    public static RefWatcher getRefWatcher(Context context) {
        CommonApplication application = (CommonApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }
}
