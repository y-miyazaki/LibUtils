package com.miya38.utils;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;

/**
 * クリップボードユーティリティ
 * 
 * @author y-miyazaki
 */
public final class ClipboardUtils {
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ClipboardUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     * 
     * @param context
     *            {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    /**
     * クリップボードへのコピー
     * 
     * @param text
     *            コピー文字列
     */
    public static void copy(String text) {
        if (AplUtils.hasHoneycomb()) {
            copyToClipboard(text);
        } else {
            copyToClipboardUnder11(text);
        }
    }

    /**
     * Api Level11未満で使用されるClipboard処理
     * 
     * @param text
     *            コピー文字列
     */
    @SuppressWarnings("deprecation")
    private static void copyToClipboardUnder11(String text) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) sContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(text);
    }

    /**
     * Api Level11以上で使用されるClipboard処理
     * 
     * @param text
     *            コピー文字列
     */
    @TargetApi(11)
    private static void copyToClipboard(String text) {
        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) sContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = new ClipData.Item(text);
        String[] mimeTypes = new String[] {
                ClipDescription.MIMETYPE_TEXT_PLAIN
        };
        ClipData clip = new ClipData("data", mimeTypes, item);
        clipboardManager.setPrimaryClip(clip);
    }
}
