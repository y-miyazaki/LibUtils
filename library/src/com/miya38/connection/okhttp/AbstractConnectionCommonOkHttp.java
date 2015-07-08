package com.miya38.connection.okhttp;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.miya38.R;
import com.miya38.common.CommonInterface;
import com.miya38.utils.AplUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.utils.guava.Preconditions;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * コネクション共通処理
 * <p>
 * Activity/Fragment/Dialogで使用するコネクション処理の共通部分を行う。
 * </p>
 *
 * @author y-miyazaki
 */
public abstract class AbstractConnectionCommonOkHttp {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** TAG */
    private static final String TAG = AbstractConnectionCommonOkHttp.class.getSimpleName();

    // ----------------------------------------------------------
    // View
    // ----------------------------------------------------------
    /** Activity */
    protected FragmentActivity mActivity;
    /** Fragment */
    protected Fragment mFragment;
    /** DialogFragment */
    protected DialogFragment mDialogFragment;

    // ----------------------------------------------------------
    // callback listener
    // ----------------------------------------------------------
    /** OnLoaderFinishListener */
    private CommonInterface.OnLoaderFinishListener mOnLoaderFinishListener;

    // ----------------------------------------------------------
    // connection
    // ----------------------------------------------------------
    /** 画面にプログレスバーを表示するフラグ(ListView等で独自にフッダープログレスを表示する場合はfalseにする。) */
    private SparseArray<View> mDisplayProgress;

    /**
     * Jsonエラー表示用メソッド
     *
     * @param networkRequestOkHttp
     *         {@link NetworkRequestOkHttp}
     * @param response
     *         {@link Response}
     * @param data
     *         受信データ
     */
    public abstract void setJsonError(NetworkRequestOkHttp networkRequestOkHttp, Response response, String data);

    /**
     * エラー表示用メソッド
     * <p>
     * 通信周りでエラーが発生した場合に本メソッドでエラーを表示する。
     * </p>
     *
     * @param networkRequestOkHttp
     *         {@link NetworkRequestOkHttp}
     * @param response
     *         {@link Response}
     * @param object
     *         受信データ
     * @return true:エラー表示あり<br>
     * false:エラー表示なし
     */
    public abstract boolean setError(NetworkRequestOkHttp networkRequestOkHttp, Response response, Object object);

    /**
     * Volley用のリクエストキューを取得します。
     *
     * @return Volleyインスタンスリクエストキュー
     */
    public abstract RequestQueue getRequestQueue();

    /**
     * コンストラクタ
     *
     * @param activity
     *         {@link FragmentActivity}
     */
    public AbstractConnectionCommonOkHttp(final FragmentActivity activity) {
        mActivity = activity;
        init(activity);
    }

    /**
     * コンストラクタ
     *
     * @param fragment
     *         {@link Fragment}
     */
    public AbstractConnectionCommonOkHttp(final Fragment fragment) {
        mFragment = fragment;
        init(fragment);
    }

    /**
     * コンストラクタ
     *
     * @param dialogFragment
     *         {@link DialogFragment}
     */
    public AbstractConnectionCommonOkHttp(final DialogFragment dialogFragment) {
        mDialogFragment = dialogFragment;
        init(dialogFragment);
    }

    /**
     * ロード完了
     *
     * @param networkRequestOkHttp
     *         リクエストパラメータ<br>
     *         リクエスト時に送信したURL、メソッド、ヘッダ、パラメータを保持している
     * @param response
     *         受信データ(statusCode/header/data/notmodified)<br>
     *         ※タイムアウト等の場合はnullが設定される。
     * @param data
     *         受信データ
     */
    public void onLoadFinished(final NetworkRequestOkHttp networkRequestOkHttp, final Response response, final String data) {
        LogUtils.d(TAG, "onLoadFinished");
        // ---------------------------------------------------------------
        // 通信中にアクティビティが終了するような状況になった場合は、通信イベントの通知を行わない。
        // ---------------------------------------------------------------
        if (isFinishing()) {
            return;
        }

        onMethodLoaderFinished(networkRequestOkHttp, response, data);
        // エラー表示
        if (networkRequestOkHttp.isErrorCheck()) {
            setError(networkRequestOkHttp, response, data);
        }
        loadingDisplayProgress(false, networkRequestOkHttp.getId(), networkRequestOkHttp.isDisplayProgress()); // ローディング表示をオフにする
    }

