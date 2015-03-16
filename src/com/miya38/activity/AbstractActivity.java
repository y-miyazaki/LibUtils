package com.miya38.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

import com.miya38.BuildConfig;
import com.miya38.R;
import com.miya38.common.CommonInterface.OnWindowFocusChangedListener;
import com.miya38.exception.CustomUncaughtExceptionHandler;
import com.miya38.utils.AplUtils;
import com.miya38.utils.ClassUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomTextView;

/**
 * Activity抽象化クラス
 *
 * @author y-miyazaki
 */
public abstract class AbstractActivity extends ActionBarActivity {
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
    /** stop()コールがされているか？ */
    private boolean mIsStop;

    /** ログアウトブロードキャストレシーバー */
    private final LogoutBroadcastReceiver mLogoutBroadcastReceiver = new LogoutBroadcastReceiver();

    /** OnWindowFocusChangedListener */
    private OnWindowFocusChangedListener mOnWindowFocusChangedListener;

    // ---------------------------------------------------------------
    // abstract method
    // ---------------------------------------------------------------
    /**
     * ヘッダ初期化処理
     *
     * @param savedInstanceState
     *            {@link Bundle}
     */
    protected abstract void initHeader(Bundle savedInstanceState);

    /**
     * フッタ初期化処理
     *
     * @param savedInstanceState
     *            {@link Bundle}
     */
    protected abstract void initFooter(Bundle savedInstanceState);

    /**
     * ビュー初期化処理
     *
     * @param savedInstanceState
     *            {@link Bundle}
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
     * もしViewStubがgetViewLayoutIdで指定されたレイアウト上に存在しない場合は、<br>
     * {@link AbstractActivity#LAYOUT_NO_SETTING}を指定してください。
     * </p>
     *
     * @return ビュースタブID
     */
    protected abstract int getViewStubLayoutId();

    /**
     * ログアウト用ブロードキャストレシーバー
     * <p>
     * アクティビティに全てセットすることで全画面を一気にfinishすることが可能なブロードキャストレシーバー
     * </p>
     *
     * @author y-miyazaki
     */
    private class LogoutBroadcastReceiver extends BroadcastReceiver {
        /*
         * (非 Javadoc)
         * @see
         * android.content.BroadcastReceiver#onReceive(android.content.Context,
         * android.content.Intent)
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            // 特定のクラス名が指定されていた場合はそのクラスだけはfinishしない。
            final String className = intent.getStringExtra("className");
            if (className == null) {
                finish();
            } else {
                if (!AbstractActivity.this.getClass().getSimpleName().equals(className)) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");

        // ---------------------------------------------------------------
        // ActionBarプログレスバー、レイアウト、キーボード設定を行う
        // ---------------------------------------------------------------
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // ---------------------------------------------------------------
        // デバッグ状態の場合は、デバッグ情報をアプリに保存する。
        // ---------------------------------------------------------------
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
        }

        setContentView(getViewLayoutId());

        if (getViewStubLayoutId() != LAYOUT_NO_SETTING) {
            final ViewStub viewStub = ViewHelper.findView(this, R.id.ViewStub01);
            viewStub.setInflatedId(R.id.InfratedId01);
            viewStub.setLayoutResource(getViewStubLayoutId());
            viewStub.inflate();
        }
        initView(savedInstanceState);
        initHeader(savedInstanceState);
        initFooter(savedInstanceState);

        // ---------------------------------------------------------------
        // ログアウト処理をBroadcastReceiverで実施する。
        // ---------------------------------------------------------------
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AplUtils.getPackageName() + ".LOGOUT");
        registerReceiver(mLogoutBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        mIsStop = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
        mIsStop = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
        mIsStop = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, "onRestart");
        mIsStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop");
        mIsStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy");

        // Viewのクリア
        ViewHelper.cleanView(this, this, getWindow().getDecorView());

        // ブロードキャストレシーバーのクリア
        unregisterReceiver(mLogoutBroadcastReceiver);

        // オブジェクトのnull初期化
        ClassUtils.setAsyncObjectNull(this, getClass(), AbstractActivity.class);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult");
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mOnWindowFocusChangedListener != null) {
            mOnWindowFocusChangedListener.onWindowFocusChanged(hasFocus);
        }
    }

    /**
     * ヘッダー非表示処理
     *
     * @return タイトル非表示にしたか？
     */
    public boolean setNoHeader() {
        final RelativeLayout relativeLayoutHeader = ViewHelper.findView(this, R.id.RelativeLayoutHeader);
        if (relativeLayoutHeader == null) {
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null && actionBar.isShowing()) {
                actionBar.hide();
            }
        } else {
            relativeLayoutHeader.setVisibility(View.GONE);
        }
        return true;
    }

