package com.miya38.connection;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Bitmap非キャッシュクラス
 * 
 * @author y-miyazaki
 * 
 */
public class BitmapNoCache implements ImageCache {
    @Override
    public Bitmap getBitmap(final String url) {
        // キャッシュしないためnullを固定で返却
        return null;
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        // キャッシュしないので何もしない。
    }
}