package com.miya38.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager.LayoutParams;

import com.miya38.BuildConfig;
import com.miya38.R;
import com.miya38.activity.AbstractActivity;
import com.miya38.application.CommonApplication;
import com.miya38.application.CommonMultiDexApplication;
import com.miya38.common.CommonInterface.OnKeyDownListener;
import com.miya38.common.CommonInterface.OnWindowFocusChangedListener;
import com.miya38.exception.ApplicationException;
import com.miya38.utils.ClassUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;
import com.squareup.leakcanary.RefWatcher;

/**
 * Fragment抽象化クラス
 *
 * @author y-miyazaki
 */
public abstract class AbstractFragment extends Fragment {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();
    /** レイアウト未設定 */
    protected static final int LAYOUT_NO_SETTING = 0;

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** 親のアクティビティ */
    private AbstractActivity mActivity;
    /** OnKeyDownListener */
    private OnKeyDownListener mOnKeyDownListener;

    // ---------------------------------------------------------------
    // abstract method
    // ---------------------------------------------------------------

    /**
     * ヘッダ初期化処理
     * <p>
     * ここでは、ヘッダに表示すべきViewの初期化処理を記載します。
     * </p>
     *
     * @param savedInstanceState
     *         {@link Bundle}
     */
    protected abstract void initHeader(Bundle savedInstanceState);

    /**
     * フッタ初期化処理
     * <p>
     * ここでは、フッタに表示すべきViewの初期化処理を記載します。
     * </p>
     *
     * @param savedInstanceState
     *         {@link Bundle}
     */
    protected abstract void initFooter(Bundle savedInstanceState);

    /**
     * ビュー初期化処理
     * <p>
     * ここでは、通信が走る前に記述すべきViewの初期化処理を記載します。
     * </p>
     *
     * @param savedInstanceState
     *         {@link Bundle}
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * ビューレイアウトのリソースIDを取得します。
     *
     * @return リソースID。
     */
    protected abstract int getViewLayoutId();

