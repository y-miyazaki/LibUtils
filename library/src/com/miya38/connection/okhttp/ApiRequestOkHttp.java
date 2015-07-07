package com.miya38.connection.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.miya38.BuildConfig;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.CookieUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * リクエストクラス(OkHttp)
 *
 * @author y-miyazaki
 */
public final class ApiRequestOkHttp {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    private static final String TAG = ApiRequestOkHttp.class.getSimpleName();
    /** Content-Type */
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    /** Content-Type: text/plain */
    private static final String HEADER_CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    /** Content-Type: application/json */
    private static final String HEADER_CONTENT_TYPE_APPLICATION_JSON = "application/json";

    /** コネクションタイムアウト(秒) */
    private static final int CONNECTION_TIMEOUT_SECOND = 20;
    /** リードタイムアウト(秒) */
    private static final int READ_TIMEOUT_SECOND = 10;

    /** Content-Encoding */
    private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    /** Content-Enccoding: gzip */
    private static final String HEADER_CONTENT_ENCODING_GZIP = "gzip";

    /** Set-Cookie */
    private static final String HEADER_SET_COOKIE = "Set-Cookie";

    /** JSON */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** Singlton OkHttpClient */
    private static OkHttpClient sOkHttpClient;
    /** WebView Cookie設定 */
    private static boolean sIsWebViewCookie;

    /**
     * プライベートコンストラクタ
     */
    private ApiRequestOkHttp() {
    }

    /**
     * OkHttpClient取得
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getNewOkHttpClient() {
        if (sOkHttpClient == null) {
            sOkHttpClient = new OkHttpClient();
            sOkHttpClient.setConnectTimeout(CONNECTION_TIMEOUT_SECOND, TimeUnit.SECONDS);
            sOkHttpClient.setReadTimeout(READ_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }
        return sOkHttpClient;
    }

    /**
     * コネクションタイムアウト設定
     *
     * @param seconds
     *         コネクションタイムアウト(秒)
     */
    public static void setConnectionTimeout(int seconds) {
        final OkHttpClient okHttpClient = getNewOkHttpClient();
        okHttpClient.setConnectTimeout(seconds, TimeUnit.SECONDS);
    }

    /**
     * リードタイムアウト設定
     *
     * @param seconds
     *         リードタイムアウト(秒)
     */
    public static void setReadTimeout(int seconds) {
        final OkHttpClient okHttpClient = getNewOkHttpClient();
        okHttpClient.setReadTimeout(seconds, TimeUnit.SECONDS);
    }

    /**
     * WebView Cookie自動設定
     *
     * @param isWebViewCookie
     *         true:WebViewのCookieをAPIから自動的に設定する場合は、trueを指定する。
     *         false:WebViewのCookieをAPIから設定しない場合は、falseを指定する。
     *         <b>デフォルトはfalseである。</b>
     */
    public static void setWebViewCookie(boolean isWebViewCookie) {
        sIsWebViewCookie = isWebViewCookie;
    }

    public static void request(NetworkRequestOkHttp networkRequestOkHttp, OkHttpCallbackListener callbackListener) {
        if (networkRequestOkHttp.getMethod() == NetworkRequestOkHttp.Method.GET) {
            requestGet(networkRequestOkHttp, callbackListener);
        } else if (networkRequestOkHttp.getMethod() == NetworkRequestOkHttp.Method.POST) {
            requestPost(networkRequestOkHttp, callbackListener);
        }
    }

    /**
     * GETリクエスト
     *
     * @param networkRequestOkHttp
     *         リクエスト
     * @param callbackListener
     *         コールバックリスナー
     */
    public static void requestGet(NetworkRequestOkHttp networkRequestOkHttp, OkHttpCallbackListener callbackListener) {
        Request.Builder requestBuilder = new Request.Builder().
                url(networkRequestOkHttp.getUrl()).
                get();
        // リクエストヘッダ
        final Map<String, String> headers = networkRequestOkHttp.getHeaders();
        if (!CollectionUtils.isNullOrEmpty(headers)) {
            for (final Map.Entry<String, String> e : headers.entrySet()) {
                requestBuilder.addHeader(e.getKey(), e.getValue());
            }
        }
        final Request request = requestBuilder.build();
        execute(networkRequestOkHttp, request, callbackListener);
    }

