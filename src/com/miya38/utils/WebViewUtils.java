package com.miya38.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * WebViewユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class WebViewUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private WebViewUtils() {
    }

    /**
     * ユーザエージェント取得
     * 
     * @param context
     *            Context
     * @return ユーザエージェント
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getUserAgent(final Context context) {
        // API17以上
        if (AplUtils.hasJellyBeanMR1()) {
            return WebSettings.getDefaultUserAgent(context);
        }
        // API17未満
        else {
            try {
                final Class<?> webSettingsClassicClass = Class.forName("android.webkit.WebSettingsClassic");
                final Constructor<?> constructor = webSettingsClassicClass.getDeclaredConstructor(Context.class, Class.forName("android.webkit.WebViewClassic"));
                constructor.setAccessible(true);
                final Method method = webSettingsClassicClass.getMethod("getUserAgentString");
                return (String) method.invoke(constructor.newInstance(context, null));
            } catch (final Exception e) {
                return new WebView(context).getSettings().getUserAgentString();
            }
        }
    }
}