    /**
     * 各メソッド毎にコールバックメソッドを呼びだす。
     *
     * @param networkRequestOkHttp
     *         リクエストパラメータ
     *         <p>
     *         リクエスト時に送信したURL、メソッド、ヘッダ、パラメータを保持している
     *         </p>
     * @param response
     *         受信データ(statusCode/header/data/notmodified)
     * @param data
     *         受信データ
     */
    public void onMethodLoaderFinished(final NetworkRequestOkHttp networkRequestOkHttp, final Response response, final String data) {
        // ---------------------------------------------------------------
        // 通信中にアクティビティが終了するような状況になった場合は、通信イベントの通知を行わない。
        // ---------------------------------------------------------------
        if (isFinishing()) {
            return;
        }

        // Jsonエラーチェック
        boolean isJsonException = false;
        try {
            if (mOnLoaderFinishListener != null) {
                mOnLoaderFinishListener.onLoaderFinished(networkRequestOkHttp, response, data);
            }
        } catch (final JsonParseException e) {
            isJsonException = true;
        } catch (final JsonMappingException e) {
            isJsonException = true;
        } catch (final IOException e) {
            isJsonException = true;
        }
        // ---------------------------------------------------------------
        // Jsonパースエラー
        // ---------------------------------------------------------------
        if (isJsonException && networkRequestOkHttp.isErrorCheck()) {
            setJsonError(networkRequestOkHttp, response, data);
        }
    }

    /**
     * リクエストAPI
     *
     * @param networkRequestOkHttp
     *         {@link NetworkRequestOkHttp}
     */
    public final void requestAPI(final NetworkRequestOkHttp networkRequestOkHttp) {
        Preconditions.checkNotNull(networkRequestOkHttp, "networkRequest should not be null.");
        ApiRequestOkHttp.request(networkRequestOkHttp, new ApiRequestOkHttp.OkHttpCallbackListener() {
            @Override
            public void onFailure(final Response response, final Throwable throwable) {
                onLoadFinished(networkRequestOkHttp, response, null);
            }

            @Override
            public void onSuccess(final Response response, final String content) {
                onLoadFinished(networkRequestOkHttp, response, content);
            }
        });
        loadingDisplayProgress(true, networkRequestOkHttp.getId(), networkRequestOkHttp.isDisplayProgress()); // ローディング表示をオンにする
    }

