package com.miya38.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

import com.miya38.exception.ApplicationException;
import com.miya38.list.adapter.CustomArrayAdapter;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.utils.guava.Preconditions;
import com.miya38.widget.CustomGallery;

/**
 * CustomGallery設定クラス
 * <p>
 * このクラスはCustomGalleryに関する基本的な設定を行うクラスである。<br>
 * CustomListViewに関する設定は全てこのクラスを使うことで操作を行うことで設定漏れ等を極力なくすのが目的である。<br>
 * 主にCustomGallery、Adapterに関する操作をこのクラスでは実装している。<br>
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
public class SettingGallery<T extends CustomArrayAdapter<U>, U> extends AbstractSettingGallery<T, U> {
    /** Adapter */
    private T mAdapter;
    /** Acitivity */
    private Activity mActivity;
    /** View */
    private View mView;
    /** Dialog */
    private Dialog mDialog;
    /** Gallery */
    private CustomGallery mGallery;
    /** 0件時に表示するためのView */
    private int mGalleryEmptyId;

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     */
    public SettingGallery(final T adapter, final Activity activity) { // NOPMD by y-miyazaki on 14/07/08 10:03
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
    public SettingGallery(final T adapter, final Dialog customDialog) {
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
     *            Galleryが設定されているview
     */
    public SettingGallery(final T adapter, final View view) {
        Preconditions.checkNotNull(adapter, "adapter should not be null.");
        Preconditions.checkNotNull(view, "activity should not be null.");
        this.mAdapter = adapter;
        this.mView = view;
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     */
    public final void setView(final int gridViewId) {
        mGallery = getView(gridViewId);
        mGallery.setAdapter(mAdapter);
        setEmptyView();
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     * @param gridViewEmptyId
     *            Empty時に表示するViewのId
     */
    public final void setView(final int gridViewId, final int gridViewEmptyId) {
        this.mGalleryEmptyId = gridViewEmptyId;
        setView(gridViewId);
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener) {
        setView(gridViewId, onItemClickListener, null, 0);
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param gridViewEmptyId
     *            Empty時に表示するViewのId
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener, final int gridViewEmptyId) {
        this.mGalleryEmptyId = gridViewEmptyId;
        setView(gridViewId, onItemClickListener, null, gridViewEmptyId);
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onItemSelectedListener
     *            リスト切り替え時のリスナー
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener, final OnItemSelectedListener onItemSelectedListener) {
        setView(gridViewId, onItemClickListener, onItemSelectedListener, 0);
    }

    /**
     * Gallery設定(初期設定)
     * 
     * @param gridViewId
     *            Gallery自身(ex:R.id.Gallery)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onItemSelectedListener
     *            リスト切り替え時のリスナー
     * @param gridViewEmptyId
     *            Empty時に表示するViewのId
     */
    public final void setView(final int gridViewId, final OnItemClickListener onItemClickListener, final OnItemSelectedListener onItemSelectedListener, final int gridViewEmptyId) {
        mGallery = getView(gridViewId);
        mGallery.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnItemSelectedListener(onItemSelectedListener);
        setEmptyView();
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
    public final void setEmptyView() {
        if (mGalleryEmptyId == 0) {
            return;
        }
        final View emptyView = getView(mGalleryEmptyId);
        mGallery.setEmptyView(emptyView);
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
    }

    @Override
    public final void setItems(final List<U> items) {
        if (items != null) {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public final void insertItem(final U item, final int position) {
        if (item != null) {
            mAdapter.insert(item, position);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public final void addBefore(final U item) {
        if (item != null) {
            mAdapter.insert(item, 0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public final void addBefore(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.insert(item, 0);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public final void addAfter(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.add(item);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public final void addAfter(final U item) {
        if (item != null) {
            mAdapter.add(item);
            mAdapter.notifyDataSetChanged();
        }
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
    public final CustomGallery getCustomGallery() {
        return mGallery;
    }

    @Override
    public final Gallery getGallery() {
        return mGallery;
    }

    @Override
    public final void setOnItemClickListener(final OnItemClickListener l) {
        // if (l == null) {
        // mGallery.setSelector(android.R.color.transparent);
        // }
        mGallery.setOnItemClickListener(l);
    }

    @Override
    public final void setOnItemSelectedListener(final OnItemSelectedListener l) {
        // if (l == null) {
        // mGallery.setSelector(android.R.color.transparent);
        // }
        mGallery.setOnItemSelectedListener(l);
    }

    @Override
    public void destory() {
        mAdapter.clear();
        mAdapter = null;
        mActivity = null;
        mView = null;
        mDialog = null;
        mGallery = null;
    }
}