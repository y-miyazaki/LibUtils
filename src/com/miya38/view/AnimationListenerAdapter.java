package com.miya38.view;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.miya38.utils.LogUtils;

/**
 * アニメーションアダプター
 *
 * @author y-miyazaki
 *
 */
public abstract class AnimationListenerAdapter implements AnimationListener {
    /** TAG */
    private static final String TAG = AnimationListenerAdapter.class.getSimpleName();

    /** Fragment */
    private Fragment mFragment;
    /** Activity */
    private Activity mActivity;

    /**
     * コンストラクタ
     */
    public AnimationListenerAdapter() {
        // 何もしない。
    }

    /**
     * コンストラクタ
     *
     * @param fragment
     */
    public AnimationListenerAdapter(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * コンストラクタ
     *
     * @param mFragment
     */
    public AnimationListenerAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        LogUtils.d(TAG, "onAnimationEnd");
        if (mFragment != null && !mFragment.isDetached() && !mFragment.isRemoving() && mFragment.getView() != null) {
            onAnimationEndCustom(animation);
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            onAnimationEndCustom(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        LogUtils.d(TAG, "onAnimationRepeat");
    }

    @Override
    public void onAnimationStart(Animation animation) {
        LogUtils.d(TAG, "onAnimationStart");
    }

    /**
     * onAnimationEndのカスタムアダプター
     *
     * @param animation
     *            {@link Animation}
     */
    protected abstract void onAnimationEndCustom(Animation animation);
}