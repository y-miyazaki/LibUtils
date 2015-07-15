package com.miya38.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * コネクションユーティリティークラス
 *
 * @author y-miyazaki
 */
public final class ConnectionUtils {
    /** Context */
    private static Context sContext;
    /** 基本の文字コード */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ConnectionUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param context
     *         {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    /**
     * ホスト名取得
     *
     * @param url
     *         URL
     * @return ホスト名
     */
    public static String getHost(final String url) {
        try {
            return new URL(url).getHost();
        } catch (final MalformedURLException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * パス名取得
     *
     * @param url
     *         URL
     * @return ホスト名
     */
    public static String getPath(final String url) {
        try {
            return new URL(url).getPath();
        } catch (final MalformedURLException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * クエリーを除いたURLを取得
     *
     * @param url
     *         URL
     * @return クエリーを除いたURL
     */
    public static String getDeleteQuery(@NonNull final String url) {
        try {
            return url.split("\\?")[0];
        } catch (final Exception e) {
            // 握りつぶす
        }
        return url;
    }

    /**
     * URLからクエリーのMapを取得
     *
     * @param url
     *         URL
     * @return クエリーのMAP
     */
    public static Map<String, String> getQuery(@NonNull final String url) {
        final HashMap<String, String> map = new HashMap<String, String>();
        try {
            final List<NameValuePair> parameters = URLEncodedUtils.parse(new URI(url), DEFAULT_CHARSET);
            for (final NameValuePair p : parameters) {
                map.put(p.getName(), p.getValue());
            }
        } catch (final IllegalArgumentException e) {
            // 握りつぶす
        } catch (final URISyntaxException e) {
            // 握りつぶす
        }
        return map;
    }

    /**
     * URL・クエリーの組み合わせを取得
     *
     * @param url
     *         URL
     * @param query
     *         クエリーmap
     * @return URL
     */
    public static String getUrl(@NonNull final String url, final Map<String, String> query) {
        // URL設定
        final StringBuilder stringBuilderUrl = new StringBuilder();

        try {
            if (query != null && !query.isEmpty()) {
                stringBuilderUrl.append('?');
                final Set<String> keySet = query.keySet();
                final Iterator<String> keyIte = keySet.iterator();
                int count = 0;
                while (keyIte.hasNext()) {
                    if (count > 0) {
                        stringBuilderUrl.append('&');
                    }
                    final String key = keyIte.next();
                    final String value = query.get(key);
                    stringBuilderUrl.append(key);
                    stringBuilderUrl.append('=');
                    if (value != null) {
                        stringBuilderUrl.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                    }
                    count++;
                }
            }
        } catch (final UnsupportedEncodingException e) {
            // 握りつぶす
        }
        return stringBuilderUrl.insert(0, url).toString();
    }

    /**
     * クエリーパラメータ生成
     *
     * @param query
     *         クエリーMap
     * @return クエリーパラメータ<br>
     * ex) name1=value1&name2=value2
     */
    public static String getQuery(final Map<String, String> query) {
        try {
            // URL設定
            final StringBuilder queryParameter = new StringBuilder();

            if (query != null && !query.isEmpty()) {
                final Set<String> keySet = query.keySet();
                final Iterator<String> keyIte = keySet.iterator();
                int count = 0;
                while (keyIte.hasNext()) {
                    if (count > 0) {
                        queryParameter.append('&');
                    }
                    final String key = keyIte.next();
                    final String value = query.get(key);
                    queryParameter.append(key);
                    queryParameter.append('=');
                    if (value != null) {
                        queryParameter.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                    }
                    count++;
                }
            }
            return queryParameter.toString();
        } catch (final UnsupportedEncodingException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * ボディー設定
     *
     * @param body
     *         ボディ
     * @return キーバリューのペア
     */
    public static List<NameValuePair> getParams(final Map<String, String> body) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();

        if (body != null) {
            final Set<String> keySet = body.keySet();
            final Iterator<String> keyIte = keySet.iterator();
            while (keyIte.hasNext()) {
                final String key = keyIte.next();
                final String value = body.get(key);
                params.add(new BasicNameValuePair(key, value));
            }
        }

        return params;
    }

    /**
     * gzip判定
     *
     * @param response
     *         レスポンス
     * @return gzip有:true gzip無:false
     */
    public static boolean isGZipHttpResponse(final HttpResponse response) {
        final Header header = response.getEntity().getContentEncoding();
        if (header == null) {
            return false;
        }
        final String value = header.getValue();
        return !TextUtils.isEmpty(value) && value.contains("gzip");
    }

    /**
     * gzipを解凍する
     *
     * @param data
     *         gzipされたコンテンツ
     * @return GZip解凍後のコンテンツ
     * 解凍失敗時には、nullで返却する。
     */
    public byte[] getGZipToContents(final byte[] data) {
        try {
            // ContentTypeが、もしgzipなら解凍する
            final GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(data)));
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                final byte[] buffer = new byte[1024];
                int count;
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                return baos.toByteArray();
            } finally {
                baos.close();
                zis.close();
            }
        } catch (final UnsupportedEncodingException e) {
            // 握りつぶす
        } catch (final IOException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * ネットワークが使用可能かを返却する
     *
     * @return true:ネットワーク使用可能 false:ネットワーク使用不可
     */
    @RequiresPermission(allOf = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE})
    public static boolean hasAvailableNetwork() {
        // システムから接続情報をとってくる
        final ConnectivityManager conMan = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else {
            return networkInfo.isConnected();
        }
    }
}
