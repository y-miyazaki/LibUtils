package com.miya38.list;

import java.util.List;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.miya38.widget.CustomGridView;

/**
 * GridView抽象化クラス
 *
 * @author y-miyazaki
 *
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 */
public abstract class AbstractSettingGridView<T, U> {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();

    /**
     * 空の場合のレイアウト
     */
    public abstract void setEmptyView();

    // /**
    // * ヘッダー設定
    // *
    // * @param headerViewLayout
    // * ヘッダーに設定するレイアウトのID(ex:R.layout.header)
    // */
    // public abstract void setHeaderView(int headerViewLayout);
    //
    // /**
    // * ヘッダー設定
    // *
    // * @param headerView
    // * headerView
    // */
    // public abstract void setHeaderView(View headerView);

    /**
     * モード設定
     *
     * @param mode
     *            上方・下方のpull設定モード
     */
    public abstract void setMode(Mode mode);

    /**
     * GridView設定(中身のクリア)
     */
    public abstract void clear();

    /**
     * GridView設定(getItem)
     *
     * @param position
     *            アイテム設定位置
     * @return アイテム
     */
    public abstract U getItem(int position);

    /**
     * GridView設定(getItems)
     *
     * @return アイテム
     */
    public abstract List<U> getItems();

    /**
     * GridView設定(setItem)
     *
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void setItem(U item, int position);

    /**
     * GridView設定(setItems)
     *
     * @param items
     *            アイテム
     */
    public abstract void setItems(List<U> items);

    /**
     * GridView設定(insertItem)
     *
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void insertItem(U item, int position);

    /**
     * GridView設定(addView)(before)
     *
     * @param item
     *            アイテム
     */
    public abstract void addBefore(U item);

    /**
     * GridView設定(addView)(before)
     *
     * @param items
     *            アイテムリスト
     */
    public abstract void addBefore(List<U> items);

    /**
     * GridView設定(addView)(after)
     *
     * @param items
     *            アイテムリスト
     */
    public abstract void addAfter(List<U> items);

    /**
     * GridView設定(addView)(after)
     *
     * @param item
     *            アイテム
     */
    public abstract void addAfter(U item);

    /**
     * adapterにセットされている数を返却する
     *
     * @return カウント数
     */
    public abstract int getCount();

    /**
     * Adapter取得
     *
     * @return adapter アダプター
     */
    public abstract T getAdapter();

    /**
     * Adapter設定
     *
     * @param adapter
     *            アダプター
     */
    public abstract void setAdapter(T adapter);

    /**
     * CustomGridView取得
     *
     * @return CustomGridView
     */
    public abstract CustomGridView getCustomGridView();

    /**
     * GridView取得
     *
     * @return GridView
     */
    public abstract GridView getGridView();

    /**
     * onItemClickListenner設定
     *
     * @param l
     *            OnItemClickListener
     */
    public abstract void setOnItemClickListener(OnItemClickListener l);

    /**
     * onRefreshListener設定
     *
     * @param l
     *            OnRefreshListener
     */
    public abstract void setOnRefreshListener(OnRefreshListener<GridView> l);

    /**
     * 本クラス内データを全てクリアする。
     */
    public abstract void destory();
}
