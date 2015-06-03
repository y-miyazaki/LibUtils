package com.miya38.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 画像ユーティリティクラス
 */
public final class ImageUtils {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** モザイクのドット */
    private static final int MOZAIC_DOT = 16;
    /** JPEG/PNG品質 */
    private static final int IMAGE_QUALITY = 100;

    /**
     * 画像の種類
     */
    public enum IMAGE {
        /** png形式 */
        PNG,
        /** jpeg形式 */
        JPEG,
        /** webp形式 */
        WEBP
    }

    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ImageUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param context
     *         {@link Context}
     */
    public static void configure(final Context context) {
        sContext = context;
    }

    // static {
    // System.loadLibrary("Image");
    // }

    /* implementend by libplasma.so */

    // private static native void Mozaic(int[] pixels, int width, int height,
    // int originalWidth, int originalHeight, int
    // dot, int square);
    //
    // private static native void Grayscale(int[] pixels, int width, int
    // height);
    //
    // private static native void Negative(int[] pixels, int width, int height);
    //
    // private static native void Brightness(int[] pixels, int width, int
    // height, int setting);
    //
    // private static native void Contrast(int[] pixels, int width, int height,
    // int setting);
    //
    // private static native void Saturation(int[] pixels, int width, int
    // height, int setting);
    //
    // private static native void Sepia(int[] pixels, int width, int height);

    /**
     * イメージリサイズ処理(Bitmap→Bitmap)<br>
     * ※縮尺比を考慮し、幅に合わせ画像をリサイズする処理
     *
     * @param bitmap
     *         ビットマップ
     * @param w
     *         横幅
     * @param h
     *         縦幅
     * @return ビットマップ
     */
    public static Bitmap imageResize(final Bitmap bitmap, final int w, final int h) {
        // ----------------------------------------------------
        // チェック
        // ----------------------------------------------------
        if (bitmap == null) {
            return null;
        }

        final int width = bitmap.getWidth(); // ビットマップの横幅取得
        final int height = bitmap.getHeight(); // ビットマップの縦幅取得

        // calculate the scale - in this case = 0.4f
        final float scaleWidth = (float) w / width; // 横幅縮尺比
        final float scaleHeight = (float) h / height; // 縦幅縮尺比

        // createa matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the bit map
        if (scaleWidth < scaleHeight) { // 縮尺比が低いものに合わせる
            matrix.postScale(scaleWidth, scaleWidth); // 縮尺比変更
        } else { // 横幅が広い場合
            matrix.postScale(scaleHeight, scaleHeight); // 縮尺比変更
        }
        // recreate the new Bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * イメージリサイズ処理<br>
     * ※縮尺比を考慮し、幅に合わせ画像をリサイズする処理
     *
     * @param filename
     *         ビットマップ
     * @param w
     *         変更後の横幅
     * @param h
     *         変更後の縦幅
     * @return ビットマップ
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap imageResize(final String filename, final int w, final int h) {
        // ----------------------------------------------------
        // チェック
        // ----------------------------------------------------
        final File ffilename = new File(filename);
        if (!ffilename.exists()) { // ファイルが存在しているかチェック
            return null;
        }

        // ----------------------------------------------------
        // ここからメモリを考慮し、画面サイズ以下までBitmapサイズを縮小/拡大して読み込む処理
        // ----------------------------------------------------
        // この値をtrueにすると実際には画像を読み込まず、
        // 画像のサイズ情報だけを取得することができます。
        final Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options); // ファイルオープン

        // ----------------------------------------------------
        // 横幅縦幅取得後、options.inSampleSizeに指定する縮小比を指定する
        // ----------------------------------------------------
        final int scaleWidth = options.outWidth / w + 1; // 横幅縮尺比
        final int scaleHeight = options.outHeight / h + 1; // 縦幅縮尺比

        options.inJustDecodeBounds = false; // サイズ取得完了のため、元に戻す
        // inMutableはAPI11以上からのため。
        if (AplUtils.hasHoneycomb()) {
            options.inMutable = true;
        }
        options.inSampleSize = Math.max(scaleWidth, scaleHeight);
        options.inPurgeable = true;

        Bitmap tmpBitmap = BitmapFactory.decodeFile(filename, options);
        final Bitmap resizeBitmap = imageResize(tmpBitmap, w, h);
        if (tmpBitmap != null && !tmpBitmap.equals(resizeBitmap)) {
            tmpBitmap.recycle();
            tmpBitmap = null;
        }
        return resizeBitmap;
    }

