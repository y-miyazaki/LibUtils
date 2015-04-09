package com.miya38.widget.callback;

import android.graphics.Bitmap;

/**
 * フリックリスナーインタフェース
 *
 * @author y-miyazaki
 *
 */
public interface OnNetworkImageViewListener {
    /**
     * イメージ設定イベント
     *
     * @param bm
     *            setImageBitmapで設定されたBitmap
     */
    void onSetImageBitmap(Bitmap bm);

}
