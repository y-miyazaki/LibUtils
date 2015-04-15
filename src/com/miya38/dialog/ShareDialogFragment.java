package com.miya38.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.miya38.R;
import com.miya38.list.SettingListView;
import com.miya38.list.adapter.CustomArrayAdapter;
import com.miya38.utils.AplUtils;
import com.miya38.utils.ClipboardUtils;
import com.miya38.utils.FragmentUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomImageView;
import com.miya38.widget.CustomTextView;

/**
 * SNS系シェアダイアログフラグメント
 * 
 * @author y-miyazaki
 * 
 */
public class ShareDialogFragment extends AbstractDialogFragment implements OnItemClickListener {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** URL用正規表現 */
    private static final String MATCH_URL = "^(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)$";

    /** key:シェアしたいメッセージ */
    private static final String KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_MESSAGE = "message";
    /** key:シェアしたいURL */
    private static final String KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_URL = "url";

    /** 機能:URLをコピーする */
    private static final int FUNCTION_CATEGORY_URL_COPY = 1;

    /** 対応するシェア先のパッケージ名 */
    private static final List<String> PACKAGE_NAMES;
    static {
        PACKAGE_NAMES = new ArrayList<String>();
        PACKAGE_NAMES.add("com.twitter.android");
        PACKAGE_NAMES.add("com.facebook.katana");
        PACKAGE_NAMES.add("com.google.android.apps.plus");
        PACKAGE_NAMES.add("jp.naver.line.android");
        PACKAGE_NAMES.add("com.ideashower.readitlater.pro");
        PACKAGE_NAMES.add("com.hatena.android.bookmark");
        PACKAGE_NAMES.add("com.evernote");
        PACKAGE_NAMES.add("com.tumblr");
        PACKAGE_NAMES.add("com.pinterest");
        PACKAGE_NAMES.add("com.google.android.gm");
    }

    /** その他機能 */
    private static final List<Function> FUNCTIONS;
    static {
        FUNCTIONS = new ArrayList<Function>();
        FUNCTIONS.add(new Function("URLをコピーする", FUNCTION_CATEGORY_URL_COPY));
    }

    // ----------------------------------------------------------
    // View定義
    // ----------------------------------------------------------
    /** Dialog */
    private Dialog mDialog;

    // ----------------------------------------------------------
    // ListView
    // ----------------------------------------------------------
    /** SettingListView */
    private SettingListView<TsEtc004ApWebViewDialogListViewAdapter, TsEtc004ApWebViewDialogListViewItem> mSettingListViewCustom;

    // ----------------------------------------------------------
    // Other
    // ----------------------------------------------------------
    /** インストールされているアプリのパッケージ名 */
    private List<String> mPackageName = new ArrayList<String>();

    /** 引数:メッセージ */
    private String mMessage;
    /** 引数:URL */
    private String mUrl;

    /**
     * ニューインスタンス
     * 
     * @return インスタンス
     */
    public static ShareDialogFragment newInstance() {
        return new ShareDialogFragment();
    }

    /**
     * Use this method as a constructor!!
     * 
     * @param fragment
     *            Fragment
     * @param message
     *            シェアしたい文字列
     * @param url
     *            コピーしたいURL<BR>
     * @param isCancelable
     *            whether the shown Dialog is cancelable.
     * @return new instance
     */
    public static ShareDialogFragment newInstance(int listenerId, String message, String url, boolean isCancelable) {
        final Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_CUSTOM_DIALOG_LISTENER_ID, listenerId);
        bundle.putString(KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_MESSAGE, message);
        bundle.putString(KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_URL, url);

        final ShareDialogFragment customDialogFragment = new ShareDialogFragment();
        customDialogFragment.setArguments(bundle);
        customDialogFragment.setCancelable(isCancelable);
        return customDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mMessage = FragmentUtils.getString(this, KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_MESSAGE);
        mUrl = FragmentUtils.getString(this, KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_URL);

        int size = PACKAGE_NAMES.size();
        for (int i = 0; i < size; i++) {
            if (AplUtils.hasApplication(PACKAGE_NAMES.get(i))) {
                mPackageName.add(PACKAGE_NAMES.get(i));
            }
        }

        List<TsEtc004ApWebViewDialogListViewItem> tsEtc004ApWebViewDialogListViewItems = new ArrayList<TsEtc004ApWebViewDialogListViewItem>();
        size = mPackageName.size();
        for (int i = 0; i < size; i++) {
            TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = new TsEtc004ApWebViewDialogListViewItem();
            String packageName = mPackageName.get(i);

            tsEtc004ApWebViewDialogListViewItem.appName = AplUtils.getApplicationName(packageName);
            tsEtc004ApWebViewDialogListViewItem.packageName = packageName;
            tsEtc004ApWebViewDialogListViewItem.drawable = AplUtils.getApplicationIcon(packageName);
            tsEtc004ApWebViewDialogListViewItems.add(tsEtc004ApWebViewDialogListViewItem);
        }

