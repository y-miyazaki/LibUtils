package com.miya38.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.android.volley.NetworkResponse;
import com.miya38.connection.volley.AbstractConnectionCommonVolley;
import com.miya38.connection.volley.NetworkRequest;

/**
 * Fragment コネクション用抽象化クラス
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionVolleyFragment extends AbstractFragment {
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
    public void onDestroy() {
        if (mAbstractConnectionCommonVolley != null) {
            mAbstractConnectionCommonVolley.getRequestQueue().cancelAll(getActivity().getApplicationContext());
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
     * @return true:エラー表示あり/false:エラー表示なし
     */
    public boolean setError(final NetworkRequest networkRequest, final NetworkResponse networkResponse, final Object object) {
        if (mAbstractConnectionCommonVolley != null) {
            return mAbstractConnectionCommonVolley.setError(networkRequest, networkResponse, object);
        }
        return false;
    }

    /**
     * リクエストAPI
     * <p>
     * </p>
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
