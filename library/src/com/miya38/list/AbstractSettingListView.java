package com.miya38.list;

import java.util.List;

import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.widget.CustomListView;

/**
 * ListView抽象化クラス
 * 
 * @author y-miyazaki
 * 
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 */
public abstract class AbstractSettingListView<T, U> {
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
     * ヘッダー削除
     * 
     * @param headerView
     *            headerView
     */
    public abstract void removeHeaderView(View headerView);

    /**
     * ヘッダー追加
     * 
     * @param headerViewLayout
     *            ヘッダーに設定するレイアウトのID(ex:R.layout.header)
     */
    public abstract void addHeaderView(int headerViewLayout);

    /**
     * ヘッダー追加
     * 
     * @param headerView
     *            headerView
     */
    public abstract void addHeaderView(View headerView);

    /**
     * モード取得
     * 
     * @return 上方・下方のpull設定モード
     */
    public abstract Mode getMode();

    /**
     * モード設定
     * 
     * @param mode
     *            上方・下方のpull設定モード
     */
    public abstract void setMode(Mode mode);

    /**
     * ListView設定(中身のクリア)
     */
    public abstract void clear();

    /**
     * ListView設定(getItem)
     * 
     * @param position
     *            アイテム設定位置
     * @return アイテム
     */
    public abstract U getItem(int position);

    /**
     * ListView設定(getItems)
     * 
     * @return アイテム
     */
    public abstract List<U> getItems();

    /**
     * ListView設定(setItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void setItem(U item, int position);

    /**
     * ListView設定(setItems)
     * 
     * @param items
     *            アイテム
     */
    public abstract void setItems(List<U> items);

    /**
     * ListView設定(insertItem)
     * 
     * @param item
     *            アイテム
     * @param position
     *            アイテム設定位置
     */
    public abstract void insertItem(U item, int position);

    /**
     * ListView設定(addView)(before)
     * 
     * @param item
     *            アイテム
     */
    public abstract void addBefore(U item);

    /**
     * ListView設定(addView)(before)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addBefore(List<U> items);

    /**
     * ListView設定(addView)(after)
     * 
     * @param items
     *            アイテムリスト
     */
    public abstract void addAll(List<U> items);

    /**
     * ListView設定(addView)(after)
     * 
     * @param item
     *            アイテム
     */
    public abstract void add(U item);

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
     * CustomListView取得
     * 
     * @return CustomListView
     */
    public abstract CustomListView getCustomListView();

    /**
     * ListView取得
     * 
     * @return ListView
     */
    public abstract ListView getListView();

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
    public abstract void setOnRefreshListener(OnRefreshListener<ListView> l);

    /**
     * onRefreshListener設定
     * 
     * @param l
     *            OnRefreshListener
     * @param mode
     *            {@link Mode}
     */
    public abstract void setOnRefreshListener(OnRefreshListener<ListView> l, Mode mode);

    /**
     * onRefreshListener設定
     * 
     * @param l
     *            OnRefreshListener2
     */
    public abstract void setOnRefreshListener(OnRefreshListener2<ListView> l);

    /**
     * onRefreshListener設定
     * 
     * @param l
     *            OnRefreshListener2
     * @param mode
     *            {@link Mode}
     */
    public abstract void setOnRefreshListener(OnRefreshListener2<ListView> l, Mode mode);

    /**
     * プル時の表示文字列設定
     * 
     * @param includeStart
     *            上プル時に表示される文字列
     * @param includeEnd
     *            下プル時に表示される文字列
     */
    public abstract void setPullLabel(String includeStart, String includeEnd);

    /**
     * リフレッシュ中の表示文字列設定
     * 
     * @param includeStart
     *            上プル時に表示される文字列
     * @param includeEnd
     *            下プル時に表示される文字列
     */
    public abstract void setRefreshingLabel(String includeStart, String includeEnd);

    /**
     * 話した際の表示文字列設定
     * 
     * @param includeStart
     *            上プル時に表示される文字列
     * @param includeEnd
     *            下プル時に表示される文字列
     */
    public abstract void setReleaseLabel(String includeStart, String includeEnd);

    /**
     * リフレッシュコンプリート
     */
    public abstract void onRefreshComplete();

    /**
     * 本クラス内データを全てクリアする。
     */
    public abstract void destroy();
}
