package com.miya38.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.WindowManager.LayoutParams;

import com.miya38.R;
import com.miya38.utils.ClassUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;

/**
 * カスタムダイアログクラス レイアウトを自由に設定可能なクラス<br>
 * このクラスはFragmentベースなので、再生成が行われることを考慮し、コールバックはインタフェースを用いて行い、<br>
 * また匿名型クラスでは行えない仕様とする。<br>
 * Activity/Fragmentに対してDialogFragmentListenerをimplementsすること。<br>
 *
 * @author y-miyazaki
 *
 */
public abstract class AbstractDialogFragment extends DialogFragment implements OnClickListener {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();
    /** レイアウト未設定 */
    protected static final int LAYOUT_NO_SETTING = 0;

    // ---------------------------------------------------------------
    // id define
    // --------------------------------------------------------------
    /** default layout */
    public static final int LAYOUT_DEFAULT = R.layout.common_dialog_layout_01_default;
    /** default layout viewstub */
    public static final int LAYOUT_VIEWSTUB_DEFAULT = R.layout.common_dialog_viewstub_01_default;

    // ---------------------------------------------------------------
    // key
    // ---------------------------------------------------------------
    /** layout id */
    public static final String KEY_FRAGMENT_CUSTOM_DIALOG_LAYOUT_ID = "layoutId";
    /** layout stub id */
    public static final String KEY_FRAGMENT_CUSTOM_DIALOG_LAYOUT_VIEWSTUB_ID = "layoutViewstubId";
    /** theme id */
    public static final String KEY_FRAGMENT_CUSTOM_DIALOG_THEME_ID = "themeId";
    /** リスナーID */
    public static final String KEY_FRAGMENT_CUSTOM_DIALOG_LISTENER_ID = "listenerId";

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** Dialog */
    private Dialog mDialog;
    /** クローズフラグ */
    private boolean mIsDismissFlg;

    /** リスナーID */
    private int mListenerId;

    /**
     * テーマを取得します。
     * <p>
     * 特にDialog表示時にテーマを指定しない場合は、0を指定してください。
     * </p>
     *
     * @return テーマID
     */
    protected abstract int getThemeId();

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

    /**
     * このインタフェースは、ダイアログからのボタンイベント、キャンセルイベント、クローズイベントを受け取る。 <br>
     * setOnDialogFramgentListenerにリスナーを設定することでイベントを受け取ることが出来る。
     *
     * @see #onEvent(int, int)
     */
    public interface OnDialogFragmentListener {
        /** negative button click */
        int DIALOG_NEGATIVE = 1;
        /** neutral button click */
        int DIALOG_NEUTRAL = 2;
        /** positive button click */
        int DIALOG_POSITIVE = 3;
        /** close button click */
        int DIALOG_CLOSE = 99;
        /** dialog dismiss */
        int DIALOG_DISMISS = 100;
        /** dialog cancel */
        int DIALOG_CANCEL = 101;

        /**
         * On event.
         *
         * @param listenerId
         *            listener id
         * @param event
         *            event is either of the following.<br>
         *            {@link #DIALOG_DISMISS}<br>
         *            {@link #DIALOG_CANCEL}<br>
         *            {@link #DIALOG_POSITIVE}<br>
         *            {@link #DIALOG_CLOSE}<br>
         *            {@link #DIALOG_NEGATIVE}<br>
         *            {@link #DIALOG_NEUTRAL}<br>
         */
        void onEvent(int listenerId, int event);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateDialog");
        // ----------------------------------------------------------------
        // Bundleからデータを取得
        // ----------------------------------------------------------------
        final int themeId = getThemeId();
        final int layoutId = getViewLayoutId();
        final int layoutViewStubId = getViewStubLayoutId();
        mListenerId = getArguments().getInt(KEY_FRAGMENT_CUSTOM_DIALOG_LISTENER_ID);

        // ----------------------------------------------------------------
        // Dialog setting
        // ----------------------------------------------------------------
        mDialog = new Dialog(getActivity(), themeId);
        mDialog.setContentView(layoutId);
        mDialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.setCanceledOnTouchOutside(false);

        // ----------------------------------------------------------------
        // ViewStub
        // ----------------------------------------------------------------
        if (layoutViewStubId != LAYOUT_NO_SETTING) {
            final ViewStub viewStub = ViewHelper.findView(mDialog, R.id.ViewStub01);
            viewStub.setInflatedId(R.id.InfratedId01);
            viewStub.setLayoutResource(layoutViewStubId);
            viewStub.inflate();
        }
        return mDialog;
    }

