package com.miya38.connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.miya38.utils.DiskLruCache;
import com.miya38.utils.LogUtils;

/**
 * シングルトンBitmapメモリ/ディスクキャッシュクラス
 * <p>
 * このクラスは、返却する際にはメモリから優先的に返却し、存在しない場合はディスクから返却する。<br>
 * メモリから優先的に取ることにより速度を重視しつつ、メモリがクリアされた場合でもディスクから取り出すことで安易なキャッシュクリアをしない設計としている。
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public final class BitmapLDCache implements ImageCache {
    /** メモリキャッシュサイズ(5M) */
    private static final String TAG = "BitmapLDCache";

    /** メモリキャッシュサイズ(5M) */
    private static final int MAX_BITMAP_DISKCACHE_BYTESIZE = 1024 * 1024 * 5;
    /** シングルトンBitmapCache */
    private static BitmapLDCache sBitmapLruCacheAndDiskCache;
    /** LruCache */
    private final LruCache<String, Bitmap> mCache;
    /** DiskLruCache */
    private final DiskLruCache mDiskLruCache;

    /**
     * コンストラクタ Bitmapのキャッシュサイズ指定、LruCacheの設定を行う。
     * 
     * @param context
     *            {@link Context}
     * @param cacheSize
     *            バイト単位で指定する。1024 * 1024 * 5がデフォルト値
     */
    private BitmapLDCache(final Context context, int cacheSize) {
        cacheSize = cacheSize == -1 ? MAX_BITMAP_DISKCACHE_BYTESIZE : cacheSize;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mDiskLruCache = DiskLruCache.openCache(context, cacheSize * 2);
    }

    /**
     * インスタンス取得
     * 
     * @param context
     *            {@link Context}
     * @return {@link BitmapLDCache}
     */
    public static BitmapLDCache getInstance(final Context context) {
        if (sBitmapLruCacheAndDiskCache == null) {
            sBitmapLruCacheAndDiskCache = new BitmapLDCache(context, -1);
        }
        return sBitmapLruCacheAndDiskCache;
    }

    /**
     * インスタンス取得
     * 
     * @param context
     *            {@link Context}
     * @param cacheSize
     *            バイト単位で指定する。1024 * 1024 * 5がデフォルト値
     * @return {@link BitmapLDCache}
     */
    public static BitmapLDCache getInstance(final Context context, int cacheSize) {
        if (sBitmapLruCacheAndDiskCache == null) {
            sBitmapLruCacheAndDiskCache = new BitmapLDCache(context, cacheSize);
        }
        return sBitmapLruCacheAndDiskCache;
    }

    @Override
    public Bitmap getBitmap(final String url) {
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            LogUtils.d(TAG, "memory cache url = %s", url);
            return bitmap;
        }
        if (mDiskLruCache != null) {
            bitmap = mDiskLruCache.get(url);
            if (bitmap != null) {
                LogUtils.d(TAG, "disk cache url = %s", url);
                mCache.put(url, bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCache.put(url, bitmap);
                if (mDiskLruCache != null) {
                    mDiskLruCache.put(url, bitmap);
                }
            }
        }).start();
    }
}
