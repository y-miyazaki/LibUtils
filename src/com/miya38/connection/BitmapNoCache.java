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
    public Bitmap getBitmap(String url) {
        // キャッシュしないためnullを固定で返却
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // キャッシュしないので何もしない。
    }
}