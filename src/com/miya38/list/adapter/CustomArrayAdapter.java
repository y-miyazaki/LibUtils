package com.miya38.list.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;

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
     * @param convertView
     * @param parent
     * @return
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
    public CustomArrayAdapter(Context context, int itemResourceId, List<T> items) {
        super(context, itemResourceId, items);
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
     * @param item
     */
    public void setItem(int position, T item) {
        final T object = getItem(position);
        insert(item, position);
        remove(object);
    }

    /**
     * アイテムリスト設定
     *
     * @param items
     */
    public void setItems(List<T> items) {
        clear();
        for (final T item : items) {
            add(item);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mSeeLastPosition = -1;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
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
    public void onScrollStateChanged(int scrollState) {
        this.mScrollState = scrollState;
        // if (scrollState <= OnScrollListener.SCROLL_STATE_IDLE) {
        // displayCache(mView);
        // }
    }
}
