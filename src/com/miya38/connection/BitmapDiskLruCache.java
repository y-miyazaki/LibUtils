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
     * @param cacheSize
     *            バイト単位で指定する。1024 * 1024 * 5がデフォルト値
     */
    private BitmapDiskLruCache(final Context context, int cacheSize) {
        cacheSize = cacheSize == -1 ? MAX_BITMAP_DISKCACHE_BYTESIZE : cacheSize;
        mDiskLruCache = DiskLruCache.openCache(context, cacheSize);
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
            sBitmapDiskLruCashe = new BitmapDiskLruCache(context, -1);
        }
        return sBitmapDiskLruCashe;
    }

    /**
     * インスタンス取得
     *
     * @param context
     *            {@link Context}
     * @param cacheSize
     *            バイト単位で指定する。1024 * 1024 * 5がデフォルト値
     * @return {@link BitmapDiskLruCache}
     */
    public static BitmapDiskLruCache getInstance(final Context context, final int cacheSize) {
        if (sBitmapDiskLruCashe == null) {
            sBitmapDiskLruCashe = new BitmapDiskLruCache(context, cacheSize);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDiskLruCache.put(url, bitmap);
                }
            }).start();
        }
    }
}
