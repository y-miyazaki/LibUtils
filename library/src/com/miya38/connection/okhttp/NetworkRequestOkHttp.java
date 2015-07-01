package com.miya38.connection.okhttp;

import com.miya38.utils.ConnectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * ネットワークパラメータクラス
 *
 * @author y-miyazaki
 */
public class NetworkRequestOkHttp {
    public enum Method {
        GET,
        POST,
        PUT,
        DELETE,
    }

    /**
     * メソッド
     */
    private Method mMethod;

    /**
     * URL
     */
    private String mUrl;
    /**
     * GET/DELETEクエリー
     */
    private Map<String, String> mQuery = new HashMap<String, String>();
    /**
     * POST/PUTボディ
     */
    private String mBody;
    /**
     * リクエストID<br>
     * このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     */
    private int mId;
    /**
     * リクエストヘッダー
     */
    private Map<String, String> mHeaders = new HashMap<String, String>();
    /**
     * プログレスバーの表示/非表示
     */
    private boolean mIsDisplayProgress = true;
    /**
     * エラーチェックを実施するか？
     */
    private boolean mIsErrorCheck = true;
    /**
     * ダイアログリスナーID<br>
     * このパラメータでもしダイアログを起動した際のコールバックリスナーを判断するためのIDに使用することが出来る。
     */
    private int mDialogListenerId = -1;

    /**
     * 空のコンストラクタ
     */
    public NetworkRequestOkHttp() {
        // 何も初期化しない。
    }

    /**
     * GET/DELETEリクエストコンストラクタ
     * <p>
     * 通常のGET/DELETEリクエストに加え、リクエストヘッダを指定したい場合には本コンストラクタを使用する。
     * </p>
     *
     * @param method
     *         メソッド
     * @param url
     *         URL
     * @param query
     *         リクエストパラメータ
     * @param body
     *         POST/PUTボディ
     * @param id
     *         リクエストID<br>
     *         このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     * @param headers
     *         リクエストヘッダー
     */
    public NetworkRequestOkHttp(final Method method, final String url, final Map<String, String> query, final String body, final int id, final Map<String, String> headers) {
        this.mMethod = method;
        this.mUrl = ConnectionUtils.getUrl(url, query);
        this.mQuery = query;
        this.mBody = body;
        this.mId = id;
        this.mHeaders = headers;
    }

    /**
     * GET/DELETEリクエストコンストラクタ
     * <p>
     * 通常のGET/DELETEリクエストに加え、リクエストヘッダを指定したい場合には本コンストラクタを使用する。
     * </p>
     *
     * @param method
     *         メソッド
     * @param url
     *         URL
     * @param query
     *         リクエストパラメータ
     * @param id
     *         リクエストID<br>
     *         このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     * @param headers
     *         リクエストヘッダー
     */
    public NetworkRequestOkHttp(final Method method, final String url, final Map<String, String> query, final int id, final Map<String, String> headers) {
        this.mMethod = method;
        this.mUrl = ConnectionUtils.getUrl(url, query);
        this.mQuery = query;
        this.mId = id;
        this.mHeaders = headers;
    }

    /**
     * POST/PUTリクエストコンストラクタ
     * <p>
     * 通常のPOST/PUTリクエストに加え、リクエストヘッダを指定したい場合には本コンストラクタを使用する。
     * </p>
     *
     * @param method
     *         メソッド
     * @param url
     *         URL
     * @param body
     *         POST/PUTボディ
     * @param id
     *         リクエストID<br>
     *         このパラメータでデータを受信した際に何をリクエストした結果であるかを判定することが出来る。
     * @param headers
     *         リクエストヘッダー
     */
    public NetworkRequestOkHttp(final Method method, final String url, final String body, final int id, final Map<String, String> headers) {
        this.mMethod = method;
        this.mUrl = url;
        this.mBody = body;
        this.mId = id;
        this.mHeaders = headers;
    }

    /**
     * メソッド取得
     *
     * @return メソッド
     */
    public Method getMethod() {
        return mMethod;
    }

    /**
     * メソッド設定
     *
     * @param method
     *         メソッド
     */
    public void setMethod(final Method method) {
        mMethod = method;
    }

    /**
     * @return mUrl
     */
    public final String getUrl() {
        return mUrl;
    }

    /**
     * @param url
     *         セットする mUrl
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
     *         セットする mQuery
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
     *         セットする mBody
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
     *         セットする mId
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
     *         セットする mHeaders
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
     *         セットする mIsDisplayProgress
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
     *         セットする mIsErrorCheck
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
     *         セットする mDialogListenerId
     */
    public final void setDialogListenerId(final int dialogListenerId) {
        this.mDialogListenerId = dialogListenerId;
    }

}