        size = FUNCTIONS.size();
        for (int i = 0; i < size; i++) {
            TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = new TsEtc004ApWebViewDialogListViewItem();
            tsEtc004ApWebViewDialogListViewItem.function = FUNCTIONS.get(i).mFunction;
            tsEtc004ApWebViewDialogListViewItem.functionCategory = FUNCTIONS.get(i).mFunctionCategory;
            tsEtc004ApWebViewDialogListViewItems.add(tsEtc004ApWebViewDialogListViewItem);
        }

        // 指定されたカテゴリのものだけを出力する。
        final TsEtc004ApWebViewDialogListViewAdapter tsEtc004ApWebViewDialogListViewAdapter = new TsEtc004ApWebViewDialogListViewAdapter(getActivity(), tsEtc004ApWebViewDialogListViewItems);
        mSettingListViewCustom = new SettingListView<TsEtc004ApWebViewDialogListViewAdapter, TsEtc004ApWebViewDialogListViewItem>(tsEtc004ApWebViewDialogListViewAdapter, mDialog, null);

        // 初期はEND PULLしか認めない。コンテンツ更新有無が返却されたことを検知した場合に双方許可する。(ダブルでリクエストされないように制御)
        mSettingListViewCustom.setView(R.id.CustomListView01, this, (OnRefreshListener<ListView>) null, Mode.DISABLED);

        return mDialog;
    }

    @Override
    protected int getThemeId() {
        return R.style.DialogThemeUp;
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.dialog_share_layout;
    }

    @Override
    protected int getViewStubLayoutId() {
        return R.layout.dialog_share_viewstub;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = mSettingListViewCustom.getItem(position - 1);

        // アプリ名がある場合はパッケージからIntentを起動する。
        if (tsEtc004ApWebViewDialogListViewItem.appName == null) {
            // URLをコピーする場合
            if (tsEtc004ApWebViewDialogListViewItem.functionCategory == FUNCTION_CATEGORY_URL_COPY) {
                ClipboardUtils.copy(mUrl);
            }
        } else {
            String message;
            // facebookはURLしかシェア出来ないためURLがあれば抜き出す。
            if (StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, "com.facebook.katana")) {
                message = mMessage.replaceAll(MATCH_URL, "$1");
            } else {
                message = mMessage;
            }

            Intent intent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, message)
                    .setPackage(tsEtc004ApWebViewDialogListViewItem.packageName); // パッケージをそのまま指定
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "not installed", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
    }

    /**
     * 機能構成クラス
     * シェアアプリ以外の機能を設定するクラス
     */
    private static class Function {
        /** 機能名 */
        private String mFunction;
        /** カテゴリ(カテゴリにより起動する内容を変更する。) */
        private int mFunctionCategory;

        /**
         * コンストラクタ
         * 
         * @param function
         *            機能名
         * @param functionCategory
         *            カテゴリ(カテゴリにより起動する内容を変更する。)
         */
        public Function(String function, int functionCategory) {
            this.mFunction = function;
            this.mFunctionCategory = functionCategory;
        }
    }

    /**
     * アダプタークラス
     * 
     * @author y-miyazaki
     */
    private static class TsEtc004ApWebViewDialogListViewAdapter extends CustomArrayAdapter<TsEtc004ApWebViewDialogListViewItem> {
        /**
         * コンストラクタ
         * 
         * @param context
         *            Context
         * @param items
         *            リストアイテム
         */
        public TsEtc004ApWebViewDialogListViewAdapter(Context context, List<TsEtc004ApWebViewDialogListViewItem> items) {
            super(context, R.layout.dialog_share_listview_item, items);
        }

        @Override
        public View getViewCustom(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view;
            if (convertView == null) {
                view = getLayoutView();
                holder = new ViewHolder();
                holder.customImageView01 = ViewHelper.findView(view, R.id.CustomImageView01);
                holder.customTextView01 = ViewHelper.findView(view, R.id.CustomTextView01);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            // 対象のアイテムを取得
            final TsEtc004ApWebViewDialogListViewItem item = getItem(position);

            // アイコン画像
            holder.customImageView01.setImageDrawable(item.drawable);
            if (item.appName == null) {
                // 機能名
                holder.customTextView01.setText(item.function);
            } else {
                // アプリ名
                holder.customTextView01.setText(item.appName);
            }

            return view;
        }

        /** 処理速度化のためのholder */
        @SuppressWarnings("unused")
        private class ViewHolder {
            /** ポジション */
            public int position;
            /** アイコン画像 */
            public CustomImageView customImageView01;
            /** アプリ名 */
            public CustomTextView customTextView01;
        }
    }

    /**
     * シェアダイアログアイテム
     * 
     * @author y-miyazaki
     */
    private final class TsEtc004ApWebViewDialogListViewItem {
        /** アイコン画像 */
        public Drawable drawable;
        /** アプリ名 */
        public String appName;
        /** パッケージ名 */
        public String packageName;
        /** 機能名(アプリではない機能を立ち上げる場合) */
        public String function;
        /** 機能カテゴリ */
        public int functionCategory;
    }
}
