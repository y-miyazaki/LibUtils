package com.miya38.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Cookieユーティリティクラス
 * 
 * @author y-miyazaki
 * 
 */
public final class CookieUtils {
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private CookieUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     * 
     * @param context
     *            {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    /**
     * Cookie値全取得
     * 
     * @param url
     *            URL
     * @return 指定したurlのCookie値を返却
     */
    @SuppressWarnings("deprecation")
    public static String get(final String url) {
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.createInstance(sContext);
        }
        return StringUtils.trim(CookieManager.getInstance().getCookie(url));// 文字列でCookieを取得
    }

    /**
     * Cookie値取得
     * 
     * @param url
     *            URL
     * @param key
     *            Cookieキー
     * @return 指定したkeyのCookie値を返却
     */
    @SuppressWarnings("deprecation")
    public static String getValue(final String url, final String key) {
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.createInstance(sContext);
        }
        String cookie = StringUtils.trim(CookieManager.getInstance().getCookie(url));// 文字列でCookieを取得
        if (StringUtils.isEmpty(cookie)) {
            return null;
        }

        if (!cookie.endsWith(";")) {
            cookie = StringUtils.appendBuilder(cookie, ";");
        }
        final Pattern cookiePattern = Pattern.compile("([^= ]+)=\"?([^\\;]*?)\"?;\\s?");
        final Matcher matcher = cookiePattern.matcher(cookie);

        while (matcher.find()) {
            final String cookieKey = matcher.group(1);
            final String cookieValue = matcher.group(2);
            if (StringUtils.equals(cookieKey, key)) {
                return StringUtils.trim(Uri.decode(cookieValue));
            }
        }
        return null;
    }

    /**
     * Cookie値設定
     * 
     * @param url
     *            URL
     * @param value
     *            クッキー
     */
    @SuppressWarnings("deprecation")
    public static void setValue(final String url, final String value) {
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.createInstance(sContext);
        }
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        cookieManager.setCookie(url, value);
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * WebViewの指定したURLのCookieを全削除
     * 
     * @param url
     *            URL
     */
    @SuppressWarnings("deprecation")
    public static void removeAll(final String url) {
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.createInstance(sContext);
        }
        final CookieManager cookieManager = CookieManager.getInstance();
        final String cookie = cookieManager.getCookie(url);// 文字列でCookieを取得
        if (cookie != null) {
            final String[] oneCookie = cookie.split(";");
            for (String pair : oneCookie) {
                pair = StringUtils.trim(pair);
                final String[] set = pair.split("=");
                set[0] = StringUtils.trim(set[0]);
                cookieManager.setCookie(url, set[0] + "=;");
            }
        }
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * WebViewのCookieを全削除
     */
    @SuppressWarnings("deprecation")
    public static void removeAll() {
        if (!AplUtils.hasLollipop()) {
            CookieSyncManager.createInstance(sContext);
        }
        CookieManager.getInstance().removeAllCookie();
    }
}
