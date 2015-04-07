package com.miya38.activity;

import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.miya38.connection.AbstractConnectionCommon;
import com.miya38.connection.NetworkRequest;

/**
 * Activity コネクション用抽象化クラス
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionActivity extends AbstractActivity {
    /** AbstractConnectionCommon */
    private AbstractConnectionCommon mAbstractConnectionCommon;

    /**
     * コネクション共通処理取得
     * 
     * @return {@link AbstractConnectionCommon}
     */
    protected abstract AbstractConnectionCommon getConnectionCommon();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mAbstractConnectionCommon = getConnectionCommon();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.getRequestQueue().cancelAll(getApplicationContext());
            mAbstractConnectionCommon.clear();
        }
        mAbstractConnectionCommon = null;
    }

    /**
     * エラー表示用メソッド
     * 
     * @param title
     *            タイトル
     * @param message
     *            メッセージ
     */
    public void setError(final String title, final String message) {
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.setError(title, message);
        }
    }

    /**
     * エラー表示用メソッド
     * 
     * @param networkRequest
     *            {@link NetworkRequest}
     * @param networkResponse
     *            {@link NetworkResponse}
     * @param object
     *            受信データ
     * @return {@code true} エラーである。/{@code false} エラーではない。
     */
    public boolean setError(final NetworkRequest networkRequest, final NetworkResponse networkResponse, final Object object) {
        if (mAbstractConnectionCommon != null) {
            return mAbstractConnectionCommon.setError(networkRequest, networkResponse, object);
        }
        return false;
    }

    /**
     * リクエストAPI
     * 
     * @param networkRequest
     *            {@link NetworkRequest}
     */
    public void requestAPI(final NetworkRequest networkRequest) {
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.requestAPI(networkRequest);
        }
    }
}