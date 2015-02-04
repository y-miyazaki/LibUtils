package com.miya38.utils;

import java.util.Collection;

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
     * @param c
     * @return true:null/empty<br>
     *         false:not null/not empty
     */
    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return c == null || c.isEmpty();
    }

    /**
     * コレクションのnull/空であるかを返します。
     *
     * @param o
     * @return true:null<br>
     *         empty false:not null/not empty
     */
    public static <T> boolean isNullOrEmpty(Object[] o) {
        return o == null || o.length == 0;
    }
}
