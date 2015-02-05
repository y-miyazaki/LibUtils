package com.miya38.connection;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * シングルトンBitmapメモリキャッシュクラス
 * <p>
 * メモリベースキャッシュで画像を管理する。
 * </p>
 * 
 * @author y-miyazaki
 * 
 */
public class BitmapLruCache implements ImageCache {
    /** メモリキャッシュサイズ(5M) */
    private static final int MAX_BITMAP_DISKCACHE_BYTESIZE = 1024 * 1024 * 5;
    /** シングルトンBitmapCache */
    private static BitmapLruCache sBitmapLruCashe;
    /** LruCache */
    private final LruCache<String, Bitmap> mCache;

    /**
     * コンストラクタ Bitmapのキャッシュサイズ指定、LruCacheの設定を行う。
     */
    private BitmapLruCache() {
        mCache = new LruCache<String, Bitmap>(MAX_BITMAP_DISKCACHE_BYTESIZE) {
            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * インスタンス取得
     * 
     * @return {@link BitmapLruCache}
     */
    public static BitmapLruCache getInstance() {
        if (sBitmapLruCashe == null) {
            sBitmapLruCashe = new BitmapLruCache();
        }
        return sBitmapLruCashe;
    }

    @Override
    public Bitmap getBitmap(final String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
