package com.miya38.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.common.base.Preconditions;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.miya38.exception.ApplicationException;
import com.miya38.list.adapter.CustomArrayAdapter;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomGridView;

/**
 * CustomGridView設定クラス
 * <p>
 * このクラスはCustomGridViewに関する基本的な設定を行うクラスである。<br>
 * CustomListViewに関する設定は全てこのクラスを使うことで操作を行うことで設定漏れ等を極力なくすのが目的である。<br>
 * 主にCustomGridView、Adapterに関する操作をこのクラスでは実装している。<br>
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
public class SettingGridView<T extends CustomArrayAdapter<U>, U> extends AbstractSettingGridView<T, U> {
    /** Adapter */
    private T mAdapter;
    /** Acitivity */
    private Activity mActivity;
    /** View */
    private View mView;
    /** Dialog */
    private Dialog mDialog;
    /** GridView */
    private CustomGridView mGridView;
    /** 0件時に表示するためのView */
    private int mGridViewEmptyId;

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     */
    public SettingGridView(final T adapter, final Activity activity) { // NOPMD by y-miyazaki on 14/07/08 10:03
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
     */
    public SettingGridView(final T adapter, final Dialog customDialog) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(customDialog, "activity should not be null.");
        this.mAdapter = adapter;
        this.mDialog = customDialog;
    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param view
     *            GridViewが設定されているview
     */
    public SettingGridView(final T adapter, final View view) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(view, "activity should not be null.");
        this.mAdapter = adapter;
        this.mView = view;
    }

    /**
     * GridView設定(初期設定)
     * 
     * @param gridViewId
     *            GridView自身(ex:R.id.GridView)
     */
    public final void setView(final int gridViewId) {
        mGridView = getView(gridViewId);
        mGridView.setAdapter(mAdapter);
        setEmptyView();
    }

    /**
     * GridView設定(初期設定)
     * 
     * @param gridViewId
     *            GridView自身(ex:R.id.GridView)
     * @param gridViewEmptyId
     *            Empty時に表示するViewのId
     */
    public final void setView(final int gridViewId, final int gridViewEmptyId) {
        this.mGridViewEmptyId = gridViewEmptyId;
        setView(gridViewId);
    }

    /**
     * GridView設定(初期設定)
     * 
     * @param gridViewId
     *            GridView自身(ex:R.id.GridView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            プル時のリスナー
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<GridView> onRefreshListener) {
        mGridView = getView(gridViewId);
        mGridView.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnRefreshListener(onRefreshListener);
        setEmptyView();
    }

    /**
     * GridView設定(初期設定)
     * 
     * @param gridViewId
     *            GridView自身(ex:R.id.GridView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            スクロールリスナー
     * @param gridViewEmptyId
     *            Empty時に表示するViewのId
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<GridView> onRefreshListener, final int gridViewEmptyId) {
        this.mGridViewEmptyId = gridViewEmptyId;
        setView(gridViewId, onItemClickListener, onRefreshListener);
    }

    /**
     * View取得
     * 
     * @param id
     * @return
     */
    private <V extends View> V getView(final int id) {
        if (mActivity != null) {
            return ViewHelper.findView(mActivity, id);
        } else if (mView != null) {
            return ViewHelper.findView(mView, id);
        } else if (mDialog != null) {
            return ViewHelper.findView(mDialog, id);
        } else {
            throw new ApplicationException("you must set activity or view/layoutInflater.");
        }
    }

    @Override
    public final void setMode(final Mode mode) {
        mGridView.setMode(mode);
    }

    @Override
    public final void setEmptyView() {
        if (mGridViewEmptyId == 0) {
            return;
        }
        final View emptyView = getView(mGridViewEmptyId);
        mGridView.setEmptyView(emptyView);
    }

    @Override
    public final void clear() {
        mAdapter.setItems(new ArrayList<U>());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public final U getItem(final int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public List<U> getItems() {
        return mAdapter.getItems();
    }

    @Override
    public final void setItem(final U item, final int position) {
        if (item != null) {
            mAdapter.setItem(position, item);
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void setItems(final List<U> items) {
        if (items != null) {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void insertItem(final U item, final int position) {
        if (item != null) {
            mAdapter.insert(item, position);
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void addBefore(final U item) {
        if (item != null) {
            mAdapter.insert(item, 0);
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void addBefore(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.insert(item, 0);
            }
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void addAfter(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.add(item);
            }
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final void addAfter(final U item) {
        if (item != null) {
            mAdapter.add(item);
            mAdapter.notifyDataSetChanged();
        }
        mGridView.onRefreshComplete();
    }

    @Override
    public final int getCount() {
        return mAdapter == null ? 0 : mAdapter.getCount();
    }

    @Override
    public final T getAdapter() {
        return mAdapter;
    }

    @Override
    public final void setAdapter(final T adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public final CustomGridView getCustomGridView() {
        return mGridView;
    }

    @Override
    public final GridView getGridView() {
        return mGridView.getRefreshableView();
    }

    @Override
    public final void setOnItemClickListener(final OnItemClickListener l) {
        if (l == null) {
            mGridView.getRefreshableView().setSelector(android.R.color.transparent);
        }
        mGridView.setOnItemClickListener(l);
    }

    @Override
    public final void setOnRefreshListener(final OnRefreshListener<GridView> l) {
        if (l == null) {
            mGridView.setMode(Mode.DISABLED);
        } else {
            mGridView.setMode(Mode.PULL_FROM_END);
        }
        mGridView.setOnRefreshListener(l);
    }

    @Override
    public void destory() {
        mAdapter.clear();
        mAdapter = null;
        mActivity = null;
        mView = null;
        mDialog = null;
        mGridView = null;
    }
}
