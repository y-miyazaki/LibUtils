package com.miya38.list.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;

import com.miya38.utils.CollectionUtils;

/**
 * カスタムアダプタークラス
 * <p>
 * 本クラスは以下に機能を有する。
 * <ul>
 * <li>setItem/getItems/setItemsgetItemResourceId等のArrayAdapterにはない機能を有する。</li>
 * <li>最後に表示されたポジションを保持する<br>
 * {@link CustomArrayAdapter#getLastPosition}</li>
 * </ul>
 * </p>
 *
 * @author y-miyazaki
 * @param <T>
 *            item
 */
public abstract class CustomArrayAdapter<T> extends ArrayAdapter<T> {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** アイテムリソースID */
    private final int mItemResourceId;

    /** ListView */
    private AbsListView mView;
    /** スクロール状態 */
    private int mScrollState;
    /** view */
    private int mFirstVisibleItem;
    /** 見えているアイテム数 */
    private int mVisibleItemCount;
    /** 全アイテム数 */
    private int mTotalItemCount;
    /** 一番最後に見たポジション */
    private int mSeeLastPosition;

    // ---------------------------------------------------------------
    // abstract method
    // ---------------------------------------------------------------
    // /**
    // * キャッシュされたものを表示する
    // *
    // * @param view
    // * View
    // */
    // public abstract void displayCache(AbsListView view);
    /**
     * getViewのカスタムメソッド
     * <p>
     * GridViewが0ポジションを連打するのを防ぐための対応
     * </p>
     *
     * @param position
     *            ポジション
     * @param convertView
     *            View
     * @param parent
     *            ViewGroup
     * @return View
     */
    protected abstract View getViewCustom(int position, View convertView, ViewGroup parent);

    /**
     * コンストラクタ
     *
     * @param context
     *            Context
     * @param itemResourceId
     *            アイテムリソースID
     * @param items
     *            リストアイテム
     */
    public CustomArrayAdapter(final Context context, final int itemResourceId, final List<T> items) {
        super(context, itemResourceId, items == null ? new ArrayList<T>() : items);
        this.mItemResourceId = itemResourceId;
    }

    /**
     * アイテムリソースID取得
     *
     * @return アイテムリソースID
     */
    public int getItemResourceId() {
        return this.mItemResourceId;
    }

    /**
     * レイアウトインフレータ取得
     *
     * @return レイアウトインフレータ
     */
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    /**
     * レイアウトViewを取得
     *
     * @return レイアウトView
     */
    public View getLayoutView() {
        return LayoutInflater.from(getContext()).inflate(mItemResourceId, null);
    }

    /**
     * 最後のポジションを返却する
     *
     * @return last position
     */
    public int getSeeLastPosition() {
        return mSeeLastPosition;
    }

    /**
     * アイテム全取得
     *
     * @return アイテムリスト
     */
    public List<T> getItems() {
        final List<T> items = new ArrayList<T>();
        final int count = getCount();
        for (int i = 0; i < count; i++) {
            items.add(getItem(i));
        }
        return items;
    }

    /**
     * アイテム設定
     *
     * @param position
     *            ポジション
     * @param item
     *            アイテム
     */
    public void setItem(final int position, final T item) {
        final T object = getItem(position);
        setNotifyOnChange(false);
        insert(item, position);
        remove(object);
        setNotifyOnChange(true);
        notifyDataSetChanged();
    }

    /**
     * アイテムリスト設定
     *
     * @param items
     *            アイテムリスト
     */
    public void setItems(final List<T> items) {
        setNotifyOnChange(false);
        final List<T> tmpItemList = new ArrayList<T>();
        if (!CollectionUtils.isNullOrEmpty(items)) {
            tmpItemList.addAll(items);
        }
        clear();
        if (!CollectionUtils.isNullOrEmpty(tmpItemList)) {
            for (final T item : tmpItemList) {
                add(item);
            }
        }
        setNotifyOnChange(true);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        try {
            return super.getCount();
        } catch (final NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void clear() {
        try {
            super.clear();
        } catch (final NullPointerException e) {
            // 握りつぶす
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mSeeLastPosition = -1;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // ラストポジションより現在のポジションの方が大きい場合はラストポジションに登録する。
        if (mSeeLastPosition < position) {
            mSeeLastPosition = position;
        }
        // 0ポジションで見えないポジションが最初のポジションの場合は捨てる
        return position == 0 && getFirstVisibleItem() > 0 ? convertView : getViewCustom(position, convertView, parent);
    }

    /**
     * 最初に見えているアイテム
     *
     * @return firstVisibleItem
     */
    public int getFirstVisibleItem() {
        return mFirstVisibleItem;
    }

    /**
     * 見えているアイテム数
     *
     * @return visibleItemCount
     */
    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    /**
     * 全アイテム数
     *
     * @return totalItemCount
     */
    public int getTotalItemCount() {
        return mTotalItemCount;
    }

    /**
     * ListViewインスタンスを取得
     *
     * @return AbsListView
     */
    public AbsListView getAbsListView() {
        return mView;
    }

    /**
     * スクロール状態を取得
     *
     * @return 以下のものをリターンする<br>
     *         {@link OnScrollListener#SCROLL_STATE_FLING}<br>
     *         {@link OnScrollListener#SCROLL_STATE_IDLE}<br>
     *         {@link OnScrollListener#SCROLL_STATE_TOUCH_SCROLL}<br>
     */
    public int getScrollState() {
        return mScrollState;
    }

    /**
     * スクロール位置を取得
     *
     * @param view
     *            View
     * @param firstVisibleItem
     *            開始アイテム
     * @param visibleItemCount
     *            見えるアイテム数
     * @param totalItemCount
     *            全アイテム数
     */
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        this.mView = view;
        this.mFirstVisibleItem = firstVisibleItem;
        this.mVisibleItemCount = visibleItemCount;
        this.mTotalItemCount = totalItemCount;
    }

    /**
     * スクロール状態を設定
     *
     * @param view
     *            View
     * @param scrollState
     *            スクロール状態<br>
     *            {@link OnScrollListener#SCROLL_STATE_FLING}<br>
     *            {@link OnScrollListener#SCROLL_STATE_IDLE}<br>
     *            {@link OnScrollListener#SCROLL_STATE_TOUCH_SCROLL}<br>
     */
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        this.mView = view;
        this.mScrollState = scrollState;
        // if (scrollState <= OnScrollListener.SCROLL_STATE_IDLE) {
        // displayCache(view);
        // }
    }

    /**
     * スクロール状態を設定
     *
     * @param scrollState
     *            スクロール状態<br>
     *            {@link OnScrollListener#SCROLL_STATE_FLING}<br>
     *            {@link OnScrollListener#SCROLL_STATE_IDLE}<br>
     *            {@link OnScrollListener#SCROLL_STATE_TOUCH_SCROLL}<br>
     */
    public void onScrollStateChanged(final int scrollState) {
        this.mScrollState = scrollState;
        // if (scrollState <= OnScrollListener.SCROLL_STATE_IDLE) {
        // displayCache(mView);
        // }
    }
}