    /**
     * イメージリサイズ処理(byte→Bitmap)<br>
     * ※縮尺比を考慮し、幅に合わせ画像をリサイズする処理
     *
     * @param byteData
     *         画像バイトデータ
     * @param w
     *         横幅
     * @param h
     *         縦幅
     * @return ビットマップ
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap imageResize(final byte[] byteData, final int w, final int h) {
        // ----------------------------------------------------
        // チェック
        // ----------------------------------------------------
        if (byteData == null || byteData.length == 0) { // データの存在・長さチェック
            return null;
        }
        // ----------------------------------------------------
        // ここからメモリを考慮し、画面サイズ以下までBitmapサイズを縮小/拡大して読み込む処理
        // ----------------------------------------------------
        // この値をtrueにすると実際には画像を読み込まず、
        // 画像のサイズ情報だけを取得することができます。
        final Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteData, 0, byteData.length, options);

        // ----------------------------------------------------
        // 横幅縦幅取得後、options.inSampleSizeに指定する縮小比を指定する
        // ----------------------------------------------------
        final int scaleWidth = options.outWidth / w + 1; // 横幅縮尺比
        final int scaleHeight = options.outHeight / h + 1; // 縦幅縮尺比

        options.inJustDecodeBounds = false;
        // inMutableはAPI11以上からのため。
        if (AplUtils.hasHoneycomb()) {
            options.inMutable = true;
        }
        options.inSampleSize = Math.max(scaleWidth, scaleHeight);
        options.inPurgeable = true;

        Bitmap tmpBitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length, options); // 画像を縮小
        // or
        // 拡大して読み込み
        final Bitmap resizeBitmap = imageResize(tmpBitmap, w, h);
        if (tmpBitmap != null && !tmpBitmap.equals(resizeBitmap)) {
            tmpBitmap.recycle();
            tmpBitmap = null;
        }

        return resizeBitmap;
    }

    /**
     * 画像をリサイズする処理(Bitmap→Bitmap)
     *
     * @param bitmap
     *         ビットマップ
     * @param size
     *         サイズ圧縮(1,2,4,8,16,32...etc...)
     * @return ビットマップ
     */
    public static Bitmap imageResize(final Bitmap bitmap, final int size) {
        // ----------------------------------------------------
        // チェック
        // ----------------------------------------------------
        if (bitmap == null) {
            return null;
        }
        final int width = bitmap.getWidth(); // ビットマップの横幅取得
        final int height = bitmap.getHeight(); // ビットマップの縦幅取得

        // calculate the scale - in this case = 0.4f
        final float scaleWidth = (float) 1 / size; // 横幅縮尺比
        final float scaleHeight = (float) 1 / size; // 縦幅縮尺比

        // createa matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight); // 縮尺比変更

        // recreate the new Bitmap
        return Bitmap.createBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true), 0, 0, width, height, matrix, true);
    }

    /**
     * 縮尺比を変えずに画像を縮小する処理(リソース版)
     *
     * @param resource
     *         リソース
     * @param size
     *         サイズ圧縮(1,2,4,8,16,32...etc...)
     * @return ビットマップ
     */
    public static Bitmap imageResize(final int resource, final int size) {
        // 現在の表示メトリクスの取得
        final DisplayMetrics dm = sContext.getResources().getDisplayMetrics();

        final Options options = new BitmapFactory.Options();
        // この値をtrueにすると実際には画像を読み込まず、
        // 画像のサイズ情報だけを取得することができます。
        // options.inJustDecodeBounds = true;
        options.inSampleSize = size;
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        options.inDensity = dm.densityDpi;
        // Tell to gc that whether it needs free memory, the Bitmap can be
        // cleared
        options.inPurgeable = true;
        // Which kind of reference will be used to recover the Bitmap data after
        // being clear, when it will be used in
        // the future
        options.inInputShareable = true;

        return BitmapFactory.decodeResource(sContext.getResources(), resource, options);
    }

    /**
     * 縮尺比を変えずに画像を縮小する処理(ファイル版)
     *
     * @param file
     *         画像ファイル
     * @param size
     *         サイズ圧縮(1,2,4,8,16,32...etc...)
     * @return ビットマップ
     */
    public static Bitmap imageResize(final File file, final int size) {
        // 現在の表示メトリクスの取得
        final DisplayMetrics dm = sContext.getResources().getDisplayMetrics();

        final Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = size;
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        options.inDensity = dm.densityDpi;
        // Tell to gc that whether it needs free memory, the Bitmap can be
        // cleared
        options.inPurgeable = true;
        // Which kind of reference will be used to recover the Bitmap data after
        // being clear, when it will be used in
        // the future
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    /**
     * モザイク変換処理 画面全体に対してモザイクをかける
     *
     * @param bitmap
     *         ビットマップ
     * @return 変換後のビットマップ
     */
    public static Bitmap setMosaic(final Bitmap bitmap) {
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inSampleSize = 8;
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // モザイク用画像

        int width, height, originalWidth, originalHeight;
        originalWidth = rBitmap.getWidth(); // 画像横幅
        originalHeight = rBitmap.getHeight(); // 画像縦幅
        width = originalWidth / MOZAIC_DOT; // 画像横幅×モザイク幅
        height = originalHeight / MOZAIC_DOT; // 画像縦幅×モザイク幅

        final int[] pixels = new int[originalWidth * originalHeight]; // 設定するピクセルを領域取得
        rBitmap.getPixels(pixels, 0, originalWidth, 0, 0, originalWidth, originalHeight);

        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeのモザイク処理を呼び出す
            // Mozaic(pixels, width, height, originalWidth, originalHeight, dot,
            // square);
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    // ----------------------------------------------------
                    // モザイク変換
                    // 計算式:指定ドット数のカラー値平均値
                    // ----------------------------------------------------
                    final int moveX = i * MOZAIC_DOT;
                    final int moveY = j * MOZAIC_DOT;
                    /*
                     * // ドットの中の平均値を使う int rr = 0; int gg = 0; int bb = 0; for
                     * (int k=0;k<dot;k++) { for (int l=0;l<dot;l++){ int
                     * dotColor = pixels[moveX+k + (moveY+l) * originalWidth];
                     * rr += Color.red(dotColor); gg += Color.green(dotColor);
                     * bb += Color.blue(dotColor); } } rr = rr / square; gg = gg
                     * / square; bb = bb / square; for (int k=0;k<dot;k++) { for
                     * (int l=0;l<dot;l++){ pixels[moveX+k + (moveY+l) *
                     * originalWidth] = Color.rgb(rr, gg, bb); } }
                     */
                    for (int k = 1; k < MOZAIC_DOT; k++) {
                        for (int l = 1; l < MOZAIC_DOT; l++) {
                            // アルファ値＋赤＋緑＋青
                            pixels[moveX + k + (moveY + l) * originalWidth] = pixels[moveX + k - 1 + (moveY + l - 1) * originalWidth];
                        }
                    }
                }
            }
        }
        // 設定するピクセルを一気に指定
        rBitmap.setPixels(pixels, 0, originalWidth, 0, 0, originalWidth, originalHeight);
        return rBitmap;
    }

    /**
     * モザイク変換処理 指定した領域に対してモザイクをかける
     *
     * @param bitmap
     *         ビットマップ
     * @param left
     *         モザイクをかける開始X座標
     * @param top
     *         モザイクをかける開始Y座標
     * @param right
     *         モザイクをかける終了X座標
     * @param bottom
     *         モザイクをかける終了Y座標
     * @return 変換後のビットマップ
     */
    public static Bitmap setMosaic(final Bitmap bitmap, float left, float top, final float right, final float bottom) {
        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inSampleSize = 8;
        Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // モザイク用画像

        int width, height, originalWidth, originalHeight;
        originalWidth = rBitmap.getWidth(); // 画像横幅
        originalHeight = rBitmap.getHeight(); // 画像縦幅
        width = originalWidth / MOZAIC_DOT; // 画像横幅×モザイク幅
        height = originalHeight / MOZAIC_DOT; // 画像縦幅×モザイク幅

        // ----------------------------------------------------
        // 横幅or縦幅を超えて位置を指定されている場合は、モザイクをかけない
        // ----------------------------------------------------
        if (originalWidth < left || originalHeight < top) {
            rBitmap.recycle();
            rBitmap = null;
            return bitmap;
        }
        // ----------------------------------------------------
        // 横の開始位置がマイナスの場合は0に設定する
        // ----------------------------------------------------
        final int mozaicX = left > 0 ? (int) left : 0;
        left = left > 0 ? (int) left : 0;

        // ----------------------------------------------------
        // 縦の開始位置がマイナスの場合は0に設定する
        // ----------------------------------------------------
        final int mozaicY = top > 0 ? (int) top : 0;
        top = top > 0 ? (int) top : 0;

        // ----------------------------------------------------
        // 横幅を超えている場合は、最大位置までとするよう変換する
        // ----------------------------------------------------
        int mozaicWidth = (int) (right - left);
        if (originalWidth - left < right - left) {
            mozaicWidth = (int) (originalWidth - left);
        }
        mozaicWidth = mozaicWidth > 0 ? mozaicWidth : 0;

        // ----------------------------------------------------
        // 縦幅を超えている場合は、最大位置までとするよう変換する
        // ----------------------------------------------------
        int mozaicHeight = (int) (bottom - top);
        if (originalHeight - top < bottom - top) {
            mozaicHeight = (int) (originalHeight - top);
        }
        mozaicHeight = mozaicHeight > 0 ? mozaicHeight : 0;

        final int[] pixels = new int[originalWidth * originalHeight]; // 設定するピクセルを領域取得
        rBitmap.getPixels(pixels, 0, originalWidth, mozaicX, mozaicY, mozaicWidth, mozaicHeight);
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeのモザイク処理を呼び出す
            // Mozaic(pixels, width, height, originalWidth, originalHeight, dot,
            // square);
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    // ----------------------------------------------------
                    // モザイク変換
                    // 計算式:指定ドット数のカラー値平均値
                    // ----------------------------------------------------
                    final int moveX = i * MOZAIC_DOT;
                    final int moveY = j * MOZAIC_DOT;
                    /*
                     * // ドットの中の平均値を使う int rr = 0; int gg = 0; int bb = 0; for
                     * (int k=0;k<dot;k++) { for (int l=0;l<dot;l++){ int
                     * dotColor = pixels[moveX+k + (moveY+l) * originalWidth];
                     * rr += Color.red(dotColor); gg += Color.green(dotColor);
                     * bb += Color.blue(dotColor); } } rr = rr / square; gg = gg
                     * / square; bb = bb / square; for (int k=0;k<dot;k++) { for
                     * (int l=0;l<dot;l++){ pixels[moveX+k + (moveY+l) *
                     * originalWidth] = Color.rgb(rr, gg, bb); } }
                     */
                    for (int k = 1; k < MOZAIC_DOT; k++) {
                        for (int l = 1; l < MOZAIC_DOT; l++) {
                            // アルファ値＋赤＋緑＋青
                            pixels[moveX + k + (moveY + l) * originalWidth] = pixels[moveX + k - 1 + (moveY + l - 1) * originalWidth];
                        }
                    }
                }
            }
        }
        // 設定するピクセルを一気に指定
        rBitmap.setPixels(pixels, 0, originalWidth, mozaicX, mozaicY, mozaicWidth, mozaicHeight);
        return rBitmap;
    }

    /**
     * モザイク変換処理 指定した領域に対してモザイクをかける
     *
     * @param filename
     *         ファイル名
     * @param left
     *         モザイクをかける開始X座標
     * @param top
     *         モザイクをかける開始Y座標
     * @param right
     *         モザイクをかける終了X座標
     * @param bottom
     *         モザイクをかける終了Y座標
     * @return 変換後のビットマップ
     */
    public static Bitmap setMosaic(final String filename, float left, float top, final float right, final float bottom) {
        // long start = System.currentTimeMillis();
        int width, height, originalWidth, originalHeight;
        Bitmap rBitmap = null;

        // BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inSampleSize = 8;
        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)

            originalWidth = rBitmap.getWidth(); // 画像横幅
            originalHeight = rBitmap.getHeight(); // 画像縦幅
            width = originalWidth / MOZAIC_DOT; // 画像横幅×モザイク幅
            height = originalHeight / MOZAIC_DOT; // 画像縦幅×モザイク幅

            // ----------------------------------------------------
            // 横幅or縦幅を超えて位置を指定されている場合は、モザイクをかけない
            // ----------------------------------------------------
            if (originalWidth < left || originalHeight < top) {
                rBitmap.recycle();
                rBitmap = null;
                return rBitmap;
            }
            // ----------------------------------------------------
            // 横の開始位置がマイナスの場合は0に設定する
            // ----------------------------------------------------
            final int mozaicX = left > 0 ? (int) left : 0;
            left = left > 0 ? (int) left : 0;

            // ----------------------------------------------------
            // 縦の開始位置がマイナスの場合は0に設定する
            // ----------------------------------------------------
            final int mozaicY = top > 0 ? (int) top : 0;
            top = top > 0 ? (int) top : 0;

            // ----------------------------------------------------
            // 横幅を超えている場合は、最大位置までとするよう変換する
            // ----------------------------------------------------
            int mozaicWidth = (int) (right - left);
            if (originalWidth - left < right - left) {
                mozaicWidth = (int) (originalWidth - left);
            }
            mozaicWidth = mozaicWidth > 0 ? mozaicWidth : 0;

            // ----------------------------------------------------
            // 縦幅を超えている場合は、最大位置までとするよう変換する
            // ----------------------------------------------------
            int mozaicHeight = (int) (bottom - top);
            if (originalHeight - top < bottom - top) {
                mozaicHeight = (int) (originalHeight - top);
            }
            mozaicHeight = mozaicHeight > 0 ? mozaicHeight : 0;

            final int[] pixels = new int[originalWidth * originalHeight]; // 設定するピクセルを領域取得
            rBitmap.getPixels(pixels, 0, originalWidth, mozaicX, mozaicY, mozaicWidth, mozaicHeight);
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeのモザイク処理を呼び出す
                // Mozaic(pixels, width, height, originalWidth, originalHeight,
                // dot, square);
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        // ----------------------------------------------------
                        // モザイク変換
                        // 計算式:指定ドット数のカラー値平均値
                        // ----------------------------------------------------
                        final int moveX = i * MOZAIC_DOT;
                        final int moveY = j * MOZAIC_DOT;
                        /*
                         * // ドットの中の平均値を使う int rr = 0; int gg = 0; int bb = 0;
                         * for (int k=0;k<dot;k++) { for (int l=0;l<dot;l++){
                         * int dotColor = pixels[moveX+k + (moveY+l) *
                         * originalWidth]; rr += Color.red(dotColor); gg +=
                         * Color.green(dotColor); bb += Color.blue(dotColor); }
                         * } rr = rr / square; gg = gg / square; bb = bb /
                         * square; for (int k=0;k<dot;k++) { for (int
                         * l=0;l<dot;l++){ pixels[moveX+k + (moveY+l) *
                         * originalWidth] = Color.rgb(rr, gg, bb); } }
                         */
                        for (int k = 1; k < MOZAIC_DOT; k++) {
                            for (int l = 1; l < MOZAIC_DOT; l++) {
                                // アルファ値＋赤＋緑＋青
                                pixels[moveX + k + (moveY + l) * originalWidth] = pixels[moveX + k - 1 + (moveY + l - 1) * originalWidth];
                            }
                        }
                    }
                }
            }
            // 設定するピクセルを一気に指定
            rBitmap.setPixels(pixels, 0, originalWidth, mozaicX, mozaicY, mozaicWidth, mozaicHeight);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * グレースケール変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @return 変換後のビットマップ
     */
    public static Bitmap setGrayscale(final Bitmap bitmap) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)

        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeのグレースケール処理を呼び出す
            // Grayscale(pixels, width, height); // Native C method(Grayscale)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            // グレースケール計算を行う
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    // ----------------------------------------------------
                    // グレースケール変換
                    // 計算式:R * 0.299 + G * 587 + B * 114
                    // ----------------------------------------------------
                    final int color = pixels[i + j * width];
                    final int data = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
                    pixels[i + j * width] = Color.rgb(data, data, data);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを一気に指定
        return rBitmap;
    }

    /**
     * グレースケール変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @return 変換後のファイル名
     */
    public static Bitmap setGrayscale(final String filename) {
        // long start = System.currentTimeMillis();

        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得

            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeのグレースケール処理を呼び出す
                // Grayscale(pixels, width, height); // Native C
                // method(Grayscale)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                // グレースケール計算を行う
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        // ----------------------------------------------------
                        // グレースケール変換
                        // 計算式:R * 0.299 + G * 587 + B * 114
                        // ----------------------------------------------------
                        final int color = pixels[i + j * width];
                        final int data = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
                        pixels[i + j * width] = Color.rgb(data, data, data);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを一気に指定
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * ネガティブ変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @return 変換後のビットマップ
     */
    public static Bitmap setNegative(final Bitmap bitmap) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeのネガティブ処理を呼び出す
            // Negative(pixels, width, height); // Native C method(Negative)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    // ----------------------------------------------------
                    // ネガティブ変換
                    // 計算式:カラー最大値 - 現在カラー値
                    // ----------------------------------------------------
                    final int color = pixels[i + j * width];
                    final int rr = 255 - Color.red(color);
                    final int gg = 255 - Color.green(color);
                    final int bb = 255 - Color.blue(color);
                    pixels[i + j * width] = Color.rgb(rr, gg, bb);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rBitmap;
    }

    /**
     * ネガティブ変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @return 変換後のビットマップ
     */
    public static Bitmap setNegative(final String filename) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得

            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeのネガティブ処理を呼び出す
                // Negative(pixels, width, height); // Native C method(Negative)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        // ----------------------------------------------------
                        // ネガティブ変換
                        // 計算式:カラー最大値 - 現在カラー値
                        // ----------------------------------------------------
                        final int color = pixels[i + j * width];
                        final int rr = 255 - Color.red(color);
                        final int gg = 255 - Color.green(color);
                        final int bb = 255 - Color.blue(color);
                        pixels[i + j * width] = Color.rgb(rr, gg, bb);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * 明るさ変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @param setting
     *         0%～50%～100%までコントラストを変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setBrightness(final Bitmap bitmap, final int setting) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeの明るさ処理を呼び出す
            // Brightness(pixels, width, height, setting); // Native C
            // method(Brightness)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int color = pixels[i + j * width];
                    int rr = Color.red(color) * setting / 50;
                    int gg = Color.green(color) * setting / 50;
                    int bb = Color.blue(color) * setting / 50;
                    if (rr > 255) {
                        rr = 255;
                    } else if (rr < 0) {
                        rr = 0;
                    }
                    if (gg > 255) {
                        gg = 255;
                    } else if (gg < 0) {
                        gg = 0;
                    }
                    if (bb > 255) {
                        bb = 255;
                    } else if (bb < 0) {
                        bb = 0;
                    }
                    pixels[i + j * width] = Color.rgb(rr, gg, bb);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rBitmap;
    }

    /**
     * 明るさ変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @param setting
     *         0%～50%～100%まで明るさを変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setBrightness(final String filename, final int setting) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeの明るさ処理を呼び出す
                // Brightness(pixels, width, height, setting); // Native C
                // method(Brightness)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        final int color = pixels[i + j * width];
                        int rr = Color.red(color) * setting / 50;
                        int gg = Color.green(color) * setting / 50;
                        int bb = Color.blue(color) * setting / 50;
                        if (rr > 255) {
                            rr = 255;
                        } else if (rr < 0) {
                            rr = 0;
                        }
                        if (gg > 255) {
                            gg = 255;
                        } else if (gg < 0) {
                            gg = 0;
                        }
                        if (bb > 255) {
                            bb = 255;
                        } else if (bb < 0) {
                            bb = 0;
                        }
                        pixels[i + j * width] = Color.rgb(rr, gg, bb);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * コントラスト変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @param setting
     *         0～50%～100%までコントラストを変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setContrast(final Bitmap bitmap, final int setting) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeの明るさ処理を呼び出す
            // Contrast(pixels, width, height, setting); // Native C
            // method(contrast)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int color = pixels[i + j * width];
                    // ----------------------------------------------------
                    // コントラスト変換処理
                    // 計算式 ((現在のカラー - 128) * ？%) + 128
                    // ----------------------------------------------------
                    int rr = (Color.red(color) - 128) * setting / 50 + 128;
                    int gg = (Color.green(color) - 128) * setting / 50 + 128;
                    int bb = (Color.blue(color) - 128) * setting / 50 + 128;
                    if (rr > 255) {
                        rr = 255;
                    } else if (rr < 0) {
                        rr = 0;
                    }
                    if (gg > 255) {
                        gg = 255;
                    } else if (gg < 0) {
                        gg = 0;
                    }
                    if (bb > 255) {
                        bb = 255;
                    } else if (bb < 0) {
                        bb = 0;
                    }
                    pixels[i + j * width] = Color.rgb(rr, gg, bb);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rBitmap;
    }

    /**
     * コントラスト変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @param setting
     *         0～50%～100%までコントラストを変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setContrast(final String filename, final int setting) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeのコントラスト処理を呼び出す
                // Contrast(pixels, width, height, setting); // Native C
                // method(contrast)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        final int color = pixels[i + j * width];
                        // ----------------------------------------------------
                        // コントラスト変換処理
                        // 計算式 ((現在のカラー - 128) * ？%) + 128
                        // ----------------------------------------------------
                        int rr = (Color.red(color) - 128) * setting / 50 + 128;
                        int gg = (Color.green(color) - 128) * setting / 50 + 128;
                        int bb = (Color.blue(color) - 128) * setting / 50 + 128;
                        if (rr > 255) {
                            rr = 255;
                        } else if (rr < 0) {
                            rr = 0;
                        }
                        if (gg > 255) {
                            gg = 255;
                        } else if (gg < 0) {
                            gg = 0;
                        }
                        if (bb > 255) {
                            bb = 255;
                        } else if (bb < 0) {
                            bb = 0;
                        }
                        pixels[i + j * width] = Color.rgb(rr, gg, bb);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * 彩度変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @param setting
     *         0%～50%～100%まで彩度を変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setSaturation(final Bitmap bitmap, final int setting) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeの彩度処理を呼び出す
            // Saturation(pixels, width, height, setting); // Native C
            // method(Saturation)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int color = pixels[i + j * width];
                    int rr = Color.red(color);
                    int gg = Color.green(color);
                    int bb = Color.blue(color);
                    final int max = getRGBMax(rr, gg, bb);
                    rr = max - (max - rr) * (setting / 50);
                    gg = max - (max - gg) * (setting / 50);
                    bb = max - (max - bb) * (setting / 50);

                    if (rr > 255) {
                        rr = 255;
                    } else if (rr < 0) {
                        rr = 0;
                    }
                    if (gg > 255) {
                        gg = 255;
                    } else if (gg < 0) {
                        gg = 0;
                    }
                    if (bb > 255) {
                        bb = 255;
                    } else if (bb < 0) {
                        bb = 0;
                    }
                    pixels[i + j * width] = Color.rgb(rr, gg, bb);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rBitmap;
    }

    /**
     * 彩度変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @param setting
     *         0%～50%～100%まで彩度を変更することができる。
     * @return 変換後のビットマップ
     */
    public static Bitmap setSaturation(final String filename, final int setting) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeの彩度処理を呼び出す
                // Saturation(pixels, width, height, setting); // Native C
                // method(Saturation)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        final int color = pixels[i + j * width];
                        int rr = Color.red(color);
                        int gg = Color.green(color);
                        int bb = Color.blue(color);
                        final int max = getRGBMax(rr, gg, bb);
                        rr = max - (max - rr) * (setting / 50);
                        gg = max - (max - gg) * (setting / 50);
                        bb = max - (max - bb) * (setting / 50);

                        if (rr > 255) {
                            rr = 255;
                        } else if (rr < 0) {
                            rr = 0;
                        }
                        if (gg > 255) {
                            gg = 255;
                        } else if (gg < 0) {
                            gg = 0;
                        }
                        if (bb > 255) {
                            bb = 255;
                        } else if (bb < 0) {
                            bb = 0;
                        }
                        pixels[i + j * width] = Color.rgb(rr, gg, bb);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * セピア変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @return 変換後のビットマップ
     */
    public static Bitmap setSepia(final Bitmap bitmap) {
        final int width = bitmap.getWidth(); // 画像横幅
        final int height = bitmap.getHeight(); // 画像縦幅
        final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
        final Bitmap rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
        rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
        // ----------------------------------------------------
        // ARMアーキテクチャの場合
        // ----------------------------------------------------
        final String arch = System.getProperty("os.arch");
        if (arch.contains("arm")) {
            // Nativeのセピア処理を呼び出す
            // Sepia(pixels, width, height); // Native C method(Sepia)
            // ----------------------------------------------------
            // ARMアーキテクチャ以外の場合
            // ----------------------------------------------------
        } else {
            // ピクセルデータ分ループ
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int color = pixels[i + j * width];
                    int rr = Color.red(color);
                    int gg = Color.green(color);
                    int bb = Color.blue(color);
                    final int yy = (299 * rr + 587 * gg + 114 * bb) / 1000; // 行列の計算１

                    /*
                     * uu = (-169 * rr - 331 * gg + 500 * bb) / 1000; //行列の計算２
                     * vv = (500 * rr - 419 * gg - 81 * bb) / 1000; //行列の計算３ uu
                     * = -23.296; vv = 14.336; rr = 1.0 * yy + 1.402 * vv;
                     * //逆行列の計算１ gg = 1.0 * yy - 0.334 * uu - 0.714 * vv;
                     * //逆行列の計算２ bb = 1.0 * yy + 1.772 * uu; //逆行列の計算３
                     */
                    rr = yy + 20; // 逆行列の計算１
                    gg = yy - 2; // 逆行列の計算２
                    bb = yy - 41; // 逆行列の計算３

                    if (rr > 255) {
                        rr = 255;
                    } else if (rr < 0) {
                        rr = 0;
                    }
                    if (gg > 255) {
                        gg = 255;
                    } else if (gg < 0) {
                        gg = 0;
                    }
                    if (bb > 255) {
                        bb = 255;
                    } else if (bb < 0) {
                        bb = 0;
                    }
                    pixels[i + j * width] = Color.rgb(rr, gg, bb);
                }
            }
        }
        rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return rBitmap;
    }

    /**
     * セピア変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @return 変換後のビットマップ
     */
    public static Bitmap setSepia(final String filename) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;

        try {
            rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
            rBitmap = rBitmap.copy(Bitmap.Config.ARGB_8888, true); // Mutableへの変更(ようするに編集可能にする)
            final int width = rBitmap.getWidth(); // ビットマップの横幅取得
            final int height = rBitmap.getHeight(); // ビットマップの縦幅取得
            final int[] pixels = new int[width * height]; // 設定するピクセルを領域取得
            rBitmap.getPixels(pixels, 0, width, 0, 0, width, height); // 設定するピクセルを領域取得
            // ----------------------------------------------------
            // ARMアーキテクチャの場合
            // ----------------------------------------------------
            final String arch = System.getProperty("os.arch");
            if (arch.contains("arm")) {
                // Nativeのセピア処理を呼び出す
                // Sepia(pixels, width, height); // Native C method(Sepia)
                // ----------------------------------------------------
                // ARMアーキテクチャ以外の場合
                // ----------------------------------------------------
            } else {
                // ピクセルデータ分ループ
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        final int color = pixels[i + j * width];
                        int rr = Color.red(color);
                        int gg = Color.green(color);
                        int bb = Color.blue(color);
                        final int yy = (299 * rr + 587 * gg + 114 * bb) / 1000; // 行列の計算１

                        /*
                         * uu = (-169 * rr - 331 * gg + 500 * bb) / 1000;
                         * //行列の計算２ vv = (500 * rr - 419 * gg - 81 * bb) / 1000;
                         * //行列の計算３ uu = -23.296; vv = 14.336; rr = 1.0 * yy +
                         * 1.402 * vv; //逆行列の計算１ gg = 1.0 * yy - 0.334 * uu -
                         * 0.714 * vv; //逆行列の計算２ bb = 1.0 * yy + 1.772 * uu;
                         * //逆行列の計算３
                         */
                        rr = yy + 20; // 逆行列の計算１
                        gg = yy - 2; // 逆行列の計算２
                        bb = yy - 41; // 逆行列の計算３

                        if (rr > 255) {
                            rr = 255;
                        } else if (rr < 0) {
                            rr = 0;
                        }
                        if (gg > 255) {
                            gg = 255;
                        } else if (gg < 0) {
                            gg = 0;
                        }
                        if (bb > 255) {
                            bb = 255;
                        } else if (bb < 0) {
                            bb = 0;
                        }
                        pixels[i + j * width] = Color.rgb(rr, gg, bb);
                    }
                }
            }
            rBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (final Exception e) {
            // 何もしない。
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * 回転変換処理
     *
     * @param bitmap
     *         ビットマップ
     * @param rotate
     *         0～360度まで指定可能
     * @return 変換後のビットマップ
     */
    public static Bitmap setRotate(final Bitmap bitmap, final int rotate) {
        Bitmap rBitmap = null;
        try {
            rBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true); // 画像をコピー(しないと元画像自体が変更される)
            final int width = rBitmap.getWidth(); // 画像横幅
            final int height = rBitmap.getHeight(); // 画像縦幅
            final Matrix matrix = new Matrix();
            // rotate the Bitmap
            matrix.postRotate(rotate);
            // recreate the new Bitmap
            rBitmap = Bitmap.createBitmap(rBitmap, 0, 0, width, height, matrix, true);
        } catch (final Exception e) {
            // 何もしない。
        }
        return rBitmap;
    }

    /**
     * 回転変換処理 本メソッドはBitmap実体を持つとheap memoryが少なくなるため、ファイルシステムに移行している。
     *
     * @param filename
     *         ファイル名
     * @param rotate
     *         0～360度まで指定可能
     * @return 変換後のビットマップ
     */
    public static Bitmap setRotate(final String filename, final int rotate) {
        // long start = System.currentTimeMillis();
        Bitmap rBitmap = null;
        // ----------------------------------------------------
        // ファイルチェック
        // ----------------------------------------------------
        final File ffilename = new File(filename);
        // ファイルが存在しているかチェック
        if (ffilename.exists()) {
            try {
                rBitmap = FileApplicationUtils.readBitmap(filename); // ファイルからBitmap取得
                final int width = rBitmap.getWidth(); // 画像横幅
                final int height = rBitmap.getHeight(); // 画像縦幅

                final Matrix matrix = new Matrix();
                // rotate the Bitmap
                matrix.postRotate(rotate);

                // recreate the new Bitmap
                rBitmap = Bitmap.createBitmap(rBitmap, 0, 0, width, height, matrix, true);
            } catch (final Exception e) {
                // 何もしない。
            }
        }
        // long end = System.currentTimeMillis();
        return rBitmap;
    }

    /**
     * コーナー処理
     *
     * @param bitmap
     *         ビットマップ
     * @param pixels
     *         コーナーのピクセルサイズ
     * @return bitmap
     */
    public static Bitmap getRoundedCornerBitmap(final Bitmap bitmap, final int pixels) {
        if (bitmap == null) {
            return null;
        }
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 3色の最大値を取得する
     *
     * @param red
     *         赤
     * @param green
     *         緑
     * @param blue
     *         青
     * @return 3色の最大数値
     */
    public static int getRGBMax(final int red, final int green, final int blue) {
        final int max = red < green ? green : red;
        return max < blue ? blue : max;
    }

    /**
     * 3色の最小値を取得する
     *
     * @param red
     *         赤
     * @param green
     *         緑
     * @param blue
     *         青
     * @return 3色の最小数値
     */
    public static int getRGBMin(final int red, final int green, final int blue) {
        final int min = red > green ? green : red;
        return min < blue ? blue : min;
    }

    /**
     * MIMEタイプを取得する(画像)
     *
     * @param filepath
     * @return MIMEタイプを取得する
     */
    public static String getImageMimeType(final String filepath) {
        Pattern pattern;
        Matcher matcher;
        // PNG
        pattern = Pattern.compile("\\.png$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(filepath);
        if (matcher.find()) {
            return "image/png";
        }
        // JPEG
        pattern = Pattern.compile("\\.jpe?g$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(filepath);
        if (matcher.find()) {
            return "image/jpg";
        }
        // GIF
        pattern = Pattern.compile("\\.gif$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(filepath);
        if (matcher.find()) {
            return "image/gif";
        }
        return "";
    }

    /**
     * Data URI scheme形式チェック
     *
     * @param data
     *         data:image/*;base64,～形式のデータ<br>
     *         ex:data:image/jpeg;base64,～
     * @return false:Data URI scheme形式以外<br>
     */
    public static boolean isDataUriScheme(final String data) {
        if (!StringUtils.isEmpty(data)) {
            // Pattern p = Pattern.compile("data:image/.+;base64,");
            // Matcher m = p.matcher(data);
            // if (m.find()) {
            // return true;
            // }
            if (data.startsWith("data:image")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Data URI schemeをBitmapに変換する。
     *
     * @param data
     *         data:image/*;base64,～形式のデータ<br>
     *         ex:data:image/jpeg;base64,～
     * @param w
     *         最大横幅
     * @param h
     *         最大縦幅
     * @return Bitmap
     */
    public static Bitmap getBitmap(final String data, final int w, final int h) {
        if (w <= 0 || h <= 0) {
            return getBitmap(data);
        }
        if (!isDataUriScheme(data)) {
            return null;
        }
        final String binary = data.replaceFirst("data:image/.+;base64,", "");
        final byte[] bytes = Base64Utils.base64DecodeByte(binary);
        return imageResize(bytes, w, h);
    }

    /**
     * Data URI schemeをBitmapに変換する。
     *
     * @param data
     *         data:image/*;base64,～形式のデータ<br>
     *         ex:data:image/jpeg;base64,～
     * @return Bitmap
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap getBitmap(final String data) {
        if (!isDataUriScheme(data)) {
            return null;
        }
        final String binary = data.replaceFirst("data:image/.+;base64,", "");
        final byte[] bytes = Base64Utils.base64DecodeByte(binary);
        final Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        // inMutableはAPI11以上からのため。
        if (AplUtils.hasHoneycomb()) {
            options.inMutable = true;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * ビットマップから指定の画像形式のバイトデータに変換して返却する。
     *
     * @param bitmap
     *         Bitmap
     * @return バイト型配列
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static byte[] getByte(final Bitmap bitmap, final IMAGE image) {
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            if (image == IMAGE.PNG) {
                bitmap.compress(CompressFormat.JPEG, IMAGE_QUALITY, out);
            } else if (image == IMAGE.WEBP) {
                if (AplUtils.hasIceCreamSandwich()) {
                    bitmap.compress(CompressFormat.WEBP, IMAGE_QUALITY, out);
                }
            } else if (image == IMAGE.PNG) {
                bitmap.compress(CompressFormat.PNG, IMAGE_QUALITY, out);
            }
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Mutableを変更したコピーされたBitmapを返却する。<br>
     *
     * @param bitmap
     *         Bitmap
     * @param isMutable
     *         true:mutableにする。<br>
     *         false:imutableにする。
     * @param isRecycle
     *         true:引数のBitmapをrecylceする。<br>
     *         false:引数のBitmapをrecycleしない。
     * @return コピーされたBitmap
     */
    public static Bitmap getBitmapCopy(Bitmap bitmap, final boolean isMutable, final boolean isRecycle) {
        if (bitmap == null) {
            return null;
        }
        Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        final Bitmap copyBitmap = bitmap.copy(config, isMutable);
        if (isRecycle) {
            if (!bitmap.equals(copyBitmap)) {
                // 元画像ファイルの破棄
                bitmap.recycle();
                bitmap = null;
            }
        }
        return copyBitmap;
    }
}
