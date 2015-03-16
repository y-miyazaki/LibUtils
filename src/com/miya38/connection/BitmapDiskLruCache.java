package com.miya38.connection;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.miya38.utils.DiskLruCache;

/**
 * シングルトンBitmapディスクキャッシュクラス<br>
 * <p>
 * 最悪ディスクベースキャッシュが使えなくても、メモリベースキャッシュに移行する。
 * </p>
 *
 * @author y-miyazaki
 *
 */
public final class BitmapDiskLruCache implements ImageCache {
    /** メモリキャッシュサイズ(5M) */
    private static final int MAX_BITMAP_DISKCACHE_BYTESIZE = 1024 * 1024 * 5;
    /** シングルトンBitmapCache */
    private static BitmapDiskLruCache sBitmapDiskLruCashe;
    /** シングルトンBitmapCache */
    private BitmapLruCache mBitmapLruCashe;
    /** DiskLruCacheクラス */
    private final DiskLruCache mDiskLruCache;

    /**
     * コンストラクタ Bitmapのキャッシュサイズ指定、DiskLruCacheの設定を行う。
     *
     * @param context
     *            Context
     */
    private BitmapDiskLruCache(final Context context) {
        mDiskLruCache = DiskLruCache.openCache(context, MAX_BITMAP_DISKCACHE_BYTESIZE);
        if (mDiskLruCache == null) {
            mBitmapLruCashe = BitmapLruCache.getInstance();
        }
    }

    /**
     * インスタンス取得
     *
     * @param context
     *            {@link Context}
     * @return {@link BitmapDiskLruCache}
     */
    public static BitmapDiskLruCache getInstance(final Context context) {
        if (sBitmapDiskLruCashe == null) {
            sBitmapDiskLruCashe = new BitmapDiskLruCache(context);
        }
        return sBitmapDiskLruCashe;
    }

    @Override
    public Bitmap getBitmap(final String url) {
        if (mDiskLruCache == null) {
            return mBitmapLruCashe.getBitmap(url);
        } else {
            return mDiskLruCache.get(url);
        }
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        if (mDiskLruCache == null) {
            mBitmapLruCashe.putBitmap(url, bitmap);
        } else {
            mDiskLruCache.put(url, bitmap);
        }
    }
}
