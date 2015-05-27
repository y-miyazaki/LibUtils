package com.miya38.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * コピーユーティリティークラス
 * 
 * @author t-yuba
 */
public final class CopyUtils implements Cloneable {
    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private CopyUtils() {
    }

    /**
     * このオブジェクトのCloneメソッドです.
     * 
     * @param o
     *            オブジェクト
     * @return このオブジェクトのColne(DeepCopy)
     * @throws CloneNotSupportedException
     *             Cloneを作成出来なかった
     */
    public static Object deepCopy(final Object o) throws CloneNotSupportedException {
        // オブジェクトを符号化し、バイト配列に書き込み
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream os = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);

            // 符号化されたオブジェクトのデータを保持する配列を取得
            final byte[] buff = baos.toByteArray();

            // バイト配列から、オブジェクトを複合化
            bais = new ByteArrayInputStream(buff);
            os = new ObjectInputStream(bais);
            return os.readObject();
        } catch (final Exception ex) {
            // Should not happen
            throw new CloneNotSupportedException();
        } finally {
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(os);
        }
    }
}
