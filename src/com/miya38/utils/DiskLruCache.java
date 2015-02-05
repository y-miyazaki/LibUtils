/*
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package com.miya38.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * キャッシュファイルクラス Least Recently Used (LRU): 最近最も使われていないデータを最初に捨てる
 * 
 * A simple disk LRU bitmap cache to illustrate how a disk cache would be used
 * for bitmap caching. A much more robust
 * and efficient disk LRU cache solution can be found in the ICS source code
 * (libcore/luni/src/main/java/libcore/io/DiskLruCache.java) and is preferable
 * to this simple implementation.
 * 
 * ①初期処理：initDiskLruCache<br>
 * →synchronizedMapに既存のキャッシュファイル情報を設定<br>
 * →既存キャッシュファイルの合計ファイルサイズを設定<br>
 * ※synchronizedMapを共有する必要があるため、SKINダウンロードサービスなどを別プロセスに出来ない<br>
 * ②オープン：openCache<br>
 * →空き容量+既存キャッシュファイル合計がMaxサイズ以下であるかチェック<br>
 * →アプリ内で同じインスタンスを参照するようSingletonパターンとする<br>
 * ③書き込み：put<br>
 * →合計ファイルサイズに書き込むファイルサイズを加算<br>
 * →キャッシュファイルの書き込み<br>
 * ④書き込み後処理：flush<br>
 * →キャッシュサイズがMaxサイズ以上の場合、古いファイルから順番に消していく<br>
 * →一度に削除するファイルの数は、MAX_REMOVALS(4)ファイルまでとなっている<br>
 * ※MAX_REMOVALSを上げると処理速度に影響をする<br>
 */
@SuppressWarnings("javadoc")
public abstract class DiskLruCache {
    // ----------------------------------------------------------
    // define
    // ----------------------------------------------------------
    /** IOバッファサイズ */
    public static final int IO_BUFFER_SIZE = 1024; // 1KB
    /** PNG形式保存 */
    public static CompressFormat mCompressFormat = CompressFormat.PNG;
    /** 品質 */
    public static int mCompressQuality = 100;
    /** 最初のキャパシティ */
    private static final int INITIAL_CAPACITY = 32;
    /** ファクター */
    private static final float LOAD_FACTOR = 0.75f;
    /** キャッシュファイル名プレフィックス */
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    /** 一度に削除するファイルのMax数(ファイル数制限、容量制限がオーバーしている場合は無効) */
    private static final int MAX_REMOVALS = 4;
    /** 保有可能なキャッシュファイル数 */
    private static final int MAX_CACHE_ITEM_SIZE = 1024;