    /**
     * ヘッダタイトル設定
     *
     * @param title
     *            タイトル
     * @return タイトル設定したか？
     */
    public boolean setHeaderTitle(final String title) {
        final CustomTextView customTextViewHeader01 = ViewHelper.findView(this, R.id.CustomTextViewHeader);
        if (customTextViewHeader01 == null) {
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (!actionBar.isShowing()) {
                    actionBar.show();
                }
                actionBar.setTitle(title);
            }
        } else {
            customTextViewHeader01.setText(title);
        }
        return true;
    }

    /**
     * フッタタイトル設定
     *
     * @param title
     *            タイトル
     * @return フッタ設定したか？
     */
    public boolean setFooterTitle(final String title) {
        final CustomTextView customTextViewFooter01 = ViewHelper.findView(this, R.id.CustomTextViewFooter);
        if (customTextViewFooter01 != null) {
            customTextViewFooter01.setText(title);
            return true;
        }
        return false;
    }

    /**
     * 指定されたアクティビティクラス以外を全て殺す<br>
     * 指定された引数のクラス以外を全てfinish()する。
     *
     * @param clazz
     *            finishしないClass
     */
    public final void finishAll(final Class<?> clazz) {
        final Intent intent = new Intent();
        intent.putExtra("className", clazz.getSimpleName());
        intent.setAction(StringUtils.appendBuilder(AplUtils.getPackageName(), ".LOGOUT"));
        sendBroadcast(intent);
    }

    /**
     * 指定されたアクティビティクラス以外を全て殺す<br>
     * 指定された引数のクラス以外を全てfinish()する。
     */
    public final void finishAll() {
        final Intent intent = new Intent();
        intent.setAction(StringUtils.appendBuilder(AplUtils.getPackageName(), ".LOGOUT"));
        sendBroadcast(intent);
    }

    //
    // /**
    // * メンバ変数の自動保存処理を行う。
    // *
    // * 本メソッドは、端末がメモリ不足などの理由でアプリをメモリ上に常駐できなくなった場合に実行される。 本来の Activity
    // * の仕様では各アクティビティごとにメンバ変数を保存する必要があるが、 これは非常な手間が掛かり、かつ修正時における不具合の原因となりうる。
    // *
    // * そこで本クラスはアクティビティに定義されているメンバ変数を自動的に保存する。<br>
    // * 自動保存できるメンバ変数は、プリミティブ型とそのラッパーと配列など Bundleクラスへの追加をサポートしている型変数のみとなる。
    // *
    // * @param outState
    // * 自動保存するメンバ変数の保存先 Bundle データ
    // */
    // @Override
    // protected void onSaveInstanceState(final Bundle outState) {
    // try {
    // // 自クラスを含んだ全スーパークラス情報を取得する
    // final List<Class<?>> classes = ClassUtils.getSuperClasses(getClass(), AbstractActivity.class, true, true);
    // // 自クラスを含んだ全スーパークラス情報分繰り返す
    // for (final Class<?> nowClass: classes) {
    // // 全フィールドを取得する
    // final List<Field> fields = ClassUtils.getClassInstanceFields(nowClass);
    // // 全フィールド分処理をする
    // for (final Field field: fields) {
    // // フィールドが final の場合
    // if (Modifier.isFinal(field.getModifiers())) {
    // // 次のフィールドへ
    // continue;
    // }
    // // パラメータを追加する
    // ClassUtils.putObjectBundle(outState, String.format("%1$s,%2$s", nowClass.getName(), field.getName()),
    // field.get(this));
    // }
    // }
    // } catch (final Throwable e) {
    // e.printStackTrace();
    // }
    // // スーパークラスの処理を実行する
    // super.onSaveInstanceState(outState);
    // }
    //
    // /**
    // * メンバ変数の自動復帰処理を行う。
    // *
    // * {@link #onSaveInstanceState(Bundle)} メソッドで自動保存されたメンバ変数の 自動復帰処理を行う。
    // *
    // * @param savedInstanceState
    // * 自動保存されたメンバ変数を格納した Bundle データ
    // */
    // @Override
    // protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    // try {
    // final int indexClassName = 0; // クラス名インデックス
    // final int indexFieldName = 1; // フィールド名インデックス
    // final int indexCount = 2; // インデックス数
    // // 全キー名分処理をする
    // for (final String key: savedInstanceState.keySet()) {
    // // キー名を分割する
    // final String[] names = key.split(",");
    // // キー名の要素がインデックス数と異なる場合
    // if (names.length != indexCount) {
    // // 次の要素へ
    // continue;
    // }
    // // クラス名とフィールド名を取得する
    // final String className = names[indexClassName];
    // final String fieldName = names[indexFieldName];
    // // 値を設定する
    // ClassUtils.setInstanceFieldValue(ClassUtils.getClassObject(className), this, fieldName,
    // savedInstanceState.get(key));
    // }
    // } catch (final Throwable e) {
    // // 握りつぶす
    // }
    // // スーパークラスの処理を実行する
    // super.onRestoreInstanceState(savedInstanceState);
    // }

    /**
     * 端末向き変更によるActivity再生成防止対策
     *
     * @param newConfig
     *            {@link Configuration}
     */
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "onConfigurationChanged");
    }

    /**
     * Stop状態であるかを返却する
     *
     * @return true:ライフサイクルがstop状態<br>
     *         false:ライフサイクルがstop状態以外<br>
     */
    public final boolean isStop() {
        return mIsStop;
    }

    /**
     * ウィンドウフォーカスチェンジのコールバックリスナー登録<br>
     *
     * @param l
     *            リスナーを登録するとコールバックリスナーが走ります。
     */
    public final void setOnWindowFocusChangedListener(final OnWindowFocusChangedListener l) {
        this.mOnWindowFocusChangedListener = l;
    }

    /**
     * ウィンドウフォーカスチェンジのコールバックリスナーのインスタンス返却
     *
     * @return OnKeyDownListener
     */
    public final OnWindowFocusChangedListener getOnWindowFocusChangedListener() {
        return this.mOnWindowFocusChangedListener;
    }
}
