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
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** TAG */
    private static final String TAG = AnimationListenerAdapter.class.getSimpleName();
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
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
     *            Fragment
     */
    public AnimationListenerAdapter(final Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * コンストラクタ
     * 
     * @param activity
     *            Activity
     */
    public AnimationListenerAdapter(final Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public final void onAnimationEnd(final Animation animation) {
        LogUtils.d(TAG, "onAnimationEnd");
        if (mFragment != null && !mFragment.isDetached() && !mFragment.isRemoving() && mFragment.getView() != null) {
            onAnimationEndCustom(animation);
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            onAnimationEndCustom(animation);
        }
    }

    @Override
    public final void onAnimationRepeat(final Animation animation) {
        LogUtils.d(TAG, "onAnimationRepeat");
        if (mFragment != null && !mFragment.isDetached() && !mFragment.isRemoving() && mFragment.getView() != null) {
            onAnimationRepeatCustom(animation);
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            onAnimationRepeatCustom(animation);
        }
    }

    @Override
    public final void onAnimationStart(final Animation animation) {
        LogUtils.d(TAG, "onAnimationStart");
        if (mFragment != null && !mFragment.isDetached() && !mFragment.isRemoving() && mFragment.getView() != null) {
            onAnimationStartCustom(animation);
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            onAnimationStartCustom(animation);
        }
    }

    /**
     * onAnimationEndのカスタムアダプター
     * 
     * @param animation
     *            {@link Animation}
     */
    public void onAnimationEndCustom(final Animation animation) {
    }

    /**
     * onAnimationRepeatのカスタムアダプター
     * 
     * @param animation
     *            {@link Animation}
     */
    public void onAnimationRepeatCustom(final Animation animation) {
    }

    /**
     * onAnimationEndのカスタムアダプター
     * 
     * @param animation
     *            {@link Animation}
     */
    public void onAnimationStartCustom(final Animation animation) {
    }
}