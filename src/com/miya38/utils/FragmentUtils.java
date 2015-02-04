package com.miya38.utils;

import java.io.Serializable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

/**
 * Fragmentユーティリティ
 *
 * @author y-miyazaki
 *
 */
public final class FragmentUtils {
    /** スライドin */
    private static int sInRight;
    /** スライドin */
    private static int sInLeft;
    /** スライドOut */
    private static int sOutRight;
    /** スライドOut */
    private static int sOutLeft;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private FragmentUtils() {

    }

    /**
     * カスタムアニメーション設定
     *
     * @param slideInRight
     *            画面遷移アニメーションID(右)
     * @param slideOutLeft
     *            画面戻りアニメーションID(左)
     * @param slideInLeft
     *            画面遷移アニメーションID(左)
     * @param slideOutRight
     *            画面戻りアニメーションID(右)
     */
    public static void setCustomAnimations(int slideInRight, int slideOutLeft, int slideInLeft, int slideOutRight) {
        sInRight = slideInRight;
        sInLeft = slideInLeft;
        sOutRight = slideOutRight;
        sOutLeft = slideOutLeft;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @return 引き渡された値
     */
    public static Integer getInt(Fragment fragment, String key) {
        if (fragment.getArguments() != null) {
            if (fragment.getArguments().getInt(key, 0) == 0) {
                return null;
            }
            return fragment.getArguments().getInt(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値
     */
    public static Integer getInt(Fragment fragment, String key, int defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getInt(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @return 引き渡された値
     */
    public static Long getLong(Fragment fragment, String key) {
        if (fragment.getArguments() != null) {
            if (fragment.getArguments().getLong(key, 0L) == 0L) {
                return null;
            }
            return fragment.getArguments().getLong(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値
     */
    public static Long getLong(Fragment fragment, String key, long defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @return 引き渡された値(未設定の場合はnullを返却する。)
     */
    public static String getString(Fragment fragment, String key) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getString(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static String getString(Fragment fragment, String key, String defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getString(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @return 引き渡された値(未設定の場合はnullを返却する。)
     */
    public static Boolean getBoolean(Fragment fragment, String key) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getBoolean(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @param defaultValue
     *            未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static Boolean getBoolean(Fragment fragment, String key, boolean defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *            フラグメントのインスタンス
     * @param key
     *            取得対象のキー
     * @return 引き渡された値
     */
    @SuppressWarnings("unchecked")
    public static <V extends Serializable> V getSerializable(Fragment fragment, String key) {
        if (fragment.getArguments() != null) {
            return (V) fragment.getArguments().getSerializable(key);
        }
        return null;
    }

    /**
     * Fragment遷移
     * <p>
     * Fragmentの画面遷移を行うメソッドです。<br>
     * 第1引数には画面遷移をしたいFragmentを指定してください。 <br>
     * 第2引数には、画面遷移する際にバックスタックから全てのフラグメントをポップするかを指定してください。<br>
     * </p>
     *
     * @param activity
     *            FragmentActivity
     * @param viewId
     *            Fragmentを設定するviewのR.id.????
     *
     * @param fragment
     *            Fragment
     * @param isAddToBackStack
     *            バックスタックを積むか？
     * @param isAllPopBackStack
     *            フラグメントバックスタックを全てポップするか？<br>
     *            true:全てバックスタックからポップする<br>
     *            false:そのままバックスタックを残す。<br>
     */
    public static void startFragment(FragmentActivity activity, int viewId, Fragment fragment, boolean isAddToBackStack, boolean isAllPopBackStack) {
        // Insert the fragment by replacing any existing fragment
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // もしtrueの場合はフラグメントスタックを全てpopする。
        if (isAllPopBackStack) {
            final int count = fragmentManager.getBackStackEntryCount();
            for (int i = 0; i < count; i++) {
                fragmentManager.popBackStack();
            }
        }
        // fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // カスタムアニメーションを設定されている場合
        if (sInRight != 0) {
            fragmentTransaction.setCustomAnimations(sInRight, sOutLeft, sOutLeft, sInRight);
        }
        fragmentTransaction.replace(viewId, fragment);

        if (isAddToBackStack) {
            // fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(null);
            fragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    // 何もしない
                }
            });
        }
        fragmentTransaction.commit();
    }

    /**
     * Fragment遷移
     * <p>
     * Fragmentの画面遷移を行うメソッドです。<br>
     * 第1引数には画面遷移をしたいFragmentを指定してください。 <br>
     * </p>
     *
     * @param activity
     *            FragmentActivity
     * @param viewId
     *            Fragmentを設定するviewのR.id.????
     * @param fragment
     *            Fragment
     * @param isAddToBackStack
     *            バックスタックを積むか？
     */
    public static void startFragment(FragmentActivity activity, int viewId, Fragment fragment, boolean isAddToBackStack) {
        // Insert the fragment by replacing any existing fragment
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // カスタムアニメーションを設定されている場合
        if (sInRight != 0) {
            fragmentTransaction.setCustomAnimations(sInRight, sOutLeft, sOutLeft, sInRight);
        }
        fragmentTransaction.replace(viewId, fragment);

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
            fragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    // 何もしない。
                }
            });
        }
        fragmentTransaction.commit();
    }

    /**
     * Fragmentスタック全削除
     * <p>
     * Fragmentスタック全削除を行うメソッドです。<br>
     * </p>
     *
     * @param activity
     *            FragmentActivity
     */
    public static void finishAllFragment(FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // もしtrueの場合はフラグメントスタックを全てpopする。
        // fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        // カスタムアニメーションを設定されている場合
        if (sInRight != 0) {
            fragmentTransaction.setCustomAnimations(sInRight, sOutLeft, sOutLeft, sInRight);
        }
        final int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            fragmentManager.popBackStack();
        }
        fragmentTransaction.commit();
    }

    /**
     * Fragmentスタック削除
     * <p>
     * Fragmentスタック削除を行うメソッドです。<br>
     * </p>
     *
     * @param activity
     *            FragmentActivity
     */
    public static void finishFragment(FragmentActivity activity, int viewId) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate();
        fragmentTransaction.commit();
    }

    /**
     * 現在のFragement取得
     *
     * @return Fragment
     */
    public static Fragment getVisibleFragment(FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for (final Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}
