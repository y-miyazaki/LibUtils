package com.miya38.list;

import com.miya38.widget.CustomViewPager;

import java.util.List;

/**
 * ViewPager抽象化クラス
 * 
 * @author y-miyazaki
 * 
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 */
public abstract class AbstractSettingViewPager<T, U> {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();

    /**
     * ViewPager設定(中身のクリア)
     */
    public abstract void clear();

    /**
     * ViewPager設定(getItem)
     * 
     * @param position
     *            アイテム設定位置
     * @return アイテム
     */
    public abstract U getItem(int position);

    /**
     * ViewPager設定(getItems)
     * 
     * @return アイテム
     */
    public abstract List<U> getItems();

    /**
     * ViewPager設定(setItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void setItem(U item, int position);

    /**
     * ViewPager設定(setItems)
     * 
     * @param items
     *            アイテム
     */
    public abstract void setItems(List<U> items);

    /**
     * ViewPager設定(insertItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void insertItem(U item, int position);

    /**
     * ViewPager設定(addView)(before)
     * 
     * @param item
     *            アイテム
     */
    public abstract void addBefore(U item);

    /**
     * ViewPager設定(addView)(before)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addBefore(List<U> items);

    /**
     * ViewPager設定(addView)(after)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addAfter(List<U> items);

    /**
     * ViewPager設定(addView)(after)
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
     * CustomViewPager取得
     * 
     * @return CustomViewPager
     */
    public abstract CustomViewPager getCustomViewPager();

    /**
     * 本クラス内データを全てクリアする。
     */
    public abstract void destroy();
}
