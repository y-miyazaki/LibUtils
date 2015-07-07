package com.miya38.connection.volley;

import android.support.annotation.IntDef;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miya38.BuildConfig;
import com.miya38.utils.CookieUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.volley.HttpHeaderParser;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * APIリクエストクラス
 * <p>
 * VolleyのStringRequestを継承し、ログ出力・エラーハンドリング・ポリシー・リクエストパラメータの設定などを行うクラス。 ログ出力は、JSONの自動整形を行う。
 * </p>
 *
 * @author y-miyazaki
 */
public class ApiRequestVolley extends StringRequest {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    private static final String TAG = ApiRequestVolley.class.getSimpleName();
    /** Content-Type: application/json */
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    /** Content-Type: application/json */
    private static final String HEADER_CONTENT_TYPE_APPLICATION_JSON = "application/json";

    /** Content-Encoding */
    private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    /** Content-Enccoding: gzip */
    private static final String HEADER_CONTENT_ENCODING_GZIP = "gzip";

    /** Set-Cookie */
    private static final String HEADER_SET_COOKIE = "Set-Cookie";

    /** デバッグ設定 */
    private static boolean sIsDebuggable;
    // -------------------------------------------------------
    // 通信周り
    // -------------------------------------------------------
    /** The default socket timeout in milliseconds */
    public static final int CONNETION_DEFAULT_TIMEOUT_MS = 5000;
    /** The default number of retries */
    public static final int CONNETION_DEFAULT_MAX_RETRIES = 3;
    /** The default backoff multiplier */
    public static final float CONNETION_DEFAULT_BACKOFF_MULT = 1f;

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** コールバックリスナー */
    private ApiListener mApiListener;
    /** コールバックリスナー */
    private ApiErrorListener mApiErrorListener;
    /** リクエストパラメータ */
    protected NetworkRequest mNetworkRequest;
    /** リクエストヘッダ用パラメータ */
    private Map<String, String> mHeaders;
    /** POST/PUTリクエスト用パラメータ */
    private String mBody;
    /** 通信完了後の受信データ */
    protected NetworkResponse mNetworkResponse;

