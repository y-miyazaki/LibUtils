package com.miya38.dialog;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.miya38.utils.ConnectionUtils;
import com.miya38.utils.FragmentUtils;
import com.miya38.utils.StringUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomImageView;
import com.miya38.widget.CustomTextView;

/**
 * シェアダイアログフラグメント
 * <p>
 * 各SNS・メール・URLコピー等の様々な方法で設定した内容をシェアする機能である。
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public class ShareDialogFragment extends AbstractDialogFragment implements OnItemClickListener {
    // ----------------------------------------------------------
    // define(Twitter関連)
    // ----------------------------------------------------------
    /** Twitterアプリパッケージ名 */
    private static final String TWITTER_PACKAGE = "com.twitter.android";
    /** Twitterアプリがない場合のブラウザ共有用URL */
    private static final String TWITTER_URL = "http://twitter.com/share";
    /** Facebookアプリパッケージ名 */
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    /** Lineアプリパッケージ名 */
    private static final String LINE_PACKAGE = "jp.naver.line.android";
    /** はてなブックマークアプリパッケージ名 */
    private static final String HATENA_BOOKMARK_PACKAGE = "com.hatena.android.bookmark";

    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** URL用正規表現 */
    private static final String MATCH_URL = "^.*(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+).*$";

    /** key:シェアしたいメッセージ */
    private static final String KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_MESSAGE = "message";
    /** key:シェアしたいURL */
    private static final String KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_URL = "url";

    /** 機能:メール */
    private static final int FUNCTION_CATEGORY_MAIL = 1;
    /** 機能:URLをコピーする */
    private static final int FUNCTION_CATEGORY_URL_COPY = 2;
    /** 機能:ブラウザで開く */
    private static final int FUNCTION_CATEGORY_BROWSER = 3;
    /** 機能:他の方法で共有する */
    private static final int FUNCTION_CATEGORY_OTHER = 4;

    /** 対応するシェア先のパッケージ名 */
    private static final List<Package> PACKAGE_NAMES;
    static {
        PACKAGE_NAMES = new ArrayList<Package>();
        // Twitter
        PACKAGE_NAMES.add(new Package(TWITTER_PACKAGE, "Twitter", R.drawable.common_icon_twitter));
        // Facebook
        PACKAGE_NAMES.add(new Package(FACEBOOK_PACKAGE, null, 0));
        // Google+
        PACKAGE_NAMES.add(new Package("com.google.android.apps.plus", null, 0));
        // Line
        PACKAGE_NAMES.add(new Package(LINE_PACKAGE, null, 0));
        // Pocket
        PACKAGE_NAMES.add(new Package("com.ideashower.readitlater.pro", null, 0));
        // はてなブックマーク
        PACKAGE_NAMES.add(new Package(HATENA_BOOKMARK_PACKAGE, null, 0));
        // Evernote
        PACKAGE_NAMES.add(new Package("com.evernote", null, 0));
        // Tumblr
        PACKAGE_NAMES.add(new Package("com.tumblr", null, 0));
        // Pinterest
        PACKAGE_NAMES.add(new Package("com.pinterest", null, 0));
        // skype
        PACKAGE_NAMES.add(new Package("com.skype.raider", null, 0));
        //        // Gmail
        //        PACKAGE_NAMES.add(new Package("com.google.android.gm", null, 0));
    }

    /** その他機能 */
    private static final List<Function> FUNCTIONS;
    static {
        FUNCTIONS = new ArrayList<Function>();
        FUNCTIONS.add(new Function("メール", FUNCTION_CATEGORY_MAIL));
        FUNCTIONS.add(new Function("URLをコピーする", FUNCTION_CATEGORY_URL_COPY));
        FUNCTIONS.add(new Function("ブラウザで開く", FUNCTION_CATEGORY_BROWSER));
        FUNCTIONS.add(new Function("他の方法で共有する", FUNCTION_CATEGORY_OTHER));
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
    private final List<Package> mPackageName = new ArrayList<Package>();

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
    public static ShareDialogFragment newInstance(final int listenerId, final String message, final String url, final boolean isCancelable) {
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
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mMessage = FragmentUtils.getString(this, KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_MESSAGE);
        mUrl = FragmentUtils.getString(this, KEY_FRAGMENT_CUSTOM_DIALOG_SHARE_URL);

        // ----------------------------------------------------------
        // アプリが存在するものを表示するためにチェックを行う。
        // ----------------------------------------------------------
        int size = PACKAGE_NAMES.size();
        for (int i = 0; i < size; i++) {
            final String packageName = PACKAGE_NAMES.get(i).mPackageName;
            if (AplUtils.hasApplication(packageName)) {
                mPackageName.add(PACKAGE_NAMES.get(i));
            }
            // Twitterは連携方法としてブラウザが可能なので対応できるようにする。
            else if (StringUtils.equals(TWITTER_PACKAGE, packageName)) {
                mPackageName.add(PACKAGE_NAMES.get(i));
            }
        }

        // ----------------------------------------------------------
        // アイテムにアプリリストを追加する。
        // ----------------------------------------------------------
        final List<TsEtc004ApWebViewDialogListViewItem> tsEtc004ApWebViewDialogListViewItems = new ArrayList<TsEtc004ApWebViewDialogListViewItem>();
        size = mPackageName.size();
        for (int i = 0; i < size; i++) {
            final TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = new TsEtc004ApWebViewDialogListViewItem();
            final String packageName = mPackageName.get(i).mPackageName;
            tsEtc004ApWebViewDialogListViewItem.appName = AplUtils.getApplicationName(packageName) == null ? mPackageName.get(i).mAppName : AplUtils.getApplicationName(packageName);
            tsEtc004ApWebViewDialogListViewItem.packageName = packageName;
            tsEtc004ApWebViewDialogListViewItem.drawable = AplUtils.getApplicationIcon(packageName) == null ? getResources().getDrawable(mPackageName.get(i).mIconResouceId) : AplUtils.getApplicationIcon(packageName);
            tsEtc004ApWebViewDialogListViewItems.add(tsEtc004ApWebViewDialogListViewItem);
        }

        // ----------------------------------------------------------
        // アイテムにアプリ以外の機能を追加する。
        // ----------------------------------------------------------
        size = FUNCTIONS.size();
        for (int i = 0; i < size; i++) {
            final TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = new TsEtc004ApWebViewDialogListViewItem();
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
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final TsEtc004ApWebViewDialogListViewItem tsEtc004ApWebViewDialogListViewItem = mSettingListViewCustom.getItem(position - 1);
        // ----------------------------------------------------------
        // アプリ名がない場合は機能毎に処理を変える。
        // ----------------------------------------------------------
        if (tsEtc004ApWebViewDialogListViewItem.appName == null) {
            final Intent intent;
            switch (tsEtc004ApWebViewDialogListViewItem.functionCategory) {
            case FUNCTION_CATEGORY_MAIL: // メール
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                        .putExtra(Intent.EXTRA_TEXT, mMessage);
                startActivity(intent);
                break;
            case FUNCTION_CATEGORY_URL_COPY: // URLをコピー
                // クリップボードへコピーして、Toastで表示を行う。
                ClipboardUtils.setText(mUrl);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.dialog_share_url_copy, mUrl), Toast.LENGTH_SHORT).show();
                break;
            case FUNCTION_CATEGORY_BROWSER: // ブラウザで開く
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(intent);
                break;
            case FUNCTION_CATEGORY_OTHER: // 他の方法で共有する
                intent = new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, mMessage);
                startActivity(intent);
                break;
            }
        }

        // ----------------------------------------------------------
        // アプリ名がある場合はパッケージからIntentを起動する。
        // ----------------------------------------------------------
        else {
            // ----------------------------------------------------------
            // メッセージ内容を一部修正する。
            // ----------------------------------------------------------
            String message;
            // 「facebook」「はてなブックマーク」はURLしかシェア出来ないためURLがあれば抜き出す。
            if (StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, FACEBOOK_PACKAGE) ||
                    StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, HATENA_BOOKMARK_PACKAGE)) {
                message = mMessage.replaceAll(MATCH_URL, "$1$2");
            }
            // Lineはメッセージに改行があるとエラーが出るのでtrimする。
            else if (StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, LINE_PACKAGE)) {
                message = mMessage.replaceAll("\r\n|[\n\r\u2028\u2029\u0085]", "");
            } else {
                message = mMessage;
            }

            // ----------------------------------------------------------
            // アプリを起動する。
            // ----------------------------------------------------------
            // Twitterの場合
            if (StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, TWITTER_PACKAGE)) {
                checkTwitter(message);
            }
            // Lineの場合
            else if (StringUtils.equals(tsEtc004ApWebViewDialogListViewItem.packageName, LINE_PACKAGE)) {
                checkLine(message);
            } else {
                final Intent intent = new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, message)
                        .setPackage(tsEtc004ApWebViewDialogListViewItem.packageName); // パッケージをそのまま指定
                try {
                    startActivity(intent);
                } catch (final ActivityNotFoundException e) {
                    checkAppDownload(tsEtc004ApWebViewDialogListViewItem.packageName);
                }
            }
        }
        dismiss();
    }

    /**
     * Twitterアプリ判断用
     * <p>
     * Twitterはブラウザ連携も可能なため、アプリがない場合はブラウザでも連携できるようにしておく。
     * </p>
     * 
     * @param message
     *            送信メッセージ
     */
    private void checkTwitter(final String message) {
        try {
            // アプリをまずは起動する。
            final Intent intent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, message)
                    .setPackage(TWITTER_PACKAGE); // パッケージをそのまま指定
            startActivity(intent);
        } catch (final ActivityNotFoundException e) {
            // アプリがない場合はブラウザを起動する。
            final Map<String, String> query = new HashMap<String, String>();
            query.put("text", message);
            final Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionUtils.getUrl(TWITTER_URL, query)));
            startActivity(intent2);
        }
    }

    /**
     * Lineアプリ判断用
     * <p>
     * Lineは通常パターンとは異なるため、別途切り分けられるようにしておく。
     * </p>
     * 
     * @param message
     *            送信メッセージ
     */
    private void checkLine(final String message) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("line://msg/text/" + URLEncoder.encode(message, "UTF-8")));
            startActivity(intent);
        } catch (final Exception e) {
            checkAppDownload(LINE_PACKAGE);
        }
    }

    /**
     * アプリがない場合はGoogle playを起動する。
     * 
     * @param packageName
     *            パッケージ名
     */
    private void checkAppDownload(String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * パッケージ構成クラス
     * パッケージアプリがある場合のクラス
     */
    private static class Package {
        /** 機能名 */
        private final String mPackageName;
        /** 機能名 */
        private final String mAppName;
        /** アプリがインストールされていない場合のアイコンリソースID */
        private final int mIconResouceId;

        /**
         * コンストラクタ
         * 
         * @param packageName
         *            パッケージ名
         * @param appName
         *            アプリがインストールされていない場合のアプリ名
         * @param iconResourceId
         *            アプリがインストールされていない場合のアイコンリソースID
         */
        public Package(final String packageName, final String appName, final int iconResourceId) {
            this.mPackageName = packageName;
            this.mAppName = appName;
            this.mIconResouceId = iconResourceId;
        }
    }

    /**
     * 機能構成クラス
     * シェアアプリ以外の機能を設定するクラス
     */
    private static class Function {
        /** 機能名 */
        private final String mFunction;
        /** カテゴリ(カテゴリにより起動する内容を変更する。) */
        private final int mFunctionCategory;

        /**
         * コンストラクタ
         * 
         * @param function
         *            機能名
         * @param functionCategory
         *            カテゴリ(カテゴリにより起動する内容を変更する。)
         */
        public Function(final String function, final int functionCategory) {
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
        public TsEtc004ApWebViewDialogListViewAdapter(final Context context, final List<TsEtc004ApWebViewDialogListViewItem> items) {
            super(context, R.layout.dialog_share_listview_item, items);
        }

        @Override
        public View getViewCustom(final int position, final View convertView, final ViewGroup parent) {
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
