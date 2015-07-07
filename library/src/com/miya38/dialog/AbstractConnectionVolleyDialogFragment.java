package com.miya38.dialog;

import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.miya38.connection.volley.AbstractConnectionCommonVolley;
import com.miya38.connection.volley.NetworkRequest;

/**
 * カスタムダイアログクラス レイアウトを自由に設定可能なクラス<br>
 * このクラスはFragmentベースなので、再生成が行われることを考慮し、コールバックはインタフェースを用いて行い、<br>
 * また匿名型クラスでは行えない仕様とする。<br>
 * Activity/Fragmentに対してDialogFragmentListenerをimplementsすること。<br>
 * 
 * @author y-miyazaki
 * 
 */
public abstract class AbstractConnectionVolleyDialogFragment extends AbstractDialogFragment {
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
            mAbstractConnectionCommonVolley.getRequestQueue().cancelAll(TAG);
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
     * @return true:エラー表示あり、false:エラー表示なし
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
    public void requestAPI(final NetworkRequest networkRequest) {
        if (mAbstractConnectionCommonVolley != null) {
            mAbstractConnectionCommonVolley.requestAPI(networkRequest);
        }
    }
}