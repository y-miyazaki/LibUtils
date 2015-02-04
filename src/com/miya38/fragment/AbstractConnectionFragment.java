package com.miya38.fragment;

import android.os.Bundle;

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
     */
    protected abstract AbstractConnectionCommon getConnectionCommon();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAbstractConnectionCommon = getConnectionCommon();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.getRequestQueue().cancelAll(getActivity().getApplicationContext());
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
    public void setError(String title, String message) {
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
     */
    public boolean setError(NetworkRequest networkRequest, NetworkResponse networkResponse, Object object) {
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
    public void requestAPI(final NetworkRequest networkRequest) {
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.requestAPI(networkRequest);
        }
    }
}
