package com.miya38.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.miya38.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * カスタムアダプタークラス
 * <p>
 * 本クラスは以下に機能を有する。
 * <ul>
 * <li>setItem/getItems/setItems/getItemResourceId等のArrayAdapterにはない機能を有する。</li>
 * <li>最後に表示されたポジションを保持する<br>
 * {@link CustomArrayAdapter#getSeeLastPosition()}</li>
 * </ul>
 * </p>
 *
 * @param <T>
 *         item
 * @author y-miyazaki
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
    /** 一番最後に見たポジション */
    private int mSeeLastPosition;

    // ---------------------------------------------------------------
    // abstract method
    // ---------------------------------------------------------------

    /**
     * getViewのカスタムメソッド
     * <p>
     * GridViewが0ポジションを連打するのを防ぐための対応
     * </p>
     *
     * @param position
     *         ポジション
     * @param convertView
     *         View
     * @param parent
     *         ViewGroup
     * @return View
     */
    protected abstract View getViewCustom(int position, View convertView, ViewGroup parent);

    /**
     * コンストラクタ
     *
     * @param context
     *         Context
     * @param itemResourceId
     *         アイテムリソースID
     * @param items
     *         リストアイテム
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
     *         ポジション
     * @param item
     *         アイテム
     */
    public void setItem(final int position, final T item) {
        final T object = getItem(position);
        setNotifyOnChange(false);
        insert(item, position);
        remove(object);
        notifyDataSetChanged();
    }

    /**
     * アイテムリスト設定
     *
     * @param items
     *         アイテムリスト
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
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // ラストポジションより現在のポジションの方が大きい場合はラストポジションに登録する。
        if (mSeeLastPosition < position) {
            mSeeLastPosition = position;
        }
        // 0ポジションで見えないポジションが最初のポジションの場合は捨てる
        return getViewCustom(position, convertView, parent);
    }
}
