package com.miya38.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

/**
 * htmlタグを解析してイメージを返せるようにする
 *
 * @author y-miyazaki
 */
public class ImageGetterImpl implements ImageGetter {
    /** Context */
    private final Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     *         {@link Context}
     */
    public ImageGetterImpl(final Context context) {
        mContext = context;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final Resources resources = mContext.getResources();
        // リソースIDから Drawable のインスタンスを取得
        final int id = resources.getIdentifier(source, "drawable", mContext.getPackageName());
        if (id == 0) {
            return null;
        }
        Drawable drawable = resources.getDrawable(id);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}
