package com.miya38.widget.callback;

/**
 * ソフトキーボード表示リスナーインタフェース
 * 
 * @author y-miyazaki
 * 
 */
public interface OnSoftKeyShownListener {
    /**
     * ソフトキーボード表示状態返却
     * 
     * @param isShown
     *            true:表示/ false:非表示
     */
    void onSoftKeyShown(boolean isShown);
}
