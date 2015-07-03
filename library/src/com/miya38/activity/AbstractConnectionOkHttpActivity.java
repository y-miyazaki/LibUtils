package com.miya38.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.miya38.connection.AbstractConnectionCommonOkHttp;
import com.miya38.connection.okhttp.NetworkRequestOkHttp;
import com.squareup.okhttp.Response;

/**
 * Activity コネクション用抽象化クラス(OkHttp)
 * 
 * @author y-miyazaki
 */
public abstract class AbstractConnectionOkHttpActivity extends AbstractActivity {
    /** AbstractConnectionCommonOkHttp */
    private AbstractConnectionCommonOkHttp mAbstractConnectionCommon;

    /**
     * コネクション共通処理取得
     *
     * @return {@link AbstractConnectionCommonOkHttp}
     */
    protected abstract AbstractConnectionCommonOkHttp getConnectionCommon();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mAbstractConnectionCommon = getConnectionCommon();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
//        if (mAbstractConnectionCommon != null) {
//            mAbstractConnectionCommon.getRequestQueue().cancelAll(getApplicationContext());
//            mAbstractConnectionCommon.clear();
//        }
//        mAbstractConnectionCommon = null;
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
        if (mAbstractConnectionCommon != null) {
            return mAbstractConnectionCommon.setError(networkRequestOkHttp, networkResponse, object);
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
        if (mAbstractConnectionCommon != null) {
            mAbstractConnectionCommon.requestAPI(networkRequestOkHttp);
        }
    }
}