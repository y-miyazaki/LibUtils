package com.miya38.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StatFs;

import com.google.common.io.Closeables;

/**
 * ファイル処理クラス<br>
 * このクラスは、アプリケーション領域の読み書きを対応する。
 */
public final class FileApplicationUtils {
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private FileApplicationUtils() {
    }

    /**
     * 初期化します。<br>
     * アプリケーションの開始時点で一度呼び出して下さい。
     *
     * @param context
     *            {@link Context}
     */
    public static void configure(Context context) {
        sContext = context;
    }

    /**
     * アプリケーション領域からファイル読み込み<br>
     * filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param filePath
     *            読み込みパス(相対パス+ファイル名)
     * @return ファイルデータ
     */
    public static String read(String filePath) {
        final File file = new File(getPath(filePath));
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = sContext.openFileInput(filePath);
                final byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                return new String(readBytes);
            } catch (Exception e) {
                // 握りつぶす
            } finally {
                Closeables.closeQuietly(fileInputStream);
            }
        }
        return null;
    }

    /**
     * アプリケーション領域からファイル読み込み<br>
     * filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param filePath
     *            読み込みパス(相対パス+ファイル名)
     * @return ファイルデータ
     */
    public static byte[] readByte(String filePath) {
        final File file = new File(getPath(filePath));
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = sContext.openFileInput(filePath);
                final byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                return readBytes;
            } catch (IOException e) {
                // 握りつぶす
            } finally {
                Closeables.closeQuietly(fileInputStream);
            }
        }
        return new byte[0];
    }

    /**
     * アプリケーション領域からBitmap読み込み<br>
     * filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param filePath
     *            ファイル名のみを指定(アプリケーションエリアに書き込むため、パスは指定しない(test.txt)
     * @return ビットマップオブジェクト (存在しなければnull)
     */
    public static Bitmap readBitmap(String filePath) {
        final File file = new File(getPath(filePath));
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = sContext.openFileInput(filePath);
                return BitmapFactory.decodeStream(fileInputStream);
            } catch (Exception e) {
                // 握りつぶす
            } finally {
                Closeables.closeQuietly(fileInputStream);
            }
        }
        return null;
    }

    /**
     * アプリケーション領域へのファイル書き込み<br>
     * filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param filePath
     *            保存パス(相対パス+ファイル名)
     * @param data
     *            書き込みデータ
     * @return true:書き込み成功<br>
     *         false:書き込み失敗
     */
    public static boolean write(String filePath, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            final File file = new File(getPath(filePath));
            file.mkdirs(); // ディレクトリを作る.
            file.delete(); // ファイルを削除する
            if (file.createNewFile()) {
                fileOutputStream = sContext.openFileOutput(filePath, Context.MODE_PRIVATE);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.flush();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(fileOutputStream);
        }
    }

    /**
     * アプリケーション領域への画像ファイル書き込み filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param bitmap
     *            画像
     * @param filePath
     *            ファイル名をパスで指定(例：test.txt)
     * @return true:書き込み成功<br>
     *         false:書き込み失敗
     */
    public static boolean write(String filePath, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            final File file = new File(filePath);
            file.mkdirs(); // ディレクトリを作る.
            file.delete(); // ファイルを削除する
            if (file.createNewFile()) {
                fileOutputStream = sContext.openFileOutput(filePath, Context.MODE_PRIVATE);
                if (file.getPath().endsWith(".png")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                } else if (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                }
                fileOutputStream.flush();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(fileOutputStream);
        }
    }

    /**
     * 指定したファイルを削除します。<br>
     * filePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param filePath
     * @return true:ファイル削除成功<br>
     *         false:ファイル削除失敗<br>
     */
    public static boolean delete(String filePath) {
        final File file = new File(filePath);
        return sContext.deleteFile(file.getName());
    }

    /**
     * ファイルコピー<br>
     * srcFilePath/dstFilePathは、アプリケーションディレクトリからの相対パスを指定してください。
     *
     * @param srcFilePath
     *            コピーファイル元[ファイル名のみを指定(アプリケーションエリアに書き込むため、パスは指定しない(test1.txt)]
     * @param dstFilePath
     *            ) * コピーファイル先[ファイル名のみを指定(アプリケーションエリアに書き込むため、パスは指定しない(test2.txt)]
     * @return true:コピー成功<br>
     *         false:コピー失敗<br>
     */
    public static boolean copy(String srcFilePath, String dstFilePath) {
        // ファイルコピーのフェーズ
        FileInputStream fileInputStream = null; // FileInputStreamインスタンス
        FileOutputStream fileOutputStream = null; // FileOutputStreamインスタンス
        final byte[] w = new byte[1024]; // バッファ
        int size = 0; // サイズ
        final File file1 = new File(srcFilePath); // Fileインスタンス(コピーファイル元)
        final File file2 = new File(dstFilePath); // Fileインスタンス(コピーファイル先)

        try {
            fileInputStream = sContext.openFileInput(file1.getName());
            fileOutputStream = sContext.openFileOutput(file2.getName(), Context.MODE_PRIVATE);
            while (true) {
                size = fileInputStream.read(w); // バッファサイズまで読み込み
                if (size <= 0) {
                    break;
                } // 読み込みが完了していたらループを抜ける
                fileOutputStream.write(w, 0, size);
            }
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(fileInputStream);
            Closeables.closeQuietly(fileOutputStream);
        }
    }

    /**
     * アプリケーションディレクトリ
     *
     * @return アプリケーションディレクトリ
     */
    public static String getPath() {
        return sContext.getFilesDir().getAbsolutePath();
    }

    /**
     * アプリケーションディレクトリ+ファイル名返却
     *
     * @param filename
     *            ファイル名
     * @return アプリケーションディレクトリ+ファイル名返却
     */
    public static String getPath(String filename) {
        return StringUtils.appendBuilder(sContext.getFilesDir().getAbsolutePath(), "/", filename);
    }

    /**
     * ファイルの存在有無を返却する
     *
     * @param filePath
     *            ファイルパス(フルパスで指定する。)
     * @return true:ファイルが存在する。<br>
     *         false:ファイルが存在しない。
     */
    public static boolean isExists(String filePath) {
        final File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 内部ストレージに使用可能な空き容量があるかをチェックする
     *
     * @param path
     *            ファイルパス
     * @param size
     *            指定容量
     * @return true:指定容量以上の空きサイズあり<br>
     *         false:指定容量以下の空きサイズしかない<br>
     */
    public static boolean isUsableSpace(File path, long size) {
        return getUsableSpace(path) >= size;
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param filePath
     *            ファイルパス
     * @return The space available in bytes
     */
    @SuppressWarnings("all")
    public static long getUsableSpace(File filePath) {
        try {
            return filePath.getFreeSpace();
        } catch (NoSuchMethodError e1) {
            final StatFs stats = new StatFs(filePath.getPath());
            try {
                return stats.getAvailableBytes();
            } catch (NoSuchMethodError e2) {
                return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            }
        }
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param filePath
     *            ファイルパス
     * @return The space available in bytes
     */
    @SuppressWarnings("all")
    public static long getTotalSpace(File filePath) {
        try {
            return filePath.getTotalSpace();
        } catch (NoSuchMethodError e1) {
            final StatFs stats = new StatFs(filePath.getPath());
            try {
                return stats.getTotalBytes();
            } catch (NoSuchMethodError e2) {
                return (long) stats.getBlockSize() * (long) stats.getBlockCount();
            }
        }
    }
}