    /** キャッシュディレクトリ */
    private final File mCacheDir;
    /** 保有可能なキャッシュファイルの最大容量数 */
    private long mMaxCacheByteSize = 1024 * 1024 * 5;
    /** キャッシュファイル管理テーブル */
    private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY, LOAD_FACTOR, true));
    /** キャッシュサイズ */
    private int mCcacheSize;
    /** キャッシュファイル合計サイズ */
    private static long mCcacheByteSize;
    /** DiskLruCachePostEclairクラス */
    private static DiskLruCachePostEclair sDiskLruCachePostEclair;
    /** DiskLruCachePreEclairクラス(1.6以前のため未使用) */
    private static DiskLruCachePreEclair sDiskLruCachePreEclair;

    /**
     * A filename filter to use to identify the cache filenames which have
     * CACHE_FILENAME_PREFIX prepended.
     */
    private static final FilenameFilter mCacheFileFilter = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String filename) {
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * DiskLruCacheクラス コンストラクタ<br>
     * Constructor that should not be called directly,<br>
     * instead use {@link DiskLruCache#openCache(Context, File, long)} which
     * runs some extra checks before creating a<br>
     * DiskLruCache instance.
     * 
     * @param cacheDir
     *            キャッシュディレクトリ
     * @param maxByteSize
     */
    private DiskLruCache(final File cacheDir, final long maxByteSize) {
        mCacheDir = cacheDir;
        mMaxCacheByteSize = maxByteSize;
        // キャッシュファイル管理テーブル 初期化
        initLinkedHashMap(cacheDir);
        // 全ファイルサイズ取得
        mCcacheByteSize = getAllFileSize(cacheDir);
    }

    /**
     * オープンキャッシュ Used to fetch an instance of DiskLruCache.
     * 
     * @param context
     *            Context
     * @param cacheDir
     *            キャッシュディレクトリ
     * @param maxByteSize
     * @return instance
     */
    public static synchronized DiskLruCache openCache(final Context context, final File cacheDir, final long maxByteSize) {
        try {
            // キャッシュディレクトリがない場合は作成
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            // Singletonパターン(プログラム中にSingletonクラスのインスタンスが1つしか存在しないことを保証)
            // キャッシュディレクトリ状態チェック
            if (cacheDir.isDirectory() && cacheDir.canWrite()) {
                if (AplUtils.hasEclair()) {
                    // Android 2.0以上
                    // diskLruCachePostEclairが生成されていない場合は、生成する
                    if (sDiskLruCachePostEclair == null) {
                        sDiskLruCachePostEclair = new DiskLruCachePostEclair(cacheDir, maxByteSize);
                    }
                    // diskLruCachePostEclairが生成済の場合は、そのままリターン
                    return sDiskLruCachePostEclair;
                } else {
                    // Android 1.6以下
                    // diskLruCachePretEclairが生成されていない場合は、生成する
                    if (sDiskLruCachePreEclair == null) {
                        sDiskLruCachePreEclair = new DiskLruCachePreEclair(cacheDir, maxByteSize);
                    }
                    // diskLruCachePretEclairが生成済の場合は、そのままリターン
                    return sDiskLruCachePreEclair;
                }
            }
        } catch (final RuntimeException e) {
            // 何もしない。
        }
        return null;
    }

    /**
     * Used to fetch an instance of DiskLruCache.
     * 
     * @param context
     *            Context
     * @param maxByteSize
     *            最大バイトサイズ
     * @return instance
     */
    public static DiskLruCache openCache(final Context context, final long maxByteSize) {
        return openCache(context, DiskLruCache.getDiskCacheDir(context, ""), maxByteSize);
    }

    /**
     * キャッシュから画像ファイル取得 Get an image from the disk cache.
     * 
     * @param key
     *            The unique identifier for the bitmap
     * @return The bitmap or null if not found
     */
    public Bitmap get(final String key) {
        synchronized (mLinkedHashMap) {
            final String renameKey = getKey(key);
            final String file = mLinkedHashMap.get(renameKey);
            if (file != null) {
                return BitmapFactory.decodeFile(file);
            } else {
                // キャッシュMapにないが、ファイルが存在する場合は、キャッシュMapに書き込む
                final String existingFile = createFilePath(mCacheDir, renameKey);
                if (new File(existingFile).exists()) {
                    put(renameKey, existingFile);
                    return BitmapFactory.decodeFile(existingFile);
                }
            }
            return null;
        }
    }

    /**
     * キャッシュからファイルのパスを返す Get an File from the disk cache.
     * 
     * @param key
     *            The unique identifier for the bitmap
     * @return The bitmap or null if not found
     */
    public String getFile(final String key) {
        synchronized (mLinkedHashMap) {
            final String renameKey = getKey(key);
            final String file = mLinkedHashMap.get(renameKey);
            if (file != null) {
                // ファイルが参照できないパターンを考慮しexsitsチェックを行う
                final File checkFile = new File(file);
                if (checkFile.exists()) {
                    return file;
                } else {
                    return null;
                }
            } else {
                // キャッシュMapにないが、ファイルが存在する場合は、キャッシュMapに書き込む
                final String existingFile = createFilePath(mCacheDir, renameKey);
                if (new File(existingFile).exists()) {
                    put(renameKey, existingFile);
                    return existingFile;
                }
            }
            return null;
        }
    }

    /**
     * 画像データキャッシュ書き込み Add a bitmap to the disk cache.
     * 
     * @param key
     *            A unique identifier for the bitmap.
     * @param data
     *            The bitmap to store.
     */
    public void put(final String key, final Bitmap data) {
        put(key, data, false);
    }

    /**
     * 画像データキャッシュ書き込み Add a bitmap to the disk cache.
     * 
     * @param key
     *            A unique identifier for the bitmap.
     * @param data
     *            The bitmap to store.
     * @param isAsync
     *            true:async false:not async
     */
    public void put(final String key, final Bitmap data, final boolean isAsync) {
        synchronized (mLinkedHashMap) {
            final String renameKey = getKey(key);
            if (mLinkedHashMap.get(renameKey) == null) {
                try {
                    final String file = createFilePath(mCacheDir, renameKey);
                    if (writeBitmapToFile(data, file)) {
                        put(renameKey, file);
                        flushCache();
                    }
                } catch (final FileNotFoundException e) {
                    LogUtils.e("DiskLruCache", "put Error", e);
                } catch (final IOException e) {
                    LogUtils.e("DiskLruCache", "put Error", e);
                }
            }
        }
    }

    /**
     * 文字データキャッシュ書き込み Add a bitmap to the disk cache.
     * 
     * @param key
     *            A unique identifier for the bitmap.
     * @param data
     *            The bitmap to store.
     */
    public void putData(final String key, final String data) {
        synchronized (mLinkedHashMap) {
            final String renameKey = getKey(key);
            if (mLinkedHashMap.get(renameKey) == null) {
                try {
                    final String file = createFilePath(mCacheDir, renameKey);
                    if (writeStringToFile(data, file)) {
                        put(renameKey, file);
                        flushCache();
                    }
                } catch (final FileNotFoundException e) {
                } catch (final IOException e) {
                }
            }
        }
    }

    /**
     * Checks if a specific key exist in the cache.
     * 
     * @param key
     *            The unique identifier for the bitmap
     * @return true if found, false otherwise
     */
    public boolean containsKey(final String key) {
        final String renameKey = getKey(key);
        // See if the key is in our HashMap
        if (mLinkedHashMap.containsKey(renameKey)) {
            return true;
        }

        // Now check if there's an actual file that exists based on the key
        final String existingFile = createFilePath(mCacheDir, renameKey);
        if (new File(existingFile).exists()) {
            // File found, add it to the HashMap for future use
            put(renameKey, existingFile);
            return true;
        }
        return false;
    }

    /**
     * Removes all disk cache entries from this instance cache dir
     */
    public void clearCache() {
        DiskLruCache.clearCache(mCacheDir);
    }

    /**
     * Sets the target compression format and quality for images written to the
     * disk cache.
     * 
     * @param compressFormat
     * @param quality
     */
    public static void setCompressParams(final CompressFormat compressFormat, final int quality) {
        mCompressFormat = compressFormat;
        mCompressQuality = quality;
    }

    /**
     * Removes all disk cache entries from the application cache directory in
     * the uniqueName sub-directory.
     * 
     * @param context
     *            The context to use
     * @param uniqueName
     *            A unique cache directory name to append to the app cache
     *            directory
     */
    public static void clearCache(final Context context, final String uniqueName) {
        final File cacheDir = getDiskCacheDir(context, uniqueName);
        clearCache(cacheDir);
    }

    /**
     * Removes all disk cache entries from the given directory. This should not
     * be called directly, call {@link DiskLruCache#clearCache(Context, String)}
     * or {@link DiskLruCache#clearCache()} instead.
     * 
     * @param cacheDir
     *            The directory to remove the cache files from
     */
    private static void clearCache(final File cacheDir) {
        final File[] files = cacheDir.listFiles(mCacheFileFilter);
        if (files != null) {
            for (final File file : files) {
                file.delete();
            }
        }
    }

    /**
     * キャッシュディレクトリパス取得<br>
     * Get a usable cache directory (external if available, internal otherwise).
     * 
     * @param context
     *            The context to use
     * @param uniqueName
     *            A unique directory name to append to the cache dir
     * @return The cache dir
     * 
     */
    public static File getDiskCacheDir(final Context context, final String uniqueName) {
        // SdCardがマウントされている場合:sdcard(/mnt/sdcard/Android/data/パッケージ名/cache/ファイル名)
        // SdCardがマウントされていない場合:内部ストレージ(/data/data/パッケージ名/cache/ファイル名)
        final String cachePath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Check if external storage is built-in or removable.
     * 
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    protected static boolean isExternalStorageRemovable() {
        if (AplUtils.hasEclair()) {
            return DiskLruCachePostEclair.isExternalStorageRemovable();
        } else {
            return DiskLruCachePreEclair.isExternalStorageRemovable();
        }
    }

    /**
     * Get the external app cache directory.
     * 
     * @param context
     *            The context to use
     * @return The external cache dir
     */
    protected static File getExternalCacheDir(final Context context) {
        if (AplUtils.hasEclair()) {
            return DiskLruCachePostEclair.getExternalCacheDir(context);
        } else {
            return DiskLruCachePreEclair.getExternalCacheDir(context);
        }
    }

    /**
     * Check how much usable space is available at a given path.
     * 
     * @param path
     *            The path to check
     * @return The space available in bytes
     */
    protected static long getUsableSpace(final File path) {
        if (AplUtils.hasEclair()) {
            return DiskLruCachePostEclair.getUsableSpace(path);
        } else {
            return DiskLruCachePreEclair.getUsableSpace(path);
        }
    }

    /**
     * 
     * @param cacheDir
     *            キャッシュディレクトリ
     * @param key
     * @return file path
     */
    public static String createFilePath(final File cacheDir, final String key) {
        return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX + key;
    }

    /**
     * 
     * Creates a constant cache file path given a target cache directory and an
     * image key.
     * 
     * @param cacheDir
     *            キャッシュディレクトリ
     * @return ファイルパス
     */
    public static String createMapKey(final String cacheName) {
        // プリフィックスの除去
        return cacheName.substring(CACHE_FILENAME_PREFIX.length());
    }

    /**
     * Removes a mapping with the specified key from this Map.
     * 
     * @param key
     */
    public synchronized void remove(final String key) {
        final String renameKey = getKey(key);
        // キャッシュファイル管理テーブル更新
        mLinkedHashMap.remove(renameKey);
        // ファイルサイズ更新
        mCcacheByteSize -= new File(renameKey).length();
    }

    /**
     * キャッシュファイル管理テーブルサイズ 取得
     * 
     * @return
     */
    public int getMapSize() {
        return mLinkedHashMap.size();
    }

    // ----------------------------------------------------------
    // 内部メソッド
    // ----------------------------------------------------------
    /**
     * 画像データファイル書き込み<br>
     * Writes a bitmap to a file. Call
     * {@link DiskLruCache#setCompressParams(CompressFormat, int)} first to set
     * the
     * target bitmap compression and format.
     * 
     * @param bitmap
     * @param file
     * @return
     */
    private boolean writeBitmapToFile(final Bitmap bitmap, final String file) throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 文字データファイル書き込み<br>
     * Writes a data to a file. Call
     * {@link DiskLruCache#setCompressParams(CompressFormat, int)} first to set
     * the target
     * bitmap compression and format.
     * 
     * @param data
     * @param file
     * @return
     */
    private boolean writeStringToFile(final String data, final String file) throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);
            out.write(data.getBytes());// ファイルへ書き込み
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;
    }

    /**
     * キャッシュディレクトリの容量調整<br>
     * Flush the cache, removing oldest entries if the total size is over the
     * specified cache size. Note that this isn't
     * keeping track of stale files in the cache directory that aren't in the
     * HashMap. If the images and keys in the
     * disk cache change often then they probably won't ever be removed.
     * 
     */
    private void flushCache() {
        Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;

        // 削除したファイル数が、一度に削除するファイル数「MAX_REMOVALS」を超えるまえ
        // キャッシュ保有ファイル数が制限値より小さくなるまで
        // キャッシュ全ファイルサイズが制限値より小さくなるまで
        while (count < MAX_REMOVALS && (mCcacheSize > MAX_CACHE_ITEM_SIZE || mCcacheByteSize > mMaxCacheByteSize)) {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            eldestFile = new File(eldestEntry.getValue());
            eldestFileSize = eldestFile.length();
            mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            mCcacheSize = mLinkedHashMap.size();
            mCcacheByteSize -= eldestFileSize;
            count++;
        }
    }

    /**
     * mLinkedHashMap設定
     * 
     * @param key
     * @param file
     */
    private void put(final String key, final String file) {
        final String renameKey = getKey(key);
        // キャッシュファイル管理テーブル 更新
        mLinkedHashMap.put(renameKey, file);
        // キャッシュファイル数 更新
        mCcacheSize = mLinkedHashMap.size();
        // キャッシュサイズ 更新
        mCcacheByteSize += new File(file).length();
    }

    /**
     * LinkedHashMap初期化
     * 
     * @param files
     */
    private void initLinkedHashMap(final File files) {
        final File[] listFiles = files.listFiles();
        // 全キャッシュファイルをキャッシュファイル管理テーブルに設定(更新日時の昇順)
        Arrays.sort(listFiles, new FileSort());
        for (final File file : listFiles) {
            mLinkedHashMap.put(createMapKey(file.getName()), file.getAbsolutePath());
        }
    }

    /**
     * キー取得メソッド
     * <p>
     * ファイルベースの場合は使用できない文字があるのでURLエンコードする。
     * </p>
     * 
     * @param key
     *            変換前キー
     * @return 変換後キー
     */
    private static String getKey(final String key) {
        return Uri.encode(key);
    }

    /**
     * 全ファイルサイズ取得
     * 
     * @param files
     * @return 指定ディレクトリの全ファイルサイズの合算値
     */
    private static long getAllFileSize(final File files) {
        long allFileSize = 0;
        for (final File file : files.listFiles()) {
            allFileSize += file.length();
        }
        return allFileSize;
    }

    // ----------------------------------------------------------
    // インナークラス
    // ----------------------------------------------------------
    /**
     * ファイル更新日時ソートクラス
     */
    private static class FileSort implements Comparator<File> {
        @Override
        public int compare(final File f1, final File f2) {
            return (int) (f1.lastModified() - f2.lastModified());
        }
    };

    /**
     * DiskLruCachePostEclairクラス<br>
     * DiskLruCacheのopenCacheからのみ生成
     */
    private static class DiskLruCachePostEclair extends DiskLruCache {
        public DiskLruCachePostEclair(final File cacheDir, final long maxByteSize) {
            super(cacheDir, maxByteSize);
        }

        /**
         * Check how much usable space is available at a given path.
         * 
         * @param path
         *            The path to check
         * @return The space available in bytes
         */
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @SuppressWarnings("all")
        protected static long getUsableSpace(final File path) {
            if (AplUtils.hasGingerbread()) {
                return path.getUsableSpace();
            }
            final StatFs stats = new StatFs(path.getPath());
            return (long) stats.getAvailableBlocks() * (long) stats.getBlockSize();
        }

        /**
         * Check if external storage is built-in or removable.
         * 
         * @return True if external storage is removable (like an SD card),
         *         false otherwise.
         */
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @SuppressWarnings("all")
        protected static boolean isExternalStorageRemovable() {
            if (AplUtils.hasGingerbread()) {
                return Environment.isExternalStorageRemovable();
            }
            return true;
        }

        /**
         * Get the external app cache directory.
         * 
         * @param context
         *            The context to use
         * @return The external cache dir
         */
        @TargetApi(Build.VERSION_CODES.FROYO)
        protected static File getExternalCacheDir(final Context context) {
            if (AplUtils.hasFroyo()) {
                final File cacheDir = context.getExternalCacheDir();
                if (cacheDir != null) {
                    return cacheDir;
                }
            }
            // Froyo 以前は自前でディレクトリを作成する
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
            return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
    }

    /**
     * DiskLruCachePreEclairクラス<br>
     * DiskLruCacheのopenCacheからのみ生成 Android 1.6 以前
     */
    private static class DiskLruCachePreEclair extends DiskLruCache {
        public DiskLruCachePreEclair(final File cacheDir, final long maxByteSize) {
            super(cacheDir, maxByteSize);
        }

        /**
         * Check how much usable space is available at a given path.
         * 
         * @param path
         *            The path to check
         * @return The space available in bytes
         */
        protected static long getUsableSpace(final File path) {
            final StatFs stats = new StatFs(path.getPath());
            return (long) stats.getAvailableBlocks() * (long) stats.getBlockSize();
        }

        /**
         * Check if external storage is built-in or removable.
         * 
         * @return True if external storage is removable (like an SD card),
         *         false otherwise.
         */
        protected static boolean isExternalStorageRemovable() {
            return true;
        }

        /**
         * Get the external app cache directory.
         * 
         * @param context
         *            The context to use
         * @return The external cache dir
         */
        protected static File getExternalCacheDir(final Context context) {
            // Froyo 以前は自前でディレクトリを作成する
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
            return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
    }
}
