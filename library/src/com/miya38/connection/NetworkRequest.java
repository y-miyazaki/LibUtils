package com.miya38.connection;

import android.support.annotation.IntDef;

import com.android.volley.Request.Method;
import com.miya38.exception.ApplicationException;
import com.miya38.utils.ConnectionUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * ネットワークパラメータクラス
 * 
 * @author y-miyazaki
 * 
 */
public class NetworkRequest {
    /**
     * リクエストメソッド<br>
     * Method.GET/Method.POST/Method.PUT/Method.DELETEのいずれかを設定する。
     */
    public int mMethod;
    /**
     * URL
     */
    public String mUrl;
    /**
     * GET/DELETEクエリー
     */
    public Map<String, String> mQuery = new HashMap<String, String>();
    /**
     * POST/PUTボディ
     */
    public String mBody;
    /**
     * リクエストID<br>
     * このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     */
    public int mId;
    /**
     * リクエストヘッダー
     */
    public Map<String, String> mHeaders = new HashMap<String, String>();
    /**
     * プログレスバーの表示/非表示
     */
    public boolean mIsDisplayProgress = true;
    /**
     * エラーチェックを実施するか？
     */
    public boolean mIsErrorCheck = true;
    /**
     * ダイアログリスナーID<br>
     * このパラメータでもしダイアログを起動した際のコールバックリスナーを判断するためのIDに使用することが出来る。
     */
    public int mDialogListenerId = -1;

    /**
     * 空のコンストラクタ
     */
    public NetworkRequest() {
        // 何も初期化しない。
    }
    // ---------------------------------------------------------------
    // annotation
    // ---------------------------------------------------------------
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Method.GET,Method.DELETE})
    public @interface MethodGetDef {
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Method.POST,Method.PUT})
    public @interface MethodPostDef {
    }
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Method.GET,Method.POST,Method.DELETE,Method.PUT})
    public @interface MethodDef {
    }

    /**
     * GET/DELETEリクエストコンストラクタ
     * 
     * @param method
     *            リクエストメソッド<br>
     *            {@link Method#GET}<br>
     *            {@link Method#DELETE}<br>
     * <br>
     * @param url
     *            URL
     * @param query
     *            リクエストパラメータ
     * @param id
     *            リクエストID<br>
     *            このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     */
    public NetworkRequest(@MethodGetDef final int method, final String url, final Map<String, String> query, final int id) {
        this.mMethod = method;
        this.mUrl = ConnectionUtils.getUrl(url, query);
        this.mQuery = query;
        this.mId = id;
    }

    /**
     * GET/DELETEリクエストコンストラクタ
     * <p>
     * 通常のGET/DELETEリクエストに加え、リクエストヘッダを指定したい場合には本コンストラクタを使用する。
     * </p>
     * 
     * @param method
     *            リクエストメソッド<br>
     *            {@link Method#GET}<br>
     *            {@link Method#DELETE}<br>
     * <br>
     * @param url
     *            URL
     * @param query
     *            リクエストパラメータ
     * @param id
     *            リクエストID<br>
     *            このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     * @param headers
     *            リクエストヘッダー
     */
    public NetworkRequest(@MethodGetDef final int method, final String url, final Map<String, String> query, final int id, final Map<String, String> headers) {
        this.mMethod = method;
        this.mUrl = ConnectionUtils.getUrl(url, query);
        this.mQuery = query;
        this.mId = id;
        this.mHeaders = headers;
    }

    /**
     * POST/PUTリクエストコンストラクタ
     * 
     * @param method
     *            リクエストメソッド<br>
     *            {@link Method#POST}<br>
     *            {@link Method#PUT}<br>
     * <br>
     * @param url
     *            URL
     * @param body
     *            POST/PUTボディ
     * @param id
     *            リクエストID<br>
     *            このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     */
    public NetworkRequest(@MethodPostDef final int method, final String url, final String body, final int id) {
        this.mMethod = method;
        this.mUrl = url;
        this.mBody = body;
        this.mId = id;
    }

    /**
     * POST/PUTリクエストコンストラクタ
     * <p>
     * 通常のPOST/PUTリクエストに加え、リクエストヘッダを指定したい場合には本コンストラクタを使用する。
     * </p>
     * 
     * @param method
     *            リクエストメソッド<br>
     *            {@link Method#POST}<br>
     *            {@link Method#PUT}<br>
     * <br>
     * @param url
     *            URL
     * @param body
     *            POST/PUTボディ
     * @param id
     *            リクエストID<br>
     *            このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     * @param headers
     *            リクエストヘッダー
     */
    public NetworkRequest(@MethodPostDef final int method, final String url, final String body, final int id, final Map<String, String> headers) {
        if (method != Method.POST && method != Method.PUT) {
            throw new ApplicationException("method should set Method.POST or Method.PUT.");
        }
        this.mMethod = method;
        this.mUrl = url;
        this.mBody = body;
        this.mId = id;
        this.mHeaders = headers;
    }

    /**
     * @return mMethod
     */
    public final int getMethod() {
        return mMethod;
    }

    /**
     * @param method
     *            セットする mMethod
     */
    public final void setMethod(@MethodDef final int method) {
        this.mMethod = method;
    }

    /**
     * @return mUrl
     */
    public final String getUrl() {
        return mUrl;
    }

    /**
     * @param url
     *            セットする mUrl
     */
    public final void setUrl(final String url) {
        this.mUrl = url;
    }

    /**
     * @return mQuery
     */
    public final Map<String, String> getQuery() {
        return mQuery;
    }

    /**
     * @param query
     *            セットする mQuery
     */
    public final void setQuery(final Map<String, String> query) {
        this.mQuery = query;
    }

    /**
     * @return mBody
     */
    public final String getBody() {
        return mBody;
    }

    /**
     * @param body
     *            セットする mBody
     */
    public final void setBody(final String body) {
        this.mBody = body;
    }

    /**
     * @return mId
     */
    public final int getId() {
        return mId;
    }

    /**
     * @param id
     *            セットする mId
     */
    public final void setId(final int id) {
        this.mId = id;
    }

    /**
     * @return mHeaders
     */
    public final Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * @param headers
     *            セットする mHeaders
     */
    public final void setHeaders(final Map<String, String> headers) {
        this.mHeaders = headers;
    }

    /**
     * @return mIsDisplayProgress
     */
    public final boolean isDisplayProgress() {
        return mIsDisplayProgress;
    }

    /**
     * @param isDisplayProgress
     *            セットする mIsDisplayProgress
     */
    public final void setIsDisplayProgress(final boolean isDisplayProgress) {
        this.mIsDisplayProgress = isDisplayProgress;
    }

    /**
     * @return mIsErrorCheck
     */
    public final boolean isErrorCheck() {
        return mIsErrorCheck;
    }

    /**
     * @param isErrorCheck
     *            セットする mIsErrorCheck
     */
    public final void setIsErrorCheck(final boolean isErrorCheck) {
        this.mIsErrorCheck = isErrorCheck;
    }

    /**
     * @return mDialogListenerId
     */
    public final int getDialogListenerId() {
        return mDialogListenerId;
    }

    /**
     * @param dialogListenerId
     *            セットする mDialogListenerId
     */
    public final void setDialogListenerId(final int dialogListenerId) {
        this.mDialogListenerId = dialogListenerId;
    }

}
