package com.miya38.list.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

/**
 * カスタムアダプタークラス
 *
 * @author y-miyazaki
 *
 * @param <T>
 */
public abstract class CustomPagerAdapter<T> extends PagerAdapter {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();
    /** NotifyOnChangeをする場合 */
    private static final boolean NOTIFY_ON_CHANGE = true;

    // ---------------------------------------------------------------
    // other
    // ---------------------------------------------------------------
    /** Context */
    private final Context mContext;
    /** アイテムリソースID */
    private final int mItemResourceId;
    /** 表示するアイテム */
    private List<T> mItems;
    /** リスト削除時排他フラグ */
    private final Object mLock = new Object();

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
    public CustomPagerAdapter(Context context, int itemResourceId, List<T> items) {
        this.mItemResourceId = itemResourceId;
        this.mContext = context;
        this.mItems = items == null ? new ArrayList<T>() : new ArrayList<T>(items);
    }

    /**
     * Context取得
     *
     * @return Context
     */
    protected Context getContext() {
        return this.mContext;
    }

    /**
     * アイテムリソースID取得
     *
     * @return アイテムリソースID
     */
    public int getItemResourceId() {
        return this.mItemResourceId;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    /**
     * 最後のポジションを返却する
     *
     * @return last position
     */
    public int getLastPosition() {
        return getCount() - 1;
    }

    /**
     * アイテムに追加
     *
     * @param object
     */
    public void add(T object) {
        if (mItems != null) {
            synchronized (mLock) {
                mItems.add(object);
                if (NOTIFY_ON_CHANGE) {
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * アイテムの指定位置に追加設定
     *
     * @param object
     * @param index
     */
    public void insert(T object, int index) {
        if (mItems != null) {
            synchronized (mLock) {
                mItems.add(index, object);
                if (NOTIFY_ON_CHANGE) {
                    notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * アイテム削除
     *
     * @param item
     *            削除対応アイテム
     */
    public void remove(T item) {
        if (mItems != null) {
            synchronized (mLock) {
                mItems.remove(item);
            }
        }
        if (NOTIFY_ON_CHANGE) {
            notifyDataSetChanged();
        }
    }

    /**
     * アイテム全削除
     */
    public void clear() {
        if (mItems != null) {
            synchronized (mLock) {
                mItems.clear();
            }
        }
        if (NOTIFY_ON_CHANGE) {
            notifyDataSetChanged();
        }
    }

    /**
     * アイテム取得
     *
     * @param position
     * @return item
     */
    public T getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item
     *            The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mItems.indexOf(item);
    }

    /**
     * position取得
     *
     * @param position
     * @return position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * アイテムの指定位置に設定
     *
     * @param position
     * @param item
     */
    public void setItem(int position, T item) {
        mItems.set(position, item);
    }

    /**
     * アイテムリスト取得
     *
     * @return items
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     * アイテムリスト設定
     *
     * @param items
     */
    public void setItems(List<T> items) {
        this.mItems = new ArrayList<T>(items);
    }
}