    @Override
    public void onResume() {
        LogUtils.d(TAG, "onResume");
        if (mIsDismissFlg) { // dismiss a dialog
            final Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
            if (fragment instanceof DialogFragment) {
                final DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
                getFragmentManager().beginTransaction().remove(fragment).commit();
                mIsDismissFlg = false;
            }
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        super.onDestroy();

        if (mDialog.getWindow() != null) {
            // Viewのクリア
            ViewHelper.cleanView(this, mDialog.getWindow().getDecorView());
        }
        // オブジェクトのnull初期化
        ClassUtils.setAsyncObjectNull(this, getClass(), AbstractDialogFragment.class);
    }

    /** @deprecated Use {@link #show(FragmentManager)}!! */
    @Override
    @Deprecated
    public final void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    /**
     * Display the dialog, adding the fragment to the given FragmentManager. This is a convenience for explicitly
     * creating a transaction, adding the fragment to it with the given tag, and committing it. This does <em>not</em>
     * add the transaction to the back stack. When the fragment is dismissed, a new transaction will be executed to
     * remove it from the activity.
     *
     * @param manager
     *            The FragmentManager this fragment will be added to.
     */
    public synchronized final void show(FragmentManager manager) {
        // 該当タグのダイアログが起動している場合は、2重起動しないよう制御する。
        if (!isDialogShow(manager)) {
            // super.show(manager, TAG);
            // FragmentTransaction transaction = manager.beginTransaction();
            // transaction.add(this, TAG);
            // transaction.commitAllowingStateLoss();
            super.show(manager, TAG);
        }
    }

    @Override
    public synchronized void dismiss() {
        LogUtils.d(TAG, "dismiss");
        if (isResumed()) {
            super.dismiss();
        } else {
            mIsDismissFlg = true;
        }
    }

    /**
     * ポジティブ選択
     */
    public void onPositive() {
        onEvent(OnDialogFragmentListener.DIALOG_POSITIVE);
        dismiss();
    }

    /**
     * ネガティブ選択
     */
    public void onNegative() {
        onEvent(OnDialogFragmentListener.DIALOG_NEGATIVE);
        dismiss();
    }

    /**
     * ニュートラル選択
     */
    public void onNeutral() {
        onEvent(OnDialogFragmentListener.DIALOG_NEUTRAL);
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onEvent(OnDialogFragmentListener.DIALOG_CANCEL);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onEvent(OnDialogFragmentListener.DIALOG_DISMISS);
        mListenerId = -1;
    }

    /**
     * イベント設定
     *
     * @param event
     *            event is either of the following.<br>
     *            {@link #DIALOG_DISMISS}<br>
     *            {@link #DIALOG_CANCEL}<br>
     *            {@link #DIALOG_POSITIVE}<br>
     *            {@link #DIALOG_NEGATIVE}<br>
     *            {@link #DIALOG_NEUTRAL}<br>
     */
    private void onEvent(int event) {
        if (getTargetFragment() instanceof OnDialogFragmentListener) {
            ((OnDialogFragmentListener) getTargetFragment()).onEvent(mListenerId, event);
        } else if (getActivity() instanceof OnDialogFragmentListener) {
            ((OnDialogFragmentListener) getActivity()).onEvent(mListenerId, event);
        }
    }

    @Override
    public void onClick(View v) {
        // index
        final int index = ((Integer) v.getTag()).intValue();
        // イベントコールバック判断用ID

        if (getTargetFragment() instanceof OnDialogFragmentListener) {
            ((OnDialogFragmentListener) getTargetFragment()).onEvent(mListenerId, index);
        } else if (getActivity() instanceof OnDialogFragmentListener) {
            ((OnDialogFragmentListener) getActivity()).onEvent(mListenerId, index);
        }
        dismiss();
    }

    /**
     * @param fragmentManager
     *            {@link FragmentManager}
     */
    private boolean isDialogShow(final FragmentManager manager) {
        final AbstractDialogFragment previous = (AbstractDialogFragment) manager.findFragmentByTag(TAG);
        if (previous == null) {
            return false;
        }
        final Dialog dialog = previous.getDialog();
        if (dialog == null) {
            return false;
        }
        if (!dialog.isShowing()) {
            return false;
        }
        return true;
    }

    /**
     * Fragmentが終了中・Viewが取れない等の状態を確認する。
     *
     * @return 削除中/Detach/Viewが取れない場合は、falseを返す 通常時はtrueを返す
     */
    public final boolean isFinishing() {
        if (getDialog() != null && isAlive()) {
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
     * リスナーID取得
     *
     * @return リスナーID
     */
    public int getListnerId() {
        return mListenerId;
    }
}