    /**
     * 画面にプログレスバーを表示するか
     *
     * @param display
     *         true:表示する/false:表示しない
     * @param id
     *         送信データ判別ID
     * @param isSpecialDisplay
     *         true:表示する/false:表示しない pullリクエスト等の特別にプログレスバーを表示したくない場合に使用する
     */
    private synchronized void loadingDisplayProgress(final boolean display, final int id, final boolean isSpecialDisplay) {
        if (isSpecialDisplay) {
            if (display) {
                View view;
                if (mActivity != null) {
                    final ViewStub viewStub = ViewHelper.findView(mActivity, R.id.ViewStubProgressBarActivity);
                    if (viewStub != null) {
                        viewStub.setInflatedId(R.id.InfratedIdProgressBar);
                        viewStub.setLayoutResource(R.layout.common_progressbar_viewstub);
                        viewStub.inflate();
                    }
                    view = ViewHelper.findView(mActivity, R.id.InfratedIdProgressBar);
                } else if (mFragment != null && mFragment.getView() != null) {
                    final ViewStub viewStub = ViewHelper.findView(mFragment.getView(), R.id.ViewStubProgressBarFragment);
                    if (viewStub != null) {
                        viewStub.setInflatedId(R.id.InfratedIdProgressBar);
                        viewStub.setLayoutResource(R.layout.common_progressbar_viewstub);
                        viewStub.inflate();
                    }
                    view = ViewHelper.findView(mFragment.getView(), R.id.InfratedIdProgressBar);
                } else if (mDialogFragment != null && mDialogFragment.getDialog() != null) {
                    final ViewStub viewStub = ViewHelper.findView(mDialogFragment.getDialog(), R.id.ViewStubProgressBarFragment);
                    if (viewStub != null) {
                        viewStub.setInflatedId(R.id.InfratedIdProgressBar);
                        viewStub.setLayoutResource(R.layout.common_progressbar_viewstub);
                        viewStub.inflate();
                    }
                    view = ViewHelper.findView(mDialogFragment.getDialog(), R.id.InfratedIdProgressBar);
                } else {
                    return;
                }
                mDisplayProgress.put(id, view);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                    view.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View v, final MotionEvent event) {
                            return true;
                        }
                    });
                }
            } else {
                final View view = mDisplayProgress.get(id);
                mDisplayProgress.remove(id);
                if (mDisplayProgress.size() == 0 && view != null) {
                    view.setVisibility(View.GONE);
                    view.setOnTouchListener(null);
                }
            }
        }
    }

    /**
     * リソースから文字列取得
     *
     * @param resId
     *         リソースID
     * @return リソースから取得した文字列
     */
    public String getString(final int resId) {
        if (mActivity != null) {
            return mActivity.getString(resId);
        }
        if (mFragment != null) {
            return mFragment.getString(resId);
        }
        if (mDialogFragment != null) {
            return mDialogFragment.getString(resId);
        }
        return null;
    }

    /**
     * リソースから文字列取得
     *
     * @param resId
     *         リソースID
     * @param args
     *         引数
     * @return リソースから取得した文字列
     */
    public String getString(final int resId, final Object... args) {
        if (mActivity != null) {
            return mActivity.getString(resId, args);
        }
        if (mFragment != null) {
            return mFragment.getString(resId, args);
        }
        if (mDialogFragment != null) {
            return mDialogFragment.getString(resId, args);
        }
        return null;
    }

    /**
     * Activity/Fragmentが生存状態かを確認する。
     *
     * @return 削除中/Detachの場合は、falseを返す 通常時はtrueを返す
     */
    @SuppressLint("NewApi")
    public boolean isFinishing() {
        // ---------------------------------------------------------------
        // 通信中にアクティビティが終了するような状況になった場合は、通信イベントの通知を行わない。
        // ---------------------------------------------------------------
        if (mActivity != null) {
            if (mActivity.isFinishing()) {
                return true;
            }
            if (AplUtils.hasJellyBeanMR1()) {
                if (mActivity.isDestroyed()) {
                    return true;
                }
            }
        }
        if (mFragment != null && (mFragment.getView() == null || mFragment.isRemoving() || mFragment.isDetached())) {
            return true;
        }
        if (mDialogFragment != null && (mDialogFragment.getDialog() == null || mDialogFragment.isRemoving() || mDialogFragment.isDetached())) {
            return true;
        }
        return false;
    }

    /**
     * FragmentActivity取得
     *
     * @return {@link FragmentActivity}
     */
    public FragmentActivity getActivity() {
        return mActivity;
    }

    /**
     * Fragment取得
     *
     * @return {@link Fragment}
     */
    public Fragment getFragment() {
        return mFragment;
    }

    /**
     * DialogFragment取得
     *
     * @return {@link DialogFragment}
     */
    public Fragment getDialogFragment() {
        return mFragment;
    }

    /**
     * オブジェクトクリア処理
     */
    public void clear() {
        mActivity = null;
        mFragment = null;
        mDialogFragment = null;
        mDisplayProgress.clear();
        mOnLoaderFinishListener = null;
    }

    /**
     * 初期化処理
     *
     * @param object
     *         Activity/Fragment/DialogFragment
     */
    private void init(Object object) {
        if (object instanceof CommonInterface.OnLoaderFinishListener) {
            mOnLoaderFinishListener = (CommonInterface.OnLoaderFinishListener) object;
        }
        mDisplayProgress = new SparseArray<View>();
    }
}