    // ---------------------------------------------------------------
    // annotation
    // ---------------------------------------------------------------
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Method.GET, Method.POST, Method.DELETE, Method.PUT})
    public @interface MethodDef {
    }

    /**
     * Callback interface for delivering parsed responses.
     *
     * @author y-miyazaki
     */
    public abstract static class ApiListener implements Listener<String> {
        /**
         * レスポンスを受信したときにコールする
         *
         * @param networkResponse
         *         受信データ(statusCode/header/data/notmodified)
         * @param id
         *         送信データ判別ID
         * @param response
         *         受信データ
         */
        public abstract void onResponse(NetworkResponse networkResponse, int id, String response);

        @Override
        public void onResponse(final String response) {
            // 何もしない。
        }
    }

    /**
     * Callback interface for delivering parsed responses.
     *
     * @author y-miyazaki
     */
    public abstract static class ApiErrorListener implements ErrorListener {
        /**
         * エラーレスポンスを受信したときにコールする
         *
         * @param networkResponse
         *         受信データ(statusCode/header/data/notmodified)
         * @param id
         *         送信データ判別ID
         */
        public abstract void onErrorResponse(NetworkResponse networkResponse, int id);

        @Override
        public void onErrorResponse(final VolleyError error) {
            // 何もしない。
        }
    }

    /**
     * コンストラクタ
     *
     * @param method
     *         {@link Method#GET},{@link Method#POST},{@link Method#DELETE}, {@link Method#PUT}
     * @param url
     *         URL
     * @param id
     *         リクエストID
     * @param apiListener
     *         {@link ApiListener}
     * @param apiErrorListener
     *         {@link ApiErrorListener}
     */
    public ApiRequestVolley(@MethodDef final int method, final String url, final int id, final ApiListener apiListener, final ApiErrorListener apiErrorListener) {
        super(method, url, apiListener, apiErrorListener);
        mApiListener = apiListener;
        mApiErrorListener = apiErrorListener;

        mNetworkRequest = new NetworkRequest();
        mNetworkRequest.mMethod = method;
        mNetworkRequest.mUrl = url;
        mNetworkRequest.mId = id;

        setRetryPolicy(new DefaultRetryPolicy(CONNETION_DEFAULT_TIMEOUT_MS, CONNETION_DEFAULT_MAX_RETRIES, CONNETION_DEFAULT_BACKOFF_MULT));
    }

    /**
     * コンストラクタ このコンストラクタは、自動的にsetParams/setHeadersを行う。
     *
     * @param networkRequest
     *         {@link NetworkResponse}
     * @param apiListener
     *         {@link ApiListener}
     * @param apiErrorListener
     *         {@link ApiErrorListener}
     */
    public ApiRequestVolley(final NetworkRequest networkRequest, final ApiListener apiListener, final ApiErrorListener apiErrorListener) {
        super(networkRequest.mMethod, networkRequest.mUrl, apiListener, apiErrorListener);
        mApiListener = apiListener;
        mNetworkRequest = networkRequest;
        mApiErrorListener = apiErrorListener;

        setRetryPolicy(new DefaultRetryPolicy(CONNETION_DEFAULT_TIMEOUT_MS, CONNETION_DEFAULT_MAX_RETRIES, CONNETION_DEFAULT_BACKOFF_MULT));

        if (networkRequest.mBody != null) {
            setParams(networkRequest.mBody);
        }

        if (networkRequest.mHeaders != null) {
            setHeaders(networkRequest.mHeaders);
        }
    }

    /**
     * デバッグ設定
     *
     * @param isDebugable
     *         true:デバッグ情報出力/false:デバッグ情報未出力
     */
    public static void setDebugable(boolean isDebugable) {
        sIsDebuggable = isDebugable;
    }

    @Override
    protected VolleyError parseNetworkError(final VolleyError volleyError) {
        // ---------------------------------------------------------------
        // SetCookie
        // ---------------------------------------------------------------
        NetworkResponse networkResponse = volleyError.networkResponse;
        setCookie(networkResponse);

        // ---------------------------------------------------------------
        // ログ出力
        // ---------------------------------------------------------------
        if (sIsDebuggable) {
            final StringBuffer log = new StringBuffer();
            StringUtils.appendBufferFormat(log, "----------------------------------------start(error)----------------------------------------\n");
            StringUtils.appendBufferFormat(log, "request url = %s\n", mNetworkRequest.mUrl);
            // リクエストヘッダー出力
            for (final Entry<String, String> e : mNetworkRequest.mHeaders.entrySet()) {
                StringUtils.appendBufferFormat(log, "request header: %s:%s\n", e.getKey(), e.getValue());
            }
            if (mNetworkRequest.mMethod == Method.GET) {
                StringUtils.appendBufferFormat(log, "request method: GET\n");
            } else if (mNetworkRequest.mMethod == Method.POST) {
                StringUtils.appendBufferFormat(log, "request method: POST\n");
            } else if (mNetworkRequest.mMethod == Method.PUT) {
                StringUtils.appendBufferFormat(log, "request method: PUT\n");
            } else if (mNetworkRequest.mMethod == Method.DELETE) {
                StringUtils.appendBufferFormat(log, "request method: DELETE\n");
            }
            StringUtils.appendBufferFormat(log, "request body = %s\n", mNetworkRequest.mBody);
            StringUtils.appendBufferFormat(log, "request id = %s\n", mNetworkRequest.mId);
            try {
                if (networkResponse != null) {
                    // ステータスコード出力
                    StringUtils.appendBufferFormat(log, "response status code = %s\n", networkResponse.statusCode);

                    final Header[] apacheHeaders = networkResponse.apacheHeaders;
                    if (apacheHeaders != null) {
                        for (final Header apacheHeader : apacheHeaders) {
                            final String key = apacheHeader.getName();
                            final String value = apacheHeader.getValue();
                            StringUtils.appendBufferFormat(log, "response header %-16s = %s\n", key, value);
                        }
                    }
                    StringUtils.appendBuffer(log, "response body = \n");
                    if (StringUtils.contains(networkResponse.headers.get(HEADER_CONTENT_TYPE), HEADER_CONTENT_TYPE_APPLICATION_JSON)) {
                        try {
                            JSONObject json = new JSONObject(new String(networkResponse.data));
                            StringUtils.appendBuffer(log, json.toString(4));
                        } catch (JSONException e) {
                            StringUtils.appendBuffer(log, new String(networkResponse.data));
                        }
                    } else {
                        StringUtils.appendBuffer(log, new String(networkResponse.data));
                    }
                }
                // stacktrace関連
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                volleyError.printStackTrace(pw);
                pw.flush();
                StringUtils.appendBuffer(log, "---------- volley stackTrace(start) ----------\n", sw.toString(), "---------- volley stackTrace(end) ----------\n");
                StringUtils.appendBufferFormat(log, "---------------------------------------- end(error) ----------------------------------------\n");

                LogUtils.d(TAG, log.toString());
            } catch (final Exception e) {
                // 無視する。bodyがでか過ぎて無理なため。
            }
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected Response<String> parseNetworkResponse(final NetworkResponse response) {
        mNetworkResponse = response;
        // ---------------------------------------------------------------
        // gzip対応
        // ---------------------------------------------------------------
        try {
            // ContentTypeが、もしgzipなら解凍する
            if (!mNetworkResponse.notModified && HEADER_CONTENT_ENCODING_GZIP.equalsIgnoreCase(response.headers.get(HEADER_CONTENT_ENCODING))) {
                final GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(response.data)));
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    final byte[] buffer = new byte[1024];
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                    }
                    mNetworkResponse = new NetworkResponse(response.statusCode, baos.toByteArray(), response.headers, response.notModified, response.networkTimeMs, response.apacheHeaders);
                } finally {
                    baos.close();
                    zis.close();
                }
            }
        } catch (final UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (final IOException e) {
            return Response.error(new ParseError(e));
        }

        // ---------------------------------------------------------------
        // SetCookie
        // 自動的にWebViewのCookieに書き込みを行う。
        // ---------------------------------------------------------------
        setCookie(mNetworkResponse);

        // ---------------------------------------------------------------
        // ステータスコード304の場合は、レスポンスヘッダーが除かれてしまうので、セットしなおす。
        // RFC的には正しいが、Volleyを使うと双方レスポンスヘッダーがほしくなるので。
        // ---------------------------------------------------------------
        if (mNetworkResponse.notModified && getCacheEntry() != null && getCacheEntry().responseHeaders != null) {
            for (final String key : mNetworkResponse.headers.keySet()) {
                getCacheEntry().responseHeaders.put(key, mNetworkResponse.headers.get(key));
            }
            for (final String key : getCacheEntry().responseHeaders.keySet()) {
                mNetworkResponse.headers.put(key, getCacheEntry().responseHeaders.get(key));
            }
        }

        // ---------------------------------------------------------------
        // ログ出力
        // コンパイラ考慮のため、BuildConfig.DEBUGでここは判定する
        // ---------------------------------------------------------------
        if (BuildConfig.DEBUG) {

            if (sIsDebuggable) {
                long xAndroidReceivedMillis = 0;
                long xAndroidSentMillis = 0;

                final StringBuffer log = new StringBuffer();
                StringUtils.appendBufferFormat(log, "----------------------------------------start----------------------------------------\n");
                StringUtils.appendBufferFormat(log, "■request\n");
                // URL出力
                StringUtils.appendBufferFormat(log, "url                                 = %s\n", mNetworkRequest.mUrl);
                // メソッド出力
                if (mNetworkRequest.mMethod == Method.GET) {
                    StringUtils.appendBufferFormat(log, "method                              = GET\n");
                } else if (mNetworkRequest.mMethod == Method.POST) {
                    StringUtils.appendBufferFormat(log, "method                              = POST\n");
                } else if (mNetworkRequest.mMethod == Method.PUT) {
                    StringUtils.appendBufferFormat(log, "method                              = PUT\n");
                } else if (mNetworkRequest.mMethod == Method.DELETE) {
                    StringUtils.appendBufferFormat(log, "method                              = DELETE\n");
                }
                // Body出力
                StringUtils.appendBufferFormat(log, "body                                = %s\n", mNetworkRequest.mBody);
                // リクエストID出力
                StringUtils.appendBufferFormat(log, "id                                  = %s\n", mNetworkRequest.mId);
                // リクエストヘッダー出力
                for (final Entry<String, String> e : mNetworkRequest.mHeaders.entrySet()) {
                    StringUtils.appendBufferFormat(log, "request header: %s:%s\n", e.getKey(), e.getValue());
                }

                StringUtils.appendBufferFormat(log, "\n■response\n");
                // ステータスコード出力
                StringUtils.appendBufferFormat(log, "status %-28s = %s\n", "code", mNetworkResponse.statusCode);

                final Header[] apacheHeaders = mNetworkResponse.apacheHeaders;
                if (apacheHeaders != null) {
                    for (final Header apacheHeader : apacheHeaders) {
                        final String key = apacheHeader.getName();
                        final String value = apacheHeader.getValue();
                        StringUtils.appendBufferFormat(log, "header %-28s = %s\n", key, value);

                        // 送信開始時間を取得
                        if (StringUtils.equals(key, "X-Android-Sent-Millis")) {
                            xAndroidSentMillis = Long.parseLong(value);
                        }
                        // 受信完了時間を取得
                        if (StringUtils.equals(key, "X-Android-Received-Millis")) {
                            xAndroidReceivedMillis = Long.valueOf(value);
                        }
                    }
                    if (xAndroidSentMillis != 0 && xAndroidReceivedMillis != 0) {
                        StringUtils.appendBufferFormat(log, "time                                = %d msec\n", xAndroidReceivedMillis - xAndroidSentMillis);
                    }
                }

                // BODY出力
                try {
                    StringUtils.appendBuffer(log, "body = \n");
                    if (StringUtils.contains(response.headers.get(HEADER_CONTENT_TYPE), HEADER_CONTENT_TYPE_APPLICATION_JSON)) {
                        try {
                            JSONObject json = new JSONObject(new String(mNetworkResponse.data));
                            StringUtils.appendBuffer(log, json.toString(4));
                        } catch (JSONException e) {
                            StringUtils.appendBuffer(log, new String(mNetworkResponse.data));
                        }
                    } else {
                        StringUtils.appendBuffer(log, new String(mNetworkResponse.data));
                    }
                    StringUtils.appendBufferFormat(log, "\n---------------------------------------- end ----------------------------------------\n");
                    LogUtils.d(TAG, log.toString());
                } catch (final OutOfMemoryError e) {
                    // 無視する。bodyがでか過ぎて無理なため。
                }
            }
        }
        String parsed;
        try {
            parsed = new String(mNetworkResponse.data, HttpHeaderParser.parseCharset(mNetworkResponse.headers, HTTP.UTF_8));
        } catch (final UnsupportedEncodingException e) {
            parsed = new String(mNetworkResponse.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(mNetworkResponse));
    }

    /**
     * set-Cookie設定処理
     *
     * @param response
     *         {@link NetworkResponse}
     */
    private void setCookie(final NetworkResponse response) {
        // ---------------------------------------------------------------
        // SetCookie
        // ---------------------------------------------------------------
        if (response != null) {
            final Header[] apacheHeaders = response.apacheHeaders;
            if (apacheHeaders != null) {
                for (final Header apacheHeader : apacheHeaders) {
                    final String key = apacheHeader.getName();
                    final String value = apacheHeader.getValue();
                    if (StringUtils.equals(HEADER_SET_COOKIE, key)) {
                        CookieUtils.setValue(mNetworkRequest.mUrl, value);
                    }
                }
            }
        }
    }

    @Override
    public void deliverResponse(final String response) {
        if (mApiListener != null) {
            mApiListener.onResponse(mNetworkResponse, mNetworkRequest.mId, response);
        }
        finish();
    }

    @Override
    public void deliverError(final VolleyError error) {
        if (mApiErrorListener != null) {
            mApiErrorListener.onErrorResponse(error.networkResponse, mNetworkRequest.mId);
        }
        finish();
    }

    @Override
    public void cancel() {
        super.cancel();
        finish();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        // このメソッドは通常POST/PUT用のボディを作成するメソッドだが、JSONを直接送信するため、KEY-VALUE型で送信しない。
        // そのため本メソッドを経由することはないので、nullを返却する。※実際は使用されないメソッドとする。
        return null;
    }

    /**
     * POST/PUTボディ取得
     * <p>
     * このメソッドをオーバーライドし、Map型のKEY-VALUEで取得する形式からStringで取得する形式に変更する。<br>
     * 本アプリではJSONをそのまま投げる形式のため。
     * </p>
     *
     * @return bodyのバイト配列
     * @throws AuthFailureError
     *         in the event of auth failure
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody.getBytes();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    /**
     * リクエストパラメータ設定
     *
     * @param body
     *         リクエストパラメータ
     */
    public void setParams(final String body) {
        this.mBody = body;
    }

    /**
     * リクエストヘッダ設定
     *
     * @param headers
     *         リクエストヘッダ
     */
    public void setHeaders(final Map<String, String> headers) {
        mHeaders = headers;
    }

    /**
     * 終了処理
     */
    private void finish() {
        mApiErrorListener = null;
        mApiListener = null;
        mHeaders = null;
        mNetworkRequest = null;
        mNetworkResponse = null;
    }
}
