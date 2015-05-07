package com.miya38.dialog;

import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.miya38.connection.AbstractConnectionCommon;
import com.miya38.connection.NetworkRequest;

/**
 * カスタムダイアログクラス レイアウトを自由に設定可能なクラス<br>
 * このクラスはFragmentベースなので、再生成が行われることを考慮し、コールバックはインタフェースを用いて行い、<br>
 * また匿名型クラスでは行えない仕様とする。<br>
 * Activity/Fragmentに対してDialogFragmentListenerをimplementsすること。<br>
 * 
 * @author y-miyazaki
 * 
 */
public abstract class AbstractConnectionDialogFragment extends AbstractDialogFragment {
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
        super.onDestroy();
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.getRequestQueue().cancelAll(TAG);
            mAbstractConnectionCommon.clear();
        }
        mAbstractConnectionCommon = null;
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
     * @return true:エラー表示あり、false:エラー表示なし
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
    public void requestAPI(final NetworkRequest networkRequest) {
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.requestAPI(networkRequest);
        }
    }
}