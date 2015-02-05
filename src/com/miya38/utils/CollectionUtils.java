package com.miya38.utils;

import java.util.Collection;
import java.util.Map;

/**
 * コレクションのユーティリティを提供します。
 *
 * @author y-miyazaki
 *
 */
public final class CollectionUtils {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private CollectionUtils() {
    }

    /**
     * コレクションのnull/空であるかを返します。
     *
     * @param <T>
     *            Collection
     * @param c
     *            Collection
     * @return true:null/empty<br>
     *         false:not null/not empty
     */
    public static <T> boolean isNullOrEmpty(final Collection<T> c) {
        return c == null || c.isEmpty();
    }

    /**
     * コレクションのnull/空であるかを返します。
     *
     * @param <T>
     *            Collection
     * @param o
     *            配列
     * @return true:null<br>
     *         empty false:not null/not empty
     */
    public static <T> boolean isNullOrEmpty(final Object[] o) {
        return o == null || o.length == 0;
    }

    /**
     * マップのnull/空であるかを返します。
     *
     * @param <K>
     *            キー
     * @param <V>
     *            値
     * @param m
     *            マップ
     * @return true:null/empty<br>
     *         false:not null/not empty
     */
    public static <K, V> boolean isNullOrEmpty(final Map<K, V> m) {
        return m == null || m.isEmpty();
    }
}
