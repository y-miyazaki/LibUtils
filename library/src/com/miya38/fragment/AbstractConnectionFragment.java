package com.miya38.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.android.volley.NetworkResponse;
import com.miya38.connection.AbstractConnectionCommon;
import com.miya38.connection.NetworkRequest;

/**
 * Fragment コネクション用抽象化クラス
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionFragment extends AbstractFragment {
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
    public void onDestroy() {
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.getRequestQueue().cancelAll(getActivity().getApplicationContext());
            mAbstractConnectionCommon.clear();
        }
        mAbstractConnectionCommon = null;
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
        if (mAbstractConnectionCommon != null) {
            return mAbstractConnectionCommon.setError(networkRequest, networkResponse, object);
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
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.requestAPI(networkRequest);
        }
    }
}
