package com.miya38.dialog;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.miya38.connection.okhttp.AbstractConnectionCommonOkHttp;
import com.miya38.connection.okhttp.NetworkRequestOkHttp;
import com.squareup.okhttp.Response;

/**
 * Activity コネクション用抽象化クラス(OkHttp)
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionOkHttpDialogFragment extends AbstractDialogFragment {
    /** AbstractConnectionCommonOkHttp */
    private AbstractConnectionCommonOkHttp mAbstractConnectionCommonOkHttp;

    /**
     * コネクション共通処理取得
     *
     * @return {@link AbstractConnectionCommonOkHttp}
     */
    protected abstract AbstractConnectionCommonOkHttp getConnectionCommon();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mAbstractConnectionCommonOkHttp = getConnectionCommon();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
//        if (mAbstractConnectionCommonOkHttp != null) {
//            mAbstractConnectionCommonOkHttp.getRequestQueue().cancelAll(getApplicationContext());
//            mAbstractConnectionCommonOkHttp.clear();
//        }
//        mAbstractConnectionCommonOkHttp = null;
        super.onDestroy();
    }

    /**
     * エラー表示用メソッド
     * 
     * @param networkRequestOkHttp
     *            {@link NetworkRequestOkHttp}
     * @param networkResponse
     *            {@link Response}
     * @param object
     *            受信データ
     * @return {@code true} エラーである。/{@code false} エラーではない。
     */
    public boolean setError(final NetworkRequestOkHttp networkRequestOkHttp, final Response networkResponse, final Object object) {
        if (mAbstractConnectionCommonOkHttp != null) {
            return mAbstractConnectionCommonOkHttp.setError(networkRequestOkHttp, networkResponse, object);
        }
        return false;
    }

    /**
     * リクエストAPI
     * 
     * @param networkRequestOkHttp
     *            {@link NetworkRequestOkHttp}
     */
    @RequiresPermission(Manifest.permission.INTERNET)
    public void requestAPI(final NetworkRequestOkHttp networkRequestOkHttp) {
        if (mAbstractConnectionCommonOkHttp != null) {
            mAbstractConnectionCommonOkHttp.requestAPI(networkRequestOkHttp);
        }
    }
}