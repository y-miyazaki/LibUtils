package com.miya38.list.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

/**
 * カスタムFragmentPagerAdapter
 */
public abstract class CustomFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    /** Context */
    private final Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     *            {@link Context}
     * @param fragmentManager
     *            {@link FragmentManager}
     */
    public CustomFragmentStatePagerAdapter(final Context context, final FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    // ////////////////////////////////////////////////
    // public
    // ////////////////////////////////////////////////
    /**
     * Context取得
     *
     * @return Context
     */
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            final FragmentManager manager = ((Fragment) object).getFragmentManager();
            final FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    /**
     * Fragment取得
     *
     * @param viewPager
     *            {@link ViewPager}
     * @param position
     *            ポジション
     * @param <V>
     *            Fragment継承の総称型
     * @return Fragment
     */
    @SuppressWarnings("unchecked")
    public <V extends Fragment> V findFragmentByPosition(final ViewPager viewPager, final int position) {
        return (V) instantiateItem(viewPager, position);
    }
}