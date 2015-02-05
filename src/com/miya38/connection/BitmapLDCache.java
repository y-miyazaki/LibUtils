package com.miya38.connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.miya38.utils.DiskLruCache;

/**
 * シングルトンBitmapメモリ/ディスクキャッシュクラス
 * <p>
 * このクラスは、返却する際にはメモリから優先的に返却し、存在しない場合はディスクから返却する。
 * メモリから優先的に取ることにより速度を重視しつつ、メモリがクリアされた場合でもディスクから取り出すことで安易なキャッシュクリアをしない設計としている。
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public class BitmapLDCache implements ImageCache {
    /** メモリキャッシュサイズ(5M) */
    private static final int MAX_BITMAP_DISKCACHE_BYTESIZE = 1024 * 1024 * 5;
    /** シングルトンBitmapCache */
    private static BitmapLDCache BitmapLruCacheAndDiskCache;
    /** LruCache */
    private final LruCache<String, Bitmap> mCache;
    /** DiskLruCache */
    private final DiskLruCache mDiskLruCache;

    /**
     * コンストラクタ Bitmapのキャッシュサイズ指定、LruCacheの設定を行う。
     * 
     * @param context
     *            {@link Context}
     */
    private BitmapLDCache(final Context context) {
        mCache = new LruCache<String, Bitmap>(MAX_BITMAP_DISKCACHE_BYTESIZE) {
            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mDiskLruCache = DiskLruCache.openCache(context, MAX_BITMAP_DISKCACHE_BYTESIZE);
    }

    /**
     * インスタンス取得
     * 
     * @param context
     *            {@link Context}
     * @return {@link BitmapLDCache}
     */
    public static BitmapLDCache getInstance(final Context context) {
        if (BitmapLruCacheAndDiskCache == null) {
            BitmapLruCacheAndDiskCache = new BitmapLDCache(context);
        }
        return BitmapLruCacheAndDiskCache;
    }

    @Override
    public Bitmap getBitmap(final String url) {
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        if (mDiskLruCache != null) {
            bitmap = mDiskLruCache.get(url);
            if (bitmap != null) {
                mCache.put(url, bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        mCache.put(url, bitmap);
        if (mDiskLruCache != null) {
            mDiskLruCache.put(url, bitmap);
        }
    }
}
