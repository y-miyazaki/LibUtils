package com.miya38.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.Closeables;

/**
 * Stream に関するユーティリティメソッドを提供します。
 * 
 * @author y-miyazaki
 * 
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * ストリームを全て読み込み、バイト列として返します。
     * 
     * @param inputStream
     *            {@link InputStream}
     * @return ストリームバイト列
     * @throws IOException
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                IOUtils.write(inputStream, out);
                return out.toByteArray();
            } finally {
                Closeables.closeQuietly(out);
            }
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    /**
     * ストリームを全て読み込み、UTF-8文字列に返します。
     * 
     * @param inputStream
     *            {@link InputStream}
     * @return UTF-8文字列
     * @throws IOException
     */
    public static String toString(final InputStream inputStream) throws IOException {
        return new String(toByteArray(inputStream), StringUtils.UTF_8);
    }
}
