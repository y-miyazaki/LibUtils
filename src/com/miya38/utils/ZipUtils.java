package com.miya38.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Zipユーティリティ
 * 
 * @author y-miyazaki
 * 
 */
public final class ZipUtils {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** バッファサイズ */
    private static final int BUFFER_SIZE = 1024;
    // ----------------------------------------------------------
    // other
    // ----------------------------------------------------------
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ZipUtils() {
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
     * Zip解凍後のファイルリスト
     * <p>
     * 実際に解凍はせず解凍後の想定パスでしかないことを認識すること。
     * </p>
     * 
     * @param zipPath
     *            zipのパス
     * @return zip解凍後のファイルリスト
     */
    public static List<String> getFileList(final String zipPath) {
        final List<String> fileLists = new ArrayList<String>();
        InputStream is = null;
        ZipInputStream zis = null;

        try {
            is = sContext.getResources().getAssets().open(zipPath, AssetManager.ACCESS_STREAMING);
            zis = new ZipInputStream(is);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                fileLists.add(sContext.getFilesDir().toString() + "/" + ze.getName());
            }
        } catch (final Exception e) {
            // 握りつぶす
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(zis);
        }
        return fileLists;
    }

    /**
     * Zip解凍後のファイルリスト(SDカード内)
     * <p>
     * 実際に解凍はせず解凍後の想定パスでしかないことを認識すること。
     * </p>
     * 
     * @param zipPath
     *            zipのパス
     * @return zip解凍後のファイルリスト
     */
    public static List<String> getFileListSdCard(final String zipPath) {
        final List<String> fileLists = new ArrayList<String>();
        InputStream is = null;
        ZipInputStream zis = null;

        try {
            is = sContext.getResources().getAssets().open(zipPath, AssetManager.ACCESS_STREAMING);
            zis = new ZipInputStream(is);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                fileLists.add(FileSdCardUtils.getCachePath(ze.getName()));
            }
        } catch (final Exception e) {
            // 握りつぶす
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(zis);
        }
        return fileLists;
    }

    /**
     * Assets内のzipファイルをアプリケーションディレクトリにコピーする
     * 
     * @param zipPath
     *            zipのパス
     * @return ファイルパス
     */
    public static List<String> zipToFileCopy(final String zipPath) {
        final List<String> fileLists = new ArrayList<String>();
        InputStream is = null;
        FileOutputStream fos = null;
        ZipInputStream zis = null;

        try {
            is = sContext.getResources().getAssets().open(zipPath, AssetManager.ACCESS_STREAMING);
            zis = new ZipInputStream(is);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                // パスを取得する
                final String path = sContext.getFilesDir().toString() + "/" + ze.getName();

                // 念のため、ファイルが存在している場合は削除する。
                final File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                final byte[] buf = new byte[BUFFER_SIZE];
                int size = 0;

                fos = new FileOutputStream(path, false);
                while ((size = zis.read(buf, 0, buf.length)) > -1) {
                    fos.write(buf, 0, size);
                }
                zis.closeEntry();
                fileLists.add(path);
            }
        } catch (final Exception e) {
            return new ArrayList<String>();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(zis);
        }
        return fileLists;
    }

    /**
     * Assets内のzipファイルをSDカードディレクトリにコピーする
     * 
     * @param zipPath
     *            zipのパス
     * @return SDカードのパス
     */
    public static List<String> zipToFileSdCardCopy(final String zipPath) {
        final List<String> fileLists = new ArrayList<String>();
        InputStream is = null;
        FileOutputStream fos = null;
        ZipInputStream zis = null;

        try {
            is = sContext.getResources().getAssets().open(zipPath, AssetManager.ACCESS_STREAMING);
            zis = new ZipInputStream(is);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                final String path = FileSdCardUtils.getCachePath(ze.getName());
                // 念のため、ファイルが存在している場合は削除する。
                final File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }

                // バッファ確保
                final byte[] buf = new byte[BUFFER_SIZE];
                int size = 0;

                fos = new FileOutputStream(path, false);
                while ((size = zis.read(buf, 0, buf.length)) > -1) {
                    fos.write(buf, 0, size);
                }
                zis.closeEntry();
                fileLists.add(path);
            }
        } catch (final Exception e) {
            return new ArrayList<String>();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(zis);
        }
        return fileLists;
    }
}
