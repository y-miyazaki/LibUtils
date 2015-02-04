package com.miya38.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.Closeables;

/**
 * 入出力に関するユーティリティクラス。
 *
 * @author y-miyazaki
 */
public final class IOUtils {
    /** TAG */
    private final static String TAG = IOUtils.class.getSimpleName();

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private IOUtils() {
    }

    /**
     * 指定した出力ストリームに、入力ストリームが尽きるまで書き込みます。
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        int b = -1;
        while (0 <= (b = inputStream.read())) {
            outputStream.write(b);
        }
    }

    /**
     * 指定した出力ストリームに、入力ストリームから bufferSize 分読み込んで書き込みます。<br>
     *
     * 入力ストリームが尽きたら false を返します。
     *
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @return result
     * @throws IOException
     */
    public static int write(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        final int result = inputStream.read(buffer);

        if (0 <= result) {
            outputStream.write(buffer, 0, result);
        }

        return result;
    }

    /**
     * 指定したパスにファイルをストリームを書き込みます。
     *
     * @param inputStream
     * @param filePath
     * @throws IOException
     */
    public static void writeFile(InputStream inputStream, String filePath) throws IOException {
        writeFile(inputStream, new File(filePath));
    }

    /**
     * 指定したファイルにストリームを書き込みます。
     *
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void writeFile(InputStream inputStream, File file) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            write(inputStream, out);
        } finally {
            Closeables.closeQuietly(out);
        }
    }

    /**
     * 指定したファイルを削除します。
     *
     * @param filePath
     * @throws IOException
     */
    public static void deleteFile(String filePath) throws IOException {
        final File file = new File(filePath);
        if (file.exists() && file.isFile() && file.canWrite()) {
            file.delete();
        }
    }

    /**
     * 安全にディレクトリを作成するユーティリティメソッド。
     *
     * @param dir
     * @return 指定したディレクトリを作成した結果を返す
     */
    public static File mkdirs(File dir) {
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                LogUtils.w(TAG, "directory create failed.", e);
            }
        }
        return dir;
    }
}
