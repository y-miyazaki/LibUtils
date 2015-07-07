package com.miya38.list;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

import com.miya38.widget.CustomGallery;

import java.util.List;

/**
 * Gallery抽象化クラス
 * 
 * @author y-miyazaki
 * 
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 */
@SuppressWarnings("deprecation")
public abstract class AbstractSettingGallery<T, U> {
    // ---------------------------------------------------------------
    // define
    // ---------------------------------------------------------------
    /** ログに付与するタグ */
    protected final String TAG = getClass().getSimpleName();

    /**
     * 空の場合のレイアウト
     */
    public abstract void setEmptyView();

    /**
     * Gallery設定(中身のクリア)
     */
    public abstract void clear();

    /**
     * Gallery設定(getItem)
     * 
     * @param position
     *            アイテム設定位置
     * @return アイテム
     */
    public abstract U getItem(int position);

    /**
     * Gallery設定(getItems)
     * 
     * @return アイテム
     */
    public abstract List<U> getItems();

    /**
     * Gallery設定(setItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void setItem(U item, int position);

    /**
     * Gallery設定(setItems)
     * 
     * @param items
     *            アイテム
     */
    public abstract void setItems(List<U> items);

    /**
     * Gallery設定(insertItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void insertItem(U item, int position);

    /**
     * Gallery設定(addView)(before)
     * 
     * @param item
     *            アイテム
     */
    public abstract void addBefore(U item);

    /**
     * Gallery設定(addView)(before)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addBefore(List<U> items);

    /**
     * Gallery設定(addView)(after)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addAfter(List<U> items);

    /**
     * Gallery設定(addView)(after)
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
     * CustomGallery取得
     * 
     * @return CustomGallery
     */
    public abstract CustomGallery getCustomGallery();

    /**
     * Gallery取得
     * 
     * @return Gallery
     */
    public abstract Gallery getGallery();

    /**
     * onItemClickListenner設定
     * 
     * @param l
     *            OnItemClickListener
     */
    public abstract void setOnItemClickListener(OnItemClickListener l);

    /**
     * onItemSelectedListener設定
     * 
     * @param l
     *            OnItemSelectedListener
     */
    public abstract void setOnItemSelectedListener(OnItemSelectedListener l);

    /**
     * 本クラス内データを全てクリアする。
     */
    public abstract void destroy();
}
