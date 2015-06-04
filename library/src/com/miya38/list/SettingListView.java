package com.miya38.list;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.exception.ApplicationException;
import com.miya38.list.adapter.CustomArrayAdapter;
import com.miya38.utils.AplUtils;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomListView;

import java.util.List;

/**
 * CustomListView設定クラス
 * <p>
 * このクラスはCustomListViewに関する基本的な設定を行うクラスである。<br>
 * CustomListViewに関する設定は全てこのクラスを使うことで操作を行うことで設定漏れ等を極力なくすのが目的である。<br>
 * 主にCustomListView、Adapterに関する操作をこのクラスでは実装している。<br>
 * </p>
 * 
 * 
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 * 
 * @author y-miyazaki
 */
public class SettingListView<T extends CustomArrayAdapter<U>, U> extends AbstractSettingListView<T, U> {
    /** Adapter */
    protected T mAdapter;
    /** Acitivity */
    protected Activity mActivity;
    /** View */
    protected View mView;
    /** Dialog */
    protected Dialog mDialog;
    /** ListView */
    protected CustomListView mListView;
    /** LayoutInflater */
    protected LayoutInflater mLayoutInflater;
    /** 0件時に表示するためのView */
    protected int mListViewEmptyId;
    /** ヘッダーView */
    protected View mHeaderView;

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     */
    public SettingListView(final T adapter, final Activity activity) {
        this.mAdapter = adapter;
        this.mActivity = activity;
    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     * @param layoutInflater
     *            layoutInflater
     */
    public SettingListView(final T adapter, final Activity activity, final LayoutInflater layoutInflater) {
        this.mAdapter = adapter;
        this.mActivity = activity;
        this.mLayoutInflater = layoutInflater;

    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param customDialog
     *            ダイアログ
     * @param layoutInflater
     *            layoutInflater
     */
    public SettingListView(final T adapter, final Dialog customDialog, final LayoutInflater layoutInflater) {
        this.mAdapter = adapter;
        this.mDialog = customDialog;
        this.mLayoutInflater = layoutInflater;
    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param view
     *            ListViewが設定されているView
     * @param layoutInflater
     *            layoutInflater
     */
    public SettingListView(final T adapter, final View view, final LayoutInflater layoutInflater) {
        this.mAdapter = adapter;
        this.mView = view;
        this.mLayoutInflater = layoutInflater;
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     */
    public void setView(final int listViewId) {
        mListView = getView(listViewId);
        mListView.setAdapter(mAdapter);
        setEmptyView();
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param listViewEmptyId
     *            Empty時に表示するViewのId
     */
    public void setView(final int listViewId, final int listViewEmptyId) {
        this.mListViewEmptyId = listViewEmptyId;
        setView(listViewId);
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            プル時のリスナー
     * @param mode
     *            Mode
     */
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<ListView> onRefreshListener, final Mode mode) {
        mListView = getView(listViewId);
        if (mHeaderView != null) {
            addHeaderView(mHeaderView);
        }
        mListView.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnRefreshListener(onRefreshListener, mode);
        setEmptyView();
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            スクロールリスナー
     * @param mode
     *            Mode
     * @param listViewEmptyId
     *            Empty時に表示するViewのId
     */
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<ListView> onRefreshListener, final Mode mode, final int listViewEmptyId) {
        this.mListViewEmptyId = listViewEmptyId;
        setView(listViewId, onItemClickListener, onRefreshListener, mode);
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener2
     *            プル時のリスナー
     * @param mode
     *            Mode
     */
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener2<ListView> onRefreshListener2, final Mode mode) {
        mListView = getView(listViewId);
        if (mHeaderView != null) {
            addHeaderView(mHeaderView);
        }
        mListView.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnRefreshListener(onRefreshListener2, mode);
        setEmptyView();
    }

    /**
     * ListView設定(初期設定)
     * 
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener2
     *            スクロールリスナー
     * @param mode
     *            Mode
     * @param listViewEmptyId
     *            Empty時に表示するViewのId
     */
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener2<ListView> onRefreshListener2, final Mode mode, final int listViewEmptyId) {
        this.mListViewEmptyId = listViewEmptyId;
        setView(listViewId, onItemClickListener, onRefreshListener2, mode);
    }

    /**
     * View取得
     * 
     * @param <V>
     *            Viewの継承View
     * @param id
     *            View ID
     * @return View
     */
    protected <V extends View> V getView(final int id) {
        if (mActivity != null) {
            return ViewHelper.findView(mActivity, id);
        } else if (mView != null) {
            return ViewHelper.findView(mView, id);
        } else if (mDialog != null) {
            return ViewHelper.findView(mDialog, id);
        } else {
            throw new ApplicationException("you must set activity or view or dialog.");
        }
    }

    @Override
    public Mode getMode() {
        return mListView.getMode();
    }

    @Override
    public void setMode(final Mode mode) {
        mListView.setMode(mode);
    }

    /**
     * モード追加
     * 
     * @param mode
     *            {@link Mode}
     */
    public void addMode(final Mode mode) {
        switch (mListView.getMode()) {
        case PULL_FROM_START:
            if (mode == Mode.PULL_FROM_END) {
                mListView.setMode(Mode.BOTH);
            }
            break;
        case PULL_FROM_END:
            if (mode == Mode.PULL_FROM_START) {
                mListView.setMode(Mode.BOTH);
            }
            break;
        case DISABLED:
            mListView.setMode(mode);
            break;
        default:
            break;
        }
    }

    @Override
    public void removeHeaderView(final View headerView) {
        final ListView listView = getListView();
        if (listView != null) {
            listView.removeHeaderView(headerView);
        }
    }

    @Override
    public void addHeaderView(final int headerViewLayout) {
        final ListView listView = getListView();
        final View headerView = mLayoutInflater.inflate(headerViewLayout, null);
        listView.addHeaderView(headerView, null, false);
    }

    @Override
    public void addHeaderView(final View headerView) {
        final ListView listView = getListView();
        if (listView == null) {
            mHeaderView = headerView;
        } else {
            listView.addHeaderView(headerView, null, false);
        }
    }

    @Override
    public void setEmptyView() {
        if (mListViewEmptyId == 0) {
            return;
        }
        final View emptyView = getView(mListViewEmptyId);
        mListView.setEmptyView(emptyView);
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public U getItem(final int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public List<U> getItems() {
        return mAdapter.getItems();
    }

    @Override
    public void setItem(final U item, final int position) {
        if (item != null) {
            mAdapter.setItem(position, item);
        }
        onRefreshComplete();
    }

    @Override
    public void setItems(final List<U> items) {
        if (items != null) {
            mAdapter.setItems(items);
        }
        onRefreshComplete();
    }

    @Override
    public void insertItem(final U item, final int position) {
        if (item != null) {
            mAdapter.insert(item, position);
        }
        onRefreshComplete();
    }

    @Override
    public void addBefore(final U item) {
        if (item != null) {
            mAdapter.insert(item, 0);
        }
        onRefreshComplete();
    }

    @Override
    public void addBefore(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.insert(item, 0);
            }
        }
        onRefreshComplete();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void addAll(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            if (AplUtils.hasHoneycomb()) {
                mAdapter.addAll(items);
            } else {
                for (final U item : items) {
                    mAdapter.add(item);
                }
            }
        }
        onRefreshComplete();
    }

    @Override
    public void add(final U item) {
        if (item != null) {
            mAdapter.add(item);
        }
        onRefreshComplete();
    }

    @Override
    public int getCount() {
        if (mAdapter == null) {
            return 0;
        }
        return mAdapter.getCount();
    }

    @Override
    public T getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(final T adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public CustomListView getCustomListView() {
        return mListView;
    }

    @Override
    public ListView getListView() {
        return mListView == null ? null : mListView.getRefreshableView();
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener l) {
        if (l == null) {
            mListView.getRefreshableView().setSelector(android.R.color.transparent);
        }
        mListView.setOnItemClickListener(l);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener<ListView> l) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(Mode.PULL_FROM_END);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener<ListView> l, final Mode mode) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(mode);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener2<ListView> l) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(Mode.BOTH);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener2<ListView> l, final Mode mode) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(mode);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setPullLabel(final String includeStart, final String includeEnd) {
        if (includeStart != null) {
            mListView.getLoadingLayoutProxy(true, false).setPullLabel(includeStart);
        }
        if (includeEnd != null) {
            mListView.getLoadingLayoutProxy(false, true).setPullLabel(includeEnd);
        }
    }

    @Override
    public void setRefreshingLabel(final String includeStart, final String includeEnd) {
        if (includeStart != null) {
            mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(includeStart);
        }
        if (includeEnd != null) {
            mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(includeEnd);
        }
    }

    @Override
    public void setReleaseLabel(final String includeStart, final String includeEnd) {
        if (includeStart != null) {
            mListView.getLoadingLayoutProxy(true, false).setReleaseLabel(includeStart);
        }
        if (includeEnd != null) {
            mListView.getLoadingLayoutProxy(false, true).setReleaseLabel(includeEnd);
        }
    }

    @Override
    public void onRefreshComplete() {
        if (mListView != null) {
            mListView.onRefreshComplete();
        }
    }

    @Override
    public void destory() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        if (mListView != null) {
            mListView.getRefreshableView().setAdapter(null);
        }
        mAdapter = null;
        mActivity = null;
        mView = null;
        mDialog = null;
        mLayoutInflater = null;
    }
}