    /**
     * POSTリクエスト
     *
     * @param networkRequestOkHttp
     *         リクエスト
     * @param callbackListener
     *         コールバックリスナー
     */
    public static void requestPost(NetworkRequestOkHttp networkRequestOkHttp, OkHttpCallbackListener callbackListener) {
        Request.Builder requestBuilder = new Request.Builder().
                url(networkRequestOkHttp.getUrl()).
                post(RequestBody.create(MediaType.parse(HEADER_CONTENT_TYPE_TEXT_PLAIN), networkRequestOkHttp.getBody()));
        // リクエストヘッダ
        final Map<String, String> headers = networkRequestOkHttp.getHeaders();
        if (!CollectionUtils.isNullOrEmpty(headers)) {
            for (final Map.Entry<String, String> e : headers.entrySet()) {
                requestBuilder.addHeader(e.getKey(), e.getValue());
            }
        }
        final Request request = requestBuilder.build();
        execute(networkRequestOkHttp, request, callbackListener);
    }

    /**
     * @param networkRequestOkHttp
     *         ネットワークリクエスト
     * @param request
     *         {@link Request}
     * @param callbackListener
     *         コールバックリスナー
     */
    private static void execute(final NetworkRequestOkHttp networkRequestOkHttp, final Request request, final OkHttpCallbackListener callbackListener) {
        final OkHttpClient okHttpClient = getNewOkHttpClient();

        okHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            final Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(final Request request, final IOException e) {
                // ---------------------------------------------------------------
                // ログ出力
                // コンパイラ考慮のため、BuildConfig.DEBUGでここは判定する
                // ---------------------------------------------------------------
                if (BuildConfig.DEBUG) {
                    try {
                        final StringBuffer log = new StringBuffer();
                        StringUtils.appendBufferFormat(log, "----------------------------------------start(error)----------------------------------------\n");
                        StringUtils.appendBufferFormat(log, "■request\n");
                        // URL出力
                        StringUtils.appendBufferFormat(log, "url                             = %s\n", request.urlString());
                        // メソッド出力
                        StringUtils.appendBufferFormat(log, "method                          = %s\n", request.method());
                        // Body出力
                        StringUtils.appendBufferFormat(log, "body                            = %s\n", networkRequestOkHttp.getBody());
                        // リクエストID出力
                        StringUtils.appendBufferFormat(log, "id                              = %s\n", networkRequestOkHttp.getId());

                        // リクエストヘッダー出力
                        final Headers requestHeaders = request.headers();
                        for (final String headerName : requestHeaders.names()) {
                            StringUtils.appendBufferFormat(log, "header %-24s = %s\n", headerName, requestHeaders.get(headerName));
                        }
                        StringUtils.appendBuffer(log, "---------- volley stackTrace(start) ----------\n", e.toString(), "---------- volley stackTrace(end) ----------\n");
                        StringUtils.appendBufferFormat(log, "---------------------------------------- end(error) ----------------------------------------\n");
                        LogUtils.d(TAG, log.toString());
                    } catch (Exception e1) {
                        // エラー関連はログなので握りつぶす
                    }
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callbackListener.onFailure(null, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                // レスポンスデータ
                final String data;

                // ---------------------------------------------------------------
                // gzip対応
                // ---------------------------------------------------------------
                final Headers responseHeaders = response.headers();
                try {
                    // ContentTypeが、もしgzipなら解凍する
                    if (HEADER_CONTENT_ENCODING_GZIP.equalsIgnoreCase(responseHeaders.get(HEADER_CONTENT_ENCODING))) {
                        final GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(response.body().bytes())));
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        final byte[] buffer = new byte[1024];
                        int count;
                        while ((count = zis.read(buffer)) != -1) {
                            baos.write(buffer, 0, count);
                        }
                        data = baos.toString();
                        baos.close();
                        zis.close();
                    } else {
                        data = response.body().string();
                    }
                } catch (Exception e) {
                    onFailure(response.request(), new IOException("gzip error"));
                    return;
                }
                // ---------------------------------------------------------------
                // SetCookie
                // 自動的にWebViewのCookieに書き込みを行う。
                // ---------------------------------------------------------------
                if (sIsWebViewCookie) {
                    setCookie(response);
                }

                // ---------------------------------------------------------------
                // ログ出力
                // コンパイラ考慮のため、BuildConfig.DEBUGでここは判定する
                // ---------------------------------------------------------------
                if (BuildConfig.DEBUG) {
                    long okHttpReceivedMillis = 0;
                    long okHttpSentMillis = 0;
                    try {
                        final Request request = response.request();

                        final StringBuffer log = new StringBuffer();
                        StringUtils.appendBufferFormat(log, "----------------------------------------start----------------------------------------\n");
                        StringUtils.appendBufferFormat(log, "■request\n");
                        // URL出力
                        StringUtils.appendBufferFormat(log, "url                             = %s\n", request.urlString());
                        // メソッド出力
                        StringUtils.appendBufferFormat(log, "method                          = %s\n", request.method());
                        // Body出力
                        StringUtils.appendBufferFormat(log, "body                            = %s\n", networkRequestOkHttp.getBody());
                        // リクエストID出力
                        StringUtils.appendBufferFormat(log, "id                              = %s\n", networkRequestOkHttp.getId());

                        // リクエストヘッダー出力
                        final Headers requestHeaders = request.headers();
                        for (final String headerName : requestHeaders.names()) {
                            StringUtils.appendBufferFormat(log, "header %-24s = %s\n", headerName, requestHeaders.get(headerName));
                        }

                        StringUtils.appendBufferFormat(log, "\n■response\n");
                        // ステータスコード出力
                        StringUtils.appendBufferFormat(log, "status %-24s = %s\n", "code", response.code());

                        final int size = responseHeaders.size();
                        for (int i = 0; i < size; i++) {
                            final String name = responseHeaders.name(i);
                            final String value = responseHeaders.value(i);
                            StringUtils.appendBufferFormat(log, "header %-24s = %s\n", name, value);
                            // 送信開始時間を取得
                            if (StringUtils.equals(name, "OkHttp-Sent-Millis")) {
                                okHttpSentMillis = Long.parseLong(value);
                            }
                            // 受信完了時間を取得
                            if (StringUtils.equals(name, "OkHttp-Received-Millis")) {
                                okHttpReceivedMillis = Long.valueOf(value);
                            }
                        }
                        if (okHttpSentMillis != 0 && okHttpReceivedMillis != 0) {
                            StringUtils.appendBufferFormat(log, "time                            = %d msec\n", okHttpReceivedMillis - okHttpSentMillis);
                        }

                        // BODY出力
                        try {
                            StringUtils.appendBuffer(log, "body = \n");
                            if (StringUtils.contains(responseHeaders.get(HEADER_CONTENT_TYPE), HEADER_CONTENT_TYPE_APPLICATION_JSON)) {
                                try {
                                    JSONObject json = new JSONObject(data);
                                    StringUtils.appendBuffer(log, json.toString(4));
                                } catch (JSONException e) {
                                    StringUtils.appendBuffer(log, data);
                                }
                            } else {
                                StringUtils.appendBuffer(log, data);
                            }
                            StringUtils.appendBufferFormat(log, "\n---------------------------------------- end ----------------------------------------\n");
                            LogUtils.d(TAG, log.toString());
                        } catch (final OutOfMemoryError e) {
                            // 無視する。bodyがでか過ぎて無理なため。
                        }
                    } catch (Exception e) {
                        // ログのエラーは除外する。
                    }
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callbackListener.onSuccess(response, data);
                    }
                });
            }
        });
    }

    /**
     * set-Cookie設定処理
     *
     * @param response
     *         {@link Response}
     */
    private static void setCookie(final Response response) {
        // ---------------------------------------------------------------
        // SetCookie
        // ---------------------------------------------------------------
        final Headers responseHeaders = response.headers();
        final int size = responseHeaders.size();
        for (int i = 0; i < size; i++) {
            String name = responseHeaders.name(i);
            String value = responseHeaders.value(i);
            if (StringUtils.equals(HEADER_SET_COOKIE, name)) {
                CookieUtils.setValue(response.request().urlString(), value);
            }
        }
    }

    /**
     * Callbackインタフェース
     */
    public interface OkHttpCallbackListener {
        /**
         * エラーレスポンス
         *
         * @param response
         *         {@link Response}
         * @param throwable
         *         {@link Throwable}
         */
        void onFailure(Response response, Throwable throwable);

        /**
         * 正常レスポンス
         *
         * @param response
         *         {@link Response}
         * @param content
         *         レスポンスデータ
         */
        void onSuccess(Response response, String content);
    }
}
