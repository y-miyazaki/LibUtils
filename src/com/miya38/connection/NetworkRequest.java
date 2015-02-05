package com.miya38.connection;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.miya38.exception.ApplicationException;
import com.miya38.utils.ConnectionUtils;

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
    public NetworkRequest(final int method, final String url, final Map<String, String> query, final int id) {
        if (method != Method.GET && method != Method.DELETE) {
            throw new ApplicationException("method should set Method.GET or Method.DELETE.");
        }
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
    public NetworkRequest(final int method, final String url, final Map<String, String> query, final int id, final Map<String, String> headers) {
        if (method != Method.GET && method != Method.DELETE) {
            throw new ApplicationException("method should set Method.GET or Method.DELETE.");
        }
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
    public NetworkRequest(final int method, final String url, final String body, final int id) {
        if (method != Method.POST && method != Method.PUT) {
            throw new ApplicationException("method should set Method.POST or Method.PUT.");
        }
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
    public NetworkRequest(final int method, final String url, final String body, final int id, final Map<String, String> headers) {
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
     * @param mMethod
     *            セットする mMethod
     */
    public final void setMethod(final int mMethod) {
        this.mMethod = mMethod;
    }

    /**
     * @return mUrl
     */
    public final String getUrl() {
        return mUrl;
    }

    /**
     * @param mUrl
     *            セットする mUrl
     */
    public final void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    /**
     * @return mQuery
     */
    public final Map<String, String> getQuery() {
        return mQuery;
    }

    /**
     * @param mQuery
     *            セットする mQuery
     */
    public final void setQuery(final Map<String, String> mQuery) {
        this.mQuery = mQuery;
    }

    /**
     * @return mBody
     */
    public final String getBody() {
        return mBody;
    }

    /**
     * @param mBody
     *            セットする mBody
     */
    public final void setBody(final String mBody) {
        this.mBody = mBody;
    }

    /**
     * @return mId
     */
    public final int getId() {
        return mId;
    }

    /**
     * @param mId
     *            セットする mId
     */
    public final void setId(final int mId) {
        this.mId = mId;
    }

    /**
     * @return mHeaders
     */
    public final Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * @param mHeaders
     *            セットする mHeaders
     */
    public final void setHeaders(final Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    /**
     * @return mIsDisplayProgress
     */
    public final boolean isDisplayProgress() {
        return mIsDisplayProgress;
    }

    /**
     * @param mIsDisplayProgress
     *            セットする mIsDisplayProgress
     */
    public final void setIsDisplayProgress(final boolean mIsDisplayProgress) {
        this.mIsDisplayProgress = mIsDisplayProgress;
    }

    /**
     * @return mIsErrorCheck
     */
    public final boolean isErrorCheck() {
        return mIsErrorCheck;
    }

    /**
     * @param mIsErrorCheck
     *            セットする mIsErrorCheck
     */
    public final void setIsErrorCheck(final boolean mIsErrorCheck) {
        this.mIsErrorCheck = mIsErrorCheck;
    }

    /**
     * @return mDialogListenerId
     */
    public final int getDialogListenerId() {
        return mDialogListenerId;
    }

    /**
     * @param mDialogListenerId
     *            セットする mDialogListenerId
     */
    public final void setDialogListenerId(final int mDialogListenerId) {
        this.mDialogListenerId = mDialogListenerId;
    }

}
