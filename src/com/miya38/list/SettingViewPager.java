package com.miya38.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.miya38.list.adapter.CustomPagerAdapter;
import com.miya38.utils.CollectionUtils;
import com.miya38.utils.LogUtils;
import com.miya38.utils.ViewHelper;
import com.miya38.widget.CustomViewPager;

/**
 * ノーローディングViewPager生成クラス
 * 
 * 
 * @author y-miyazaki
 * @param <T>
 *            Adapter
 * @param <U>
 *            Item
 */
public class SettingViewPager<T extends CustomPagerAdapter<U>, U> extends AbstractSettingViewPager<T, U> {
    /** Adapter */
    private T mAdapter;
    /** Acitivity */
    private Activity mActivity;
    /** View */
    private View mView;
    /** CustomDialog */
    private Dialog mDialog;
    /** ListView */
    private CustomViewPager mCustomViewPager;

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param mActivity
     *            Activityのthis
     */
    public SettingViewPager(final T adapter, final Activity activity) {
        LogUtils.d(TAG, "SettingViewPager1");
        this.mAdapter = adapter;
        this.mActivity = activity;
    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param view
     *            ListViewが設定されているView
     */
    public SettingViewPager(final T adapter, final View view) {
        LogUtils.d(TAG, "SettingViewPager2");
        this.mAdapter = adapter;
        this.mView = view;
    }

    /**
     * コンストラクタ
     * 
     * @param adapter
     *            リストビューに設定するAdapter
     * @param customDialog
     *            ダイアログ
     */
    public SettingViewPager(final T adapter, final Dialog customDialog) {
        LogUtils.d(TAG, "SettingViewPager3");
        this.mAdapter = adapter;
        this.mDialog = customDialog;
    }

    /**
     * ViewPager設定(初期設定)
     * 
     * @param viewPagerId
     *            ViewPagerのID(ex:R.id.ViewPager01)
     * @param pagerMargin
     *            ページ間マージン
     */
    public void setView(final int viewPagerId, final int pagerMargin) {
        setView(viewPagerId, 0, pagerMargin);
    }

    /**
     * ViewPager設定(初期設定)
     * 
     * @param viewPagerId
     *            ViewPagerのID(ex:R.id.ViewPager01)
     * @param viewPagerEmptyId
     *            Empty時に表示するViewのId
     * @param pagerMargin
     *            ページ間マージン
     */
    public void setView(final int viewPagerId, final int viewPagerEmptyId, final int pagerMargin) {
        if (mActivity != null) {
            mCustomViewPager = ViewHelper.findView(mActivity, viewPagerId);
        } else if (mView != null) {
            mCustomViewPager = ViewHelper.findView(mView, viewPagerId);
        } else if (mDialog != null) {
            mCustomViewPager = ViewHelper.findView(mDialog, viewPagerId);
        }
        mCustomViewPager.setOffscreenPageLimit(3);
        mCustomViewPager.setPageMargin(pagerMargin);
        mCustomViewPager.setPageMarginDrawable(null);
        mCustomViewPager.setAdapter(mAdapter);
        mCustomViewPager.setCurrentItem(0, true);
        setEmptyView(viewPagerEmptyId);
    }

    /**
     * ViewPager設定(初期設定)
     * 
     * @param viewPagerId
     *            View自身
     * @param onPageChangeListener
     *            アイテムが変わった際のリスナー
     * @param pagerMargin
     *            ページ間マージン
     */
    public void setView(final int viewPagerId, final OnPageChangeListener onPageChangeListener, final int pagerMargin) {
        setView(viewPagerId, onPageChangeListener, 0, pagerMargin, 1);
    }

    /**
     * ViewPager設定(初期設定)
     * 
     * @param viewPagerId
     *            View自身
     * @param onPageChangeListener
     *            アイテムが変わった際のリスナー
     * @param pagerMargin
     *            ページ間マージン
     * @param duration
     *            ページャーのスクロールスピード
     */
    public void setView(final int viewPagerId, final OnPageChangeListener onPageChangeListener, final int pagerMargin, final int duration) {
        setView(viewPagerId, onPageChangeListener, 0, pagerMargin, duration);
    }

    /**
     * ViewPager設定(初期設定)
     * 
     * @param viewPagerId
     *            View自身
     * @param onPageChangeListener
     *            アイテムが変わった際のリスナー
     * @param viewPagerEmptyId
     *            Empty時に表示するViewのId
     * @param pagerMargin
     *            ページ間マージン
     * @param duration
     *            ページャーのスクロールスピード
     */
    private void setView(final int viewPagerId, final OnPageChangeListener onPageChangeListener, final int viewPagerEmptyId, final int pagerMargin, final int duration) {
        if (mActivity != null) {
            mCustomViewPager = ViewHelper.findView(mActivity, viewPagerId);
        } else if (mView != null) {
            mCustomViewPager = ViewHelper.findView(mView, viewPagerId);
        } else if (mDialog != null) {
            mCustomViewPager = ViewHelper.findView(mDialog, viewPagerId);
        }
        mCustomViewPager.setOffscreenPageLimit(3);
        mCustomViewPager.setPageMargin(pagerMargin);
        mCustomViewPager.setPageMarginDrawable(null);
        mCustomViewPager.setAdapter(mAdapter);
        mCustomViewPager.setCurrentItem(0, true);
        mCustomViewPager.setOnPageChangeListener(onPageChangeListener);
        setEmptyView(viewPagerEmptyId);

        mCustomViewPager.setScrollDurationFactor(duration);
    }

    /**
     * 空の場合のレイアウト
     * 
     * @param viewPagerEmptyId
     */
    public void setEmptyView(final int viewPagerEmptyId) {
        if (viewPagerEmptyId == 0) {
            return;
        }
        View emptyView = null;
        if (mActivity != null) {
            emptyView = ViewHelper.findView(mActivity, viewPagerEmptyId);
        } else if (mView != null) {
            emptyView = ViewHelper.findView(mView, viewPagerEmptyId);
        } else if (mDialog != null) {
            emptyView = ViewHelper.findView(mDialog, viewPagerEmptyId);
        }
        if (emptyView != null) {
            // 0件ならば、空用のViewを表示する。
            if (mAdapter.getCount() <= 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void clear() {
        mAdapter.setItems(new ArrayList<U>());
        mAdapter.notifyDataSetChanged();
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
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setItems(final List<U> items) {
        if (items != null) {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void insertItem(final U item, final int position) {
        if (item != null) {
            mAdapter.insert(item, position);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addBefore(final U item) {
        if (item != null) {
            mAdapter.insert(item, 0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addBefore(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.insert(item, 0);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addAfter(final List<U> items) {
        if (!CollectionUtils.isNullOrEmpty(items)) {
            for (final U item : items) {
                mAdapter.add(item);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addAfter(final U item) {
        if (item != null) {
            mAdapter.add(item);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mAdapter == null ? 0 : mAdapter.getCount();
    }

    @Override
    public T getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(final T adapter) {
        mAdapter = adapter;
    }

    @Override
    public CustomViewPager getCustomViewPager() {
        return mCustomViewPager;
    }

    @Override
    public void destory() {
        mAdapter.clear();
        mAdapter = null;
        mActivity = null;
        mView = null;
        mDialog = null;
        mCustomViewPager.setOnPageChangeListener(null);
        mCustomViewPager.setAdapter(null);
    }
}