    /**
     * ビュースタブを取得します。
     * <p>
     * もしViewStubがgetViewLayoutIdで指定されたレイアウト上に存在しない場合は、0を指定してください。
     * </p>
     *
     * @return ビュースタブID
     */
    protected abstract int getViewStubLayoutId();

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        LogUtils.d(TAG, "Fragment-onAttach");
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "Fragment-onCreate");

        // ----------------------------------------------------------
        // キーリスナーを設定する
        // ----------------------------------------------------------
        if (this instanceof OnKeyDownListener) {
            mOnKeyDownListener = (OnKeyDownListener) this;
        }
        // ---------------------------------------------------------------
        // レイアウト、キーボード設定を行う
        // ---------------------------------------------------------------
        if (!(getActivity() instanceof AbstractActivity)) {
            throw new ApplicationException("Activity must extends AbstractActivity.");
        }
        mActivity = (AbstractActivity) getActivity();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // mActivity.setSupportProgressBarIndeterminateVisibility(false);
    }

    /*
     * (非 Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
     * android.os.Bundle)
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        LogUtils.d(TAG, "Fragment-onCreateView");
        return inflater.inflate(getViewLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d(TAG, "Fragment-onActivityCreated");
        if (!isFinishing()) {
            if (getViewStubLayoutId() != LAYOUT_NO_SETTING) {
                final ViewStub viewStub = ViewHelper.findView(getView(), R.id.ViewStub01);
                viewStub.setInflatedId(R.id.InfratedId01);
                viewStub.setLayoutResource(getViewStubLayoutId());
                viewStub.inflate();
            }
            initView(savedInstanceState);
            initHeader(savedInstanceState);
            initFooter(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "Fragment-onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "Fragment-onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "Fragment-onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "Fragment-onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "Fragment-onDestroyView");
    }

    /*
     * (非 Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "Fragment-onDestroy");

        // Viewのクリア
        ViewHelper.cleanView(getActivity(), this, getView());

        // オブジェクトの非同期クリア
        ClassUtils.setAsyncObjectNull(this, getClass(), AbstractFragment.class);

        if (BuildConfig.DEBUG) {
            // leak canary
            Application application = getActivity().getApplication();
            if (application instanceof CommonMultiDexApplication) {
                RefWatcher refWatcher = CommonMultiDexApplication.getRefWatcher(getActivity());
                if (refWatcher != null) {
                    refWatcher.watch(this);
                }
            } else if (application instanceof CommonApplication) {
                RefWatcher refWatcher = CommonApplication.getRefWatcher(getActivity());
                if (refWatcher != null) {
                    refWatcher.watch(this);
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d(TAG, "Fragment-onDetach");
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "Fragment-onActivityResult");
    }

    /**
     * ヘッダー非表示処理
     */
    public void setNoHeader() {
        if (mActivity != null) {
            mActivity.setNoHeader();
        }
    }

    /**
     * ヘッダタイトル設定
     *
     * @param title
     *         タイトル
     */
    public void setHeaderTitle(final String title) {
        if (mActivity != null) {
            mActivity.setHeaderTitle(title);
        }
    }

    /**
     * Fragmentが終了中・Viewが取れない等の状態を確認する。
     *
     * @return 削除中/Detach/Viewが取れない場合は、falseを返す 通常時はtrueを返す
     */
    public final boolean isFinishing() {
        if (getView() != null && isAlive()) {
            return false;
        }
        return true;
    }

    /**
     * Fragmentが生存状態かを確認する。
     *
     * @return 削除中/Detachの場合は、falseを返す 通常時はtrueを返す
     */
    public final boolean isAlive() {
        if (isRemoving() || isDetached()) {
            return false;
        }
        return true;
    }

    /**
     * メンバ変数の自動保存処理を行う。
     * <p/>
     * 本メソッドは、端末がメモリ不足などの理由でアプリをメモリ上に常駐できなくなった場合に実行される。 本来の Activity
     * の仕様では各アクティビティごとにメンバ変数を保存する必要があるが、
     * これは非常な手間が掛かり、かつ修正時における不具合の原因となりうる。
     * <p/>
     * そこで本クラスはアクティビティに定義されているメンバ変数を自動的に保存する。<br>
     * 自動保存できるメンバ変数は、プリミティブ型とそのラッパーと配列など Bundleクラスへの追加をサポートしている型変数のみとなる。
     *
     * @param outState
     *         自動保存するメンバ変数の保存先 Bundle データ
     */
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        LogUtils.d(TAG, "Fragment-onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    /**
     * キーダウンのコールバックリスナー登録
     *
     * @param l
     *         リスナーを登録するとコールバックリスナーが走ります。
     */
    public final void setOnKeyDownListener(final OnKeyDownListener l) {
        this.mOnKeyDownListener = l;
    }

    /**
     * キーダウンのコールバックリスナーのインスタンス返却
     *
     * @return OnKeyDownListener
     */
    public final OnKeyDownListener getOnKeyDownListener() {
        return this.mOnKeyDownListener;
    }

    /**
     * ウィンドウフォーカスチェンジのコールバックリスナー登録
     *
     * @param l
     *         リスナーを登録するとコールバックリスナーが走ります。
     */
    public final void setOnWindowFocusChangedListener(final OnWindowFocusChangedListener l) {
        mActivity.setOnWindowFocusChangedListener(l);
    }

    /**
     * ウィンドウフォーカスチェンジのコールバックリスナーのインスタンス返却
     *
     * @return OnKeyDownListener
     */
    public final OnWindowFocusChangedListener getOnWindowFocusChangedListener() {
        return mActivity.getOnWindowFocusChangedListener();
    }

    /**
     * getSupportFragmentManagerをそのまま呼べるメソッド
     *
     * @return FragmentManager(support v4)
     */
    public final FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }
}
