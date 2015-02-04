package com.miya38.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.common.base.Preconditions;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.R;
import com.miya38.exception.ApplicationException;
import com.miya38.list.adapter.CustomArrayAdapter;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomListView;

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
    /** Context */
    protected static Context sContext;
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
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param context
     *            {@link Context}
     */
    public static void configure(Context context) {
        sContext = context;
    }

    /**
     * コンストラクタ
     *
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     */
    public SettingListView(T adapter, Activity activity) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(activity, "activity should not be null.");
        this.mAdapter = adapter;
        this.mActivity = activity;
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
    public SettingListView(T adapter, Dialog customDialog, LayoutInflater layoutInflater) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(customDialog, "activity should not be null.");
        Preconditions.checkNotNull(layoutInflater, "layoutInflater should not be null.");
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
    public SettingListView(T adapter, View view, LayoutInflater layoutInflater) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(view, "view should not be null.");
        Preconditions.checkNotNull(layoutInflater, "layoutInflater should not be null.");
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
    public void setView(int listViewId) {
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
    public void setView(int listViewId, int listViewEmptyId) {
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
    public void setView(int listViewId, OnItemClickListener onItemClickListener, OnRefreshListener<ListView> onRefreshListener, Mode mode) {
        mListView = getView(listViewId);
        mListView.getLoadingLayoutProxy(true, true).setLoadingDrawable(sContext.getResources().getDrawable(R.drawable.common_icon_arrow_down));
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
    public void setView(int listViewId, OnItemClickListener onItemClickListener, OnRefreshListener<ListView> onRefreshListener, Mode mode, int listViewEmptyId) {
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
     * @param onRefreshListener
     *            プル時のリスナー
     * @param mode
     *            Mode
     */
    public void setView(int listViewId, OnItemClickListener onItemClickListener, OnRefreshListener2<ListView> onRefreshListener2, Mode mode) {
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
     * @param onRefreshListener
     *            スクロールリスナー
     * @param mode
     *            Mode
     * @param listViewEmptyId
     *            Empty時に表示するViewのId
     */
    public void setView(int listViewId, OnItemClickListener onItemClickListener, OnRefreshListener2<ListView> onRefreshListener2, Mode mode, int listViewEmptyId) {
        this.mListViewEmptyId = listViewEmptyId;
        setView(listViewId, onItemClickListener, onRefreshListener2, mode);
    }

    /**
     * View取得
     *
     * @param id
     * @return View
     */
    protected <V extends View> V getView(int id) {
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
    public void setMode(Mode mode) {
        mListView.setMode(mode);
    }

    public void addMode(Mode mode) {
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
    public void removeHeaderView(View headerView) {
        final ListView listView = getListView();
        if (listView != null) {
            listView.removeHeaderView(headerView);
        }
    }

    @Override
    public void addHeaderView(int headerViewLayout) {
        final ListView listView = getListView();
        final View headerView = mLayoutInflater.inflate(headerViewLayout, null);
        listView.addHeaderView(headerView, null, false);
    }

    @Override
    public void addHeaderView(View headerView) {
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
        mAdapter.setItems(new ArrayList<U>());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public U getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public List<U> getItems() {
        return mAdapter.getItems();
    }

    @Override
    public void setItem(U item, int position) {
        if (item != null) {
            mAdapter.setItem(position, item);
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void setItems(List<U> items) {
        if (items != null) {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void insertItem(U item, int position) {
        if (item != null) {
            mAdapter.insert(item, position);
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void addBefore(U item) {
        if (item != null) {
            mAdapter.insert(item, 0);
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void addBefore(List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.insert(item, 0);
            }
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void addAfter(List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.add(item);
            }
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public void addAfter(U item) {
        if (item != null) {
            mAdapter.add(item);
            mAdapter.notifyDataSetChanged();
        }
        onRefreshComplete();
    }

    @Override
    public int getCount() {
        if (mAdapter == null) {
            return 0;
        } else {
            return mAdapter.getCount();
        }
    }

    @Override
    public T getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(T adapter) {
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
    public void setOnItemClickListener(OnItemClickListener l) {
        if (l == null) {
            mListView.getRefreshableView().setSelector(android.R.color.transparent);
        }
        mListView.setOnItemClickListener(l);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<ListView> l) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(Mode.PULL_FROM_END);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<ListView> l, Mode mode) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(mode);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener2<ListView> l) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(Mode.BOTH);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener2<ListView> l, Mode mode) {
        if (l == null) {
            setMode(Mode.DISABLED);
        } else {
            setMode(mode);
        }
        mListView.setOnRefreshListener(l);
    }

    @Override
    public void setPullLabel(String includeStart, String includeEnd) {
        if (includeStart != null) {
            mListView.getLoadingLayoutProxy(true, false).setPullLabel(includeStart);
        }
        if (includeEnd != null) {
            mListView.getLoadingLayoutProxy(false, true).setPullLabel(includeEnd);
        }
    }

    @Override
    public void setRefreshingLabel(String includeStart, String includeEnd) {
        if (includeStart != null) {
            mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(includeStart);
        }
        if (includeEnd != null) {
            mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(includeEnd);
        }
    }

    @Override
    public void setReleaseLabel(String includeStart, String includeEnd) {
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
