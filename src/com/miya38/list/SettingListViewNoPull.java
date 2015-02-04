package com.miya38.list;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.miya38.list.adapter.CustomArrayAdapter;

/**
 * CustomListView設定クラス
 * <p>
 * ここではSettingListViewの内容に対して、PULLDOWN時に単純にFooterを表示するのみでPULLのように引っ張らなくてもイベントを投げるように修正したクラス
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
    /** Modeは、ScrollListnerからコールするために内部で保持する。実際にListViewに渡すmodeにBOTH/PULL_ENDは渡さない。変換して渡す。 */
    private Mode mMode;
    /** Footer View(Staticに保持しなければ消せないので) */
    private final View mFooterView;
    /** リフレッシュ状態(PullUp時のみ) */
    private boolean mRefreshStatus;

    /**
     * コンストラクタ
     *
     * @param adapter
     *            リストビュー
     * @param adapter
     *            リストビューに設定するAdapter
     * @param activity
     *            Activityのthis
     * @param footerViewResId
     *            フッターのリソースID
     */
    public SettingListViewNoPull(T adapter, Activity activity, int footerViewResId) {
        super(adapter, activity);
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
    public SettingListViewNoPull(T adapter, Dialog dialog, LayoutInflater layoutInflater, int footerViewResId) {
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
    public SettingListViewNoPull(T adapter, View view, LayoutInflater layoutInflater, int footerViewResId) {
        super(adapter, view, layoutInflater);
        mFooterView = mLayoutInflater.inflate(footerViewResId, null);
    }

    @Override
    public Mode getMode() {
        return mMode;
    }

    @Override
    public void setMode(Mode mode) {
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
    public void setOnRefreshListener(OnRefreshListener<ListView> l) {
        super.setOnRefreshListener(l);
        mOnRefreshListener = l;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<ListView> l, Mode mode) {
        super.setOnRefreshListener(l, mode);
        mOnRefreshListener = l;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener2<ListView> l) {
        super.setOnRefreshListener(l);
        mOnRefreshListener2 = l;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener2<ListView> l, Mode mode) {
        super.setOnRefreshListener(l, mode);
        mOnRefreshListener2 = l;
    }

    public void setOnScrollListener(OnScrollListener l) {
        mListView.setOnScrollListener(l);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 何もしない。
    }

    @Override
    public synchronized void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
        if (mRefreshStatus) {
            mRefreshStatus = false;
        }
    }

    @Override
    public void destory() {
        super.destory();
        mOnRefreshListener = null;
        mOnRefreshListener2 = null;
    }
}