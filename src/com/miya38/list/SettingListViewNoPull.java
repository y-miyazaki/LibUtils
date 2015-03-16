package com.miya38.list;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.list.adapter.CustomArrayAdapter;

/**
 * CustomListView設定クラス
 * <p>
 * ここではSettingListViewの内容に対して、
 * PULLDOWN時に単純にFooterを表示するのみでPULLのように引っ張らなくてもイベントを投げるように修正したクラス
 * </p>
 *
 *
 * @param <T>
 *            Adapter
 * @param <U>
 *            item
 *
 * @author y-miyazaki
 * @param <T>
 * @param <U>
 */
public class SettingListViewNoPull<T extends CustomArrayAdapter<U>, U> extends SettingListView<T, U> implements OnScrollListener {
    /** ScrollListnerからコールするために内部で保持する。 */
    private OnRefreshListener<ListView> mOnRefreshListener;
    /** ScrollListnerからコールするために内部で保持する。 */
    private OnRefreshListener2<ListView> mOnRefreshListener2;
    /**
     * Modeは、ScrollListnerからコールするために内部で保持する。実際にListViewに渡すmodeにBOTH/
     * PULL_ENDは渡さない。変換して渡す。
     */
    private Mode mMode;
    /** Footer View(Staticに保持しなければ消せないので) */
    private final View mFooterView;
    /** リフレッシュ状態(PullUp時のみ) */
    private boolean mRefreshStatus;

    /**
     * コンストラクタ
     *
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     * @param layoutInflater
     *            {@link LayoutInflater}
     * @param footerViewResId
     *            フッターのリソースID
     */
    public SettingListViewNoPull(final T adapter, final Activity activity, final LayoutInflater layoutInflater, final int footerViewResId) {
        super(adapter, activity, layoutInflater);
        mFooterView = mLayoutInflater.inflate(footerViewResId, null);
    }

    /**
     * コンストラクタ
     *
     * @param adapter
     *            リストビューに設定するAdapter
     * @param dialog
     *            ダイアログ
     * @param layoutInflater
     *            layoutInflater
     * @param footerViewResId
     *            フッターのリソースID
     */
    public SettingListViewNoPull(final T adapter, final Dialog dialog, final LayoutInflater layoutInflater, final int footerViewResId) {
        super(adapter, dialog, layoutInflater);
        mFooterView = mLayoutInflater.inflate(footerViewResId, null);
    }

    /**
     * コンストラクタ
     *
     * @param adapter
     *            リストビューに設定するAdapter
     * @param view
     *            ListViewが設定されているView
     * @param layoutInflater
     *            layoutInflater
     * @param footerViewResId
     *            フッターのリソースID
     */
    public SettingListViewNoPull(final T adapter, final View view, final LayoutInflater layoutInflater, final int footerViewResId) {
        super(adapter, view, layoutInflater);
        mFooterView = mLayoutInflater.inflate(footerViewResId, null);
    }

    /**
     * ListView設定(初期設定)
     *
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            プル時のリスナー
     * @param mode
     *            Mode
     */
    @Override
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<ListView> onRefreshListener, final Mode mode) {
        mListView = getView(listViewId);
        if (mHeaderView != null) {
            addHeaderView(mHeaderView);
        }
        mListView.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnRefreshListener(onRefreshListener);
        setMode(mode);
        setEmptyView();
    }

    /**
     * ListView設定(初期設定)
     *
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener
     *            スクロールリスナー
     * @param mode
     *            Mode
     * @param listViewEmptyId
     *            Empty時に表示するViewのId
     */
    @Override
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener<ListView> onRefreshListener, final Mode mode, final int listViewEmptyId) {
        this.mListViewEmptyId = listViewEmptyId;
        setView(listViewId, onItemClickListener, onRefreshListener, mode);
    }

    /**
     * ListView設定(初期設定)
     *
     * @param listViewId
     *            ListView自身(ex:R.id.ListView)
     * @param onItemClickListener
     *            リスト選択時のリスナー
     * @param onRefreshListener2
     *            プル時のリスナー
     * @param mode
     *            Mode
     */
    @Override
    public void setView(final int listViewId, final OnItemClickListener onItemClickListener, final OnRefreshListener2<ListView> onRefreshListener2, final Mode mode) {
        mListView = getView(listViewId);
        if (mHeaderView != null) {
            addHeaderView(mHeaderView);
        }
        mListView.setAdapter(mAdapter);
        setOnItemClickListener(onItemClickListener);
        setOnRefreshListener(onRefreshListener2);
        setMode(mode);
        setEmptyView();
    }

    @Override
    public Mode getMode() {
        return mMode;
    }

    @Override
    public void setMode(final Mode mode) {
        mMode = mode;
        Mode tmpMode = mode;

        // 下PULLについては、PullToRefreshListViewの機能を使わず独自で実装しているため、Modeの切り替えを独自で切り替える。
        // PullToRefreshListViewでは、FooterViewが既に1つセットされているため、FooterViewCountは1から開始される。
        if (mode == Mode.BOTH) {
            tmpMode = Mode.PULL_FROM_START;
            setOnScrollListener(this);
            if (getListView().getFooterViewsCount() <= 1) {
                getListView().addFooterView(mFooterView, null, false);
            }
        } else if (mode == Mode.PULL_FROM_END) {
            tmpMode = Mode.DISABLED;
            setOnScrollListener(this);

            if (getListView().getFooterViewsCount() <= 1) {
                getListView().addFooterView(mFooterView, null, false);
            }
        } else {
            setOnScrollListener(null);
            getListView().removeFooterView(mFooterView);
        }
        mListView.setMode(tmpMode);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener<ListView> l) {
        super.setOnRefreshListener(l);
        mOnRefreshListener = l;
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener<ListView> l, final Mode mode) {
        super.setOnRefreshListener(l, mode);
        mOnRefreshListener = l;
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener2<ListView> l) {
        super.setOnRefreshListener(l);
        mOnRefreshListener2 = l;
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener2<ListView> l, final Mode mode) {
        super.setOnRefreshListener(l, mode);
        mOnRefreshListener2 = l;
    }

    /**
     * スクロールリスナー設定
     *
     * @param l
     *            {@link OnScrollListener}
     */
    public void setOnScrollListener(final OnScrollListener l) {
        mListView.setOnScrollListener(l);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // 何もしない。
    }

    @Override
    public synchronized void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        // スクロールしていて且つラストポジションの場合のみ
        // または、見えている数とトータルが同じだと永久ループなので除外する。
        if (visibleItemCount != totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
            if (mMode == Mode.BOTH || mMode == Mode.PULL_FROM_END) {
                if (!mRefreshStatus) {
                    if (mOnRefreshListener2 != null) {
                        mOnRefreshListener2.onPullUpToRefresh(mListView);
                    }
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh(mListView);
                    }
                    mRefreshStatus = true;
                }
            }
        }
    }

    @Override
    public void onRefreshComplete() {
        super.onRefreshComplete();
        // リフレッシュ状態をリセットする。
        mRefreshStatus = false;
    }

    @Override
    public void destory() {
        super.destory();
        mOnRefreshListener = null;
        mOnRefreshListener2 = null;
    }
}