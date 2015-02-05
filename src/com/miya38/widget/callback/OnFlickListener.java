package com.miya38.widget.callback;

/**
 * フリックリスナーインタフェース
 * 
 * @author y-miyazaki
 * 
 */
public interface OnFlickListener {
    /**
     * バックフリックイベント
     */
    void onBack();

    /**
     * フォーワードフリックイベント
     */
    void onForword();
}
