package com.miya38.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Fragmentユーティリティ
 *
 * @author y-miyazaki
 */
public final class FragmentUtils {
    /** スライドin */
    private static int sInRight;
    /** スライドOut */
    private static int sOutLeft;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private FragmentUtils() {

    }

    // ////////////////////////////////////////////////
    // public
    // ////////////////////////////////////////////////

    /**
     * カスタムアニメーション設定
     *
     * @param slideInRight
     *         画面遷移アニメーションID(右)
     * @param slideOutLeft
     *         画面戻りアニメーションID(左)
     * @param slideInLeft
     *         画面遷移アニメーションID(左)
     * @param slideOutRight
     *         画面戻りアニメーションID(右)
     */
    public static void setCustomAnimations(final int slideInRight, final int slideOutLeft, final int slideInLeft, final int slideOutRight) {
        sInRight = slideInRight;
        sOutLeft = slideOutLeft;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値
     */
    public static Integer getInt(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            if (fragment.getArguments().getInt(key, -1) == -1) {
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
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @param defaultValue
     *         未設定の場合に返却される値
     * @return 引き渡された値
     */
    public static Integer getInt(final Fragment fragment, final String key, final int defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getInt(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値
     */
    public static Long getLong(final Fragment fragment, final String key) {
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
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @param defaultValue
     *         未設定の場合に返却される値
     * @return 引き渡された値
     */
    public static Long getLong(final Fragment fragment, final String key, final long defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値(未設定の場合はnullを返却する。)
     */
    public static String getString(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getString(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @param defaultValue
     *         未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static String getString(final Fragment fragment, final String key, final String defaultValue) {
        if (fragment.getArguments() != null) {
            if (AplUtils.hasHoneycombMR1()) {
                return fragment.getArguments().getString(key, defaultValue);
            }
            return fragment.getArguments().getString(key);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値(未設定の場合はnullを返却する。)
     */
    public static String[] getStringArray(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getStringArray(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値(未設定の場合はnullを返却する。)
     */
    public static ArrayList<Integer> getIntArrayList(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getIntegerArrayList(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @param defaultValue
     *         未設定の場合に返却される値
     * @return 引き渡された値(未設定の場合はdefaultValueを返却する。)
     */
    public static Boolean getBoolean(final Fragment fragment, final String key, final boolean defaultValue) {
        if (fragment.getArguments() != null) {
            return fragment.getArguments().getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param <V>
     *         Serializable
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値
     */
    @SuppressWarnings("unchecked")
    public static <V extends Serializable> V getSerializable(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            return (V) fragment.getArguments().getSerializable(key);
        }
        return null;
    }

    /**
     * 前画面からデータを取得する
     *
     * @param <V>
     *         Parcelable
     * @param fragment
     *         フラグメントのインスタンス
     * @param key
     *         取得対象のキー
     * @return 引き渡された値
     */
    @SuppressWarnings("unchecked")
    public static <V extends Parcelable> V getParcelable(final Fragment fragment, final String key) {
        if (fragment.getArguments() != null) {
            return (V) fragment.getArguments().getParcelable(key);
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
     *         FragmentActivity
     * @param viewId
     *         Fragmentを設定するviewのR.id.????
     * @param fragment
     *         Fragment
     * @param isAddToBackStack
     *         バックスタックを積むか？
     * @param isAllPopBackStack
     *         フラグメントバックスタックを全てポップするか？<br>
     *         true:全てバックスタックからポップする<br>
     *         false:そのままバックスタックを残す。<br>
     */
    public static void startFragment(final FragmentActivity activity, final int viewId, final Fragment fragment, final boolean isAddToBackStack, final boolean isAllPopBackStack) {
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
     *         FragmentActivity
     * @param viewId
     *         Fragmentを設定するviewのR.id.????
     * @param fragment
     *         Fragment
     * @param isAddToBackStack
     *         バックスタックを積むか？
     */
    public static void startFragment(final FragmentActivity activity, final int viewId, final Fragment fragment, final boolean isAddToBackStack) {
        // Insert the fragment by replacing any existing fragment
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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
     *         FragmentActivity
     */
    public static void finishAllFragment(final FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // もしtrueの場合はフラグメントスタックを全てpopする。
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
     *         FragmentActivity
     * @param viewId
     *         View ID
     */
    public static void finishFragment(final FragmentActivity activity, final int viewId) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate();
        fragmentTransaction.commit();
    }

    /**
     * 現在のFragement取得
     *
     * @param activity
     *         {@link FragmentActivity}
     * @return 表示されているFragment
     */
    public static Fragment getVisibleFragment(final FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for (final Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    // ////////////////////////////////////////////////
    // private
    // ////////////////////////////////////////////////

    // ////////////////////////////////////////////////
    // inner class
    // ////////////////////////////////////////////////

}
