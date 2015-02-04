package com.miya38.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.database.Cursor;

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

    /** IO_BUFFER_SIZE */
    protected static final int IO_BUFFER_SIZE = 4 * 1024;

    // ////////////////////////////////////////////////
    // public
    // ////////////////////////////////////////////////

    /**
     * <code>{@link Cursor}</code>をクローズします。 クローズ時に例外が発生しても無視します。
     *
     * @param c
     *            {@link Cursor}
     */
    public static void closeQuietly(final Cursor c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (final Exception e) {
            // エラーは握りつぶす
        }
    }

    /**
     * <code>{@link Closeable}</code>をクローズします。 クローズ時に例外が発生しても無視します。
     *
     * @param closeable
     *            {@link Closeable}
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // エラーは握りつぶす
        }
    }

    /**
     * ストリームの内容をコピーします。
     *
     * @param in
     *            {@link InputStream}
     * @param out
     *            {@link OutputStream}
     * @throws EzaException
     *             {@link EzaException}
     */
    public static void copy(final InputStream in, final OutputStream out) {
        BufferedOutputStream bos = null;
        if (out instanceof BufferedOutputStream) {
            bos = (BufferedOutputStream) out;
        } else {
            bos = new BufferedOutputStream(out, IO_BUFFER_SIZE);
        }
        final byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        try {
            while ((read = in.read(b)) != -1) {
                bos.write(b, 0, read);
            }
            bos.flush();
        } catch (final IOException e) {
            // 握りつぶす
        }
    }

    /**
     * ストリームの内容をバイト列として取得します。
     *
     * @param in
     *            {@link InputStream}
     * @return byte[]
     */
    public static byte[] getBytes(final InputStream in) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream(IO_BUFFER_SIZE);
        try {
            copy(in, out);
            out.flush();
            return out.toByteArray();
        } catch (final IOException e) {
            // 握りつぶす
        } finally {
            closeQuietly(out);
        }
        return null;
    }

    /**
     * ストリームの内容を文字列として取得します。
     *
     * @param in
     *            {@link InputStream}
     * @param charset
     *            エンコード
     * @return エンコード後の文字列
     */
    public static String getString(final InputStream in, final String charset) {
        try {
            return new String(getBytes(in), charset);
        } catch (final IOException e) {
            // 握りつぶす
        }
        return null;
    }
}
