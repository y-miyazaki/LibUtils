package com.miya38.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.android.volley.NetworkResponse;
import com.miya38.connection.volley.AbstractConnectionCommonVolley;
import com.miya38.connection.volley.NetworkRequest;

/**
 * Activity コネクション用抽象化クラス
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionVolleyActivity extends AbstractActivity {
    /** AbstractConnectionCommonVolley */
    private AbstractConnectionCommonVolley mAbstractConnectionCommonVolley;

    /**
     * コネクション共通処理取得
     * 
     * @return {@link AbstractConnectionCommonVolley}
     */
    protected abstract AbstractConnectionCommonVolley getConnectionCommon();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mAbstractConnectionCommonVolley = getConnectionCommon();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mAbstractConnectionCommonVolley != null) {
            mAbstractConnectionCommonVolley.getRequestQueue().cancelAll(getApplicationContext());
            mAbstractConnectionCommonVolley.clear();
        }
        mAbstractConnectionCommonVolley = null;
        super.onDestroy();
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
        if (mAbstractConnectionCommonVolley != null) {
            return mAbstractConnectionCommonVolley.setError(networkRequest, networkResponse, object);
        }
        return false;
    }

    /**
     * リクエストAPI
     * 
     * @param networkRequest
     *            {@link NetworkRequest}
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void requestAPI(final NetworkRequest networkRequest) {
        if (mAbstractConnectionCommonVolley != null) {
            mAbstractConnectionCommonVolley.requestAPI(networkRequest);
        }
    }
}