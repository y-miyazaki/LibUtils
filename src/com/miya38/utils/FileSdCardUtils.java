package com.miya38.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;

import com.google.common.io.Closeables;

/**
 * SDカード処理クラス
 * 
 * @author y-miyazaki
 */
public final class FileSdCardUtils {
    /** TAG */
    private static final String TAG = FileSdCardUtils.class.getSimpleName();
    /** Context */
    private static Context sContext;
    /** ギャラリーURI */
    private static final Uri URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    /** SDカードマウント不可エラーメッセージ */
    private static final String IOEXCEPTION_SDCARD_MESSAGE = "SD card can't read.";

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private FileSdCardUtils() {
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
     * ファイル詳細情報クラス
     * 
     * @author y-miyazaki
     */
    public static class FileResource {
        /** タイトル名(MediaStore.Images.Media.TITLE) */
        private String title;
        /** 日付(MediaStore.Images.Media.DATE_MODIFIED) */
        private long dateModified;
        /** サイズMediaStore.Images.Media.SIZE) */
        private long size;
        /** mime type(MediaStore.Images.Media.TITLE) */
        private String mimeType;

        /**
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title
         *            titleをセットする
         */
        public void setTitle(final String title) {
            this.title = title;
        }

        /**
         * @return dateModified
         */
        public long getDateModified() {
            return dateModified;
        }

        /**
         * @param dateModified
         *            dateModifiedをセットする
         */
        public void setDateModified(final long dateModified) {
            this.dateModified = dateModified;
        }

        /**
         * @return size
         */
        public long getSize() {
            return size;
        }

        /**
         * @param size
         *            sizeをセットする
         */
        public void setSize(final long size) {
            this.size = size;
        }

        /**
         * @return mimeType
         */
        public String getMimeType() {
            return mimeType;
        }

        /**
         * @param mimeType
         *            mimeTypeをセットする
         */
        public void setMimeType(final String mimeType) {
            this.mimeType = mimeType;
        }

    }

    /**
     * SDカードからBitmap読み込み<br>
     * filePathは、SDカードからの相対パスを指定してください。
     * 
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * @return ビットマップオブジェクト (存在しなければnull)
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static Bitmap readBitmap(final String filePath) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        FileInputStream fileInput = null;
        BufferedInputStream bufInput = null;
        try {
            fileInput = new FileInputStream(getPath(filePath));
            bufInput = new BufferedInputStream(fileInput);
            return BitmapFactory.decodeStream(bufInput);
        } catch (final FileNotFoundException e) {
            // 握りつぶす
        } finally {
            Closeables.closeQuietly(fileInput);
            Closeables.closeQuietly(bufInput);
        }
        return null;
    }

    /**
     * SDカードからBitmap読み込み<br>
     * ギャラリーからID指定でBitmapを取得する
     * 
     * @param cr
     *            ContentResolver
     * @param id
     *            ID
     * @return ビットマップオブジェクト (存在しなければnull)
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static Bitmap readBitmap(final ContentResolver cr, final int id) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        final Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // ストレージエリア
                new String[] { MediaColumns.DATA }, // ファイル
                BaseColumns._ID + " = ?", // selection(ID)
                new String[] { String.valueOf(id) }, // selectionArgs(ID)
                BaseColumns._ID + " asc"); // sortOrder
        try {
            if (cursor != null) { // SDカードがない場合等nullの可能性あり
                if (cursor.getCount() != 0) { // 削除データがある場合
                    cursor.moveToFirst();
                    final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                    if (file.exists()) {
                        final FileInputStream fileInput = new FileInputStream(file);
                        final BufferedInputStream bufInput = new BufferedInputStream(fileInput);
                        return BitmapFactory.decodeStream(bufInput);
                    }
                }
            }
        } catch (final IOException e) {
            // 握りつぶす
        } finally {
            if (cursor != null) {
                Closeables.closeQuietly(cursor);
            }
        }
        return null;
    }

    /**
     * ギャラリーからID指定で画像ファイル名を取得する
     * 
     * @param cr
     *            ContentResolver
     * @param id
     *            ID
     * @return ビットマップオブジェクト (存在しなければnull)
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static String readFilename(final ContentResolver cr, final int id) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        String result = null; // ファイル名
        final Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // ストレージエリア
                new String[] { MediaColumns.DATA }, // ファイル
                BaseColumns._ID + " = ?", // selection(ID)
                new String[] { String.valueOf(id) }, // selectionArgs(ID)
                BaseColumns._ID + " asc"); // sortOrder
        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                if (file.exists()) {
                    result = file.getPath();
                }
            }
            Closeables.closeQuietly(cursor);
        }
        return result;
    }

    /**
     * SDカードに存在する画像ファイルを取得する。
     * 
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * @return 画像ファイルリスト (存在しない場合は空のリスト)
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static List<File> readBitmapFileList(final String filePath) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        final List<File> result = new ArrayList<File>();
        final File loadDir = new File(getPath(filePath));

        if (!loadDir.exists()) {
            return result;
        }
        if (!loadDir.canRead()) {
            return result;
        }

        // アプリケーションディレクトリの画像ファイルを取得
        final File[] files = loadDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File loadDir, final String fileName) {
                // CompressFormat
                // JPEG = new CompressFormat("JPEG", 0);
                // PNG = new CompressFormat("PNG", 1);
                return fileName.matches(".+\\.(PNG|png|JPEG|JPG|jpeg|jpg|GIF|gif)$");
            }
        });
        // メイン側で扱いやすいようにリストに変換しています
        if (files != null && files.length > 0) {
            for (final File item : files) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * ギャラリーから画像ファイルリストを取得する。
     * 
     * @param cr
     *            ContentResolver
     * @return 画像ファイルリスト 存在しない場合は空のリスト
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static List<File> readBitmapGallaryFileList(final ContentResolver cr) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        final List<File> result = new ArrayList<File>();
        final Cursor cursor = cr.query(URI, // ストレージエリア
                null, // projection
                null, // selection
                null, // selectionArgs
                BaseColumns._ID + " desc"); // sortOrder
        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            try {
                while (cursor.moveToNext()) {
                    // for(String column : c.getColumnNames()){
                    // Utility.outputLog(context, column + " = " +
                    // c.getString(c.getColumnIndexOrThrow(column)));
                    // c.getString(c.getColumnIndexOrThrow(column));
                    // }
                    // ----------------------------------------------------
                    // DBからファイル名を抜き出した後、ファイル存在チェックを行い、存在する場合にデータを設定する
                    // ----------------------------------------------------
                    final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                    if (file.exists()) {
                        result.add(file);
                    }
                }
            } finally {
                Closeables.closeQuietly(cursor);
            }
        }
        return result;
    }

    /**
     * ギャラリーからファイル名を指定し、IDを取得する。
     * 
     * @param cr
     *            ContentResolver
     * @param filename
     *            ファイル名
     * @return ID(MediaStore.Images.Media._ID) 存在しない場合は0を返す
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static int readBitmapGallaryId(final ContentResolver cr, final String filename) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        int result = 0; // ID
        final Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // ストレージエリア
                new String[] { BaseColumns._ID, MediaColumns.DATA }, // projection(ID)
                MediaColumns.DATA + " = ?", // selection(ファイル名)
                new String[] { filename }, // selectionArgs(ファイル名)
                BaseColumns._ID + " asc"); // sortOrder
        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                if (file.exists()) {
                    result = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                }
            }
            Closeables.closeQuietly(cursor);
        }
        return result;
    }

    /**
     * ギャラリーからIDのリストを取得する。
     * 
     * @param cr
     *            ContentResolver
     * @return IDリスト 存在しない場合は空のリスト
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static List<Integer> readBitmapGallaryIdList(final ContentResolver cr) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        final List<Integer> result = new ArrayList<Integer>();
        final Cursor cursor = cr.query(URI, // ストレージエリア
                null, // projection
                null, // selection
                null, // selectionArgs
                BaseColumns._ID + " desc"); // sortOrder
        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            try {
                while (cursor.moveToNext()) {
                    // for(String column : c.getColumnNames()){
                    // Utility.outputLog(context, column + " = " +
                    // c.getString(c.getColumnIndexOrThrow(column)));
                    // c.getString(c.getColumnIndexOrThrow(column));
                    // }
                    // ----------------------------------------------------
                    // DBからファイル名を抜き出した後、ファイル存在チェックを行い、存在する場合にデータを設定する
                    // ----------------------------------------------------
                    final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                    if (file.exists()) {
                        result.add(cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
                    }
                }
            } finally {
                Closeables.closeQuietly(cursor);
            }
        }
        return result;
    }

    /**
     * SDカードへの書き込み処理(ファイル版) filePathは、絶対パスを指定してください。
     * 
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * @param data
     *            ファイルへのテキスト内容
     * @return 書き込み成功ならtrue、その他ならfalse
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static boolean write(final String filePath, final String data) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) {
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        boolean result = false; // 結果
        BufferedWriter bw = null; // BufferedWriter
        try {
            final String sdcardPath = getPath(filePath);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sdcardPath, true), "UTF-8"));
            bw.write(data);
            result = true; // 書き込み成功
        } catch (final UnsupportedEncodingException e) {
            return false;
        } catch (final FileNotFoundException e) {
            return false;
        } catch (final IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(bw);
        }
        return result;
    }

    /**
     * SDカードへの書き込み処理(画像版) filePathは、絶対パスを指定してください。
     * 
     * @param bitmap
     *            画像
     * @param filePath
     *            ファイル名をパスで指定(例：test.txt)
     * @return true:書き込み成功<br>
     *         false:書き込み失敗
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static boolean write(final String filePath, final Bitmap bitmap) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }
        FileOutputStream fileOutputStream = null;
        try {
            final File file = new File(filePath);
            file.mkdirs(); // ディレクトリを作る.
            file.delete(); // ファイルを削除する
            if (file.createNewFile()) {
                fileOutputStream = new FileOutputStream(filePath);
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
        } catch (final IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(fileOutputStream);
        }
    }

    /**
     * SDカードへの書き込み処理(画像版) ファイル名は自動的に付与するため、ディレクトリ名のみ指定
     * 
     * @param cr
     *            ContentResolver
     * @param name
     *            画像タイトル
     * @param context
     *            Context
     * @param bitmap
     *            ビットマップ
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * @return 書き込み成功ならtrue、その他ならfalse
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static boolean write(final ContentResolver cr, final String name, final Context context, final Bitmap bitmap, final String filePath) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }
        FileOutputStream outputStream = null;

        try {
            final long dateTaken = System.currentTimeMillis(); // 現在のmsec
            final String filename = StringUtils.appendBuilder(getPath(filePath), "/", createFilename(dateTaken).concat(".jpg")); // ファイル名
            // ----------------------------------------------------
            // ディレクトリが存在しない場合作成する
            // ----------------------------------------------------
            final File fdir = new File(StringUtils.appendBuilder(getPath(filePath)));
            if (!fdir.exists()) {
                fdir.mkdirs();
            }
            // ----------------------------------------------------
            // ファイルが存在しない場合作成する
            // ----------------------------------------------------
            final File ffilename = new File(filename);
            ffilename.delete();

            if (ffilename.createNewFile()) {
                outputStream = new FileOutputStream(ffilename);
                final String mimetype = ImageUtils.getImageMimeType(ffilename.getPath());
                if (StringUtils.equals(mimetype, "image/png")) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                } else if (StringUtils.equals(mimetype, "image/jpg")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                }
                outputStream.flush();

                final ContentValues values = new ContentValues();
                values.put(MediaColumns.TITLE, createFilename(dateTaken)); // _title
                values.put(MediaColumns.SIZE, ffilename.length()); // _size
                values.put(MediaColumns.DATE_ADDED, dateTaken / 1000); // _date_added
                values.put(MediaColumns.DATE_MODIFIED, dateTaken / 1000); // date_modified
                values.put(ImageColumns.DATE_TAKEN, dateTaken); // datetaken
                values.put(MediaColumns.MIME_TYPE, mimetype); // mime type
                cr.insert(URI, values);
            }
        } catch (final FileNotFoundException e) {
            return false;
        } catch (final IOException e) {
            return false;
        } finally {
            Closeables.closeQuietly(outputStream);
        }
        return true;
    }

    /**
     * SDカード(ギャラリー保存）への書き込み(Insert)処理(画像版)
     * 
     * @param cr
     *            ContentResolver
     * @param bitmap
     *            ビットマップ
     * @return ギャラリーへ保存したファイル名を取得
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static String writeBitmapGallary(final ContentResolver cr, final Bitmap bitmap) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        String result = ""; // ファイル名
        final long dateTaken = System.currentTimeMillis(); // 現在のmsec
        final String title = createFilename(dateTaken); // ファイル名生成
        final String uriStr = MediaStore.Images.Media.insertImage(cr, bitmap, title, null); // 画像を保存
        // ----------------------------------------------------
        // 1.画像登録後、画像詳細情報を登録するために、ファイル名を取得する
        // 2.そのファイル名を取得し、ヒットした場合、各画像情報を登録する
        // ----------------------------------------------------
        final Uri uri = Uri.parse(uriStr); // Uriインスタンス生成
        final Cursor cursor = cr.query(uri, // ストレージエリアにある先程登録したファイル名を取得
                new String[] { MediaColumns.DATA }, // _data
                null, null, null);
        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            if (cursor.getCount() > 0) {
                cursor.moveToFirst(); // 最初の1件目に移動
                final File file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                final ContentValues values = new ContentValues();
                values.put(MediaColumns.TITLE, createFilename(dateTaken)); // _title
                values.put(MediaColumns.SIZE, file.length()); // _size
                values.put(MediaColumns.DATE_ADDED, dateTaken / 1000); // _date_added
                values.put(MediaColumns.DATE_MODIFIED, dateTaken / 1000); // date_modified
                values.put(ImageColumns.DATE_TAKEN, dateTaken); // datetaken
                // mime type
                values.put(MediaColumns.MIME_TYPE, ImageUtils.getImageMimeType(file.getPath()));
                cr.update(uri, values, null, null);
                result = file.getPath();
            }
            Closeables.closeQuietly(cursor);
        }
        return result;
    }

    /**
     * SDカード(ギャラリー保存）への書き込み(update)処理(画像版)
     * 
     * @param cr
     *            ContentResolver
     * @param bitmap
     *            ビットマップ
     * @param filename
     *            ファイル名
     * @return 書き込み成功ならtrue、その他ならfalse
     * @throws IOException
     *             SDカードがマウントされていない場合
     */
    public static boolean updateBitmapGallary(final ContentResolver cr, final Bitmap bitmap, final String filename) throws IOException {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            throw new IOException(IOEXCEPTION_SDCARD_MESSAGE);
        }

        final long dateTaken = System.currentTimeMillis(); // 現在のmsec
        Cursor cursor = null; // Cursorインスタンス
        Cursor cursor1 = null; // Cursorインスタンス
        File file = null; // Fileインスタンス

        try {
            cursor = cr.query(URI, // ストレージエリア
                    new String[] { BaseColumns._ID, MediaColumns.DATA },// ID,DATA
                    MediaColumns.DATA + " = ?", // データ
                    new String[] { filename }, // ファイル名
                    null);
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                    final String id = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID));

                    final ContentValues values = new ContentValues();
                    // values.put(MediaStore.Images.Media.TITLE,
                    // createFilename(dateTaken)); //_title
                    values.put(MediaColumns.SIZE, file.length()); // _size
                    // values.put(MediaStore.Images.Media.DATE_ADDED, dateTaken
                    // / 1000); //_date_added
                    values.put(MediaColumns.DATE_MODIFIED, dateTaken / 1000); // date_modified
                    values.put(ImageColumns.DATE_TAKEN, dateTaken); // datetaken
                    // mime type
                    values.put(MediaColumns.MIME_TYPE, ImageUtils.getImageMimeType(file.getPath()));
                    final Uri uri = ContentUris.appendId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon(), cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))).build();
                    cr.update(uri, values, MediaColumns.DATA + " = ?", // データ
                            new String[] { filename }); // ファイル名
                    // ----------------------------------------------------
                    // サムネイルの削除
                    // ----------------------------------------------------
                    cursor1 = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // ストレージエリア
                            new String[] { MediaStore.Images.Thumbnails.DATA }, // ID,DATA
                            MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", // 元画像ID
                            new String[] { id }, // 元画像ID
                            null);
                    if (cursor1.getCount() > 0) {
                        cursor1.moveToFirst();
                        final int index = cursor1.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                        LogUtils.d("info", cursor1.getString(index));
                        file = new File(cursor1.getString(index));
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    // ----------------------------------------------------
                    // ファイル保存
                    // ----------------------------------------------------
                    final FileOutputStream outputStream = new FileOutputStream(filename);
                    final String mimetype = ImageUtils.getImageMimeType(filename);
                    if (StringUtils.equals(mimetype, "image/png")) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    } else if (StringUtils.equals(mimetype, "image/jpg")) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    }
                    outputStream.flush();
                    Closeables.closeQuietly(outputStream);
                }
                return true;
            }
        } finally {
            Closeables.closeQuietly(cursor);
            Closeables.closeQuietly(cursor1);
        }
        return false;
    }

    /**
     * ギャラリーからファイルを取得する。<br>
     * 取得できない場合は、nullを返却する。
     * 
     * @param cr
     *            ContentResolver
     * @param uri
     *            Uri
     * @return ファイル
     */
    public static File getBitmapGallaryFile(final ContentResolver cr, final Uri uri) {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            return null;
        }
        File file = null;
        final String[] columns = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };

        final Cursor cursor = cr.query(uri, columns, null, null, null);

        if (cursor != null) { // SDカードがない場合等nullの可能性あり
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                if (cursor.getColumnIndex(MediaColumns.DATA) != -1 && cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)) != null) {
                    file = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA)));
                }
            }
            Closeables.closeQuietly(cursor);
        }
        return file;
    }

    /**
     * ギャラリーからファイル詳細情報を取得する(ファイルネーム)<br>
     * 取得できない場合は、nullを返却する。
     * 
     * @param cr
     *            ContentResolver
     * @param filename
     *            ファイル名(ファイル詳細情報を取得するための検索キー)
     * @return ファイル情報
     */
    public static FileResource getDetailFileGallary(final ContentResolver cr, final String filename) {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            return null;
        }
        Cursor cursor = null;
        try {
            cursor = cr.query(URI, // ストレージエリア
                    new String[] { BaseColumns._ID, // ID
                            MediaColumns.TITLE, // タイトル
                            MediaColumns.DATE_MODIFIED, // 修正日付
                            MediaColumns.SIZE, // ファイルサイズ
                            MediaColumns.MIME_TYPE, // mime_type
                    }, MediaColumns.DATA + " = ?", // データ
                    new String[] { filename }, // ファイル名
                    null);
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                final FileResource fileResource = new FileResource();
                fileResource.setTitle(cursor.getString(cursor.getColumnIndex(MediaColumns.TITLE)));
                fileResource.setMimeType(cursor.getString(cursor.getColumnIndex(MediaColumns.MIME_TYPE)));
                fileResource.setSize(cursor.getLong(cursor.getColumnIndex(MediaColumns.SIZE)));
                fileResource.setDateModified(cursor.getLong(cursor.getColumnIndex(MediaColumns.DATE_MODIFIED)));
                return fileResource;
            }
        } finally {
            Closeables.closeQuietly(cursor);
        }
        return null;
    }

    /**
     * ギャラリーからファイル詳細情報を取得する(ID) 取得できない場合は、nullを返却する。
     * 
     * @param cr
     *            ContentResolver
     * @param id
     *            ID(ファイル詳細情報を取得するための検索キー)
     * @return 削除成功ならtrue、その他ならfalse
     */
    public static FileResource getDetailFileGallary(final ContentResolver cr, final int id) {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            return null;
        }

        Cursor cursor = null;
        try {
            cursor = cr.query(URI, // ストレージエリア
                    new String[] { BaseColumns._ID, // ID
                            MediaColumns.TITLE, // タイトル
                            MediaColumns.DATE_MODIFIED, // 修正日付
                            MediaColumns.SIZE, // ファイルサイズ
                            MediaColumns.MIME_TYPE, // mime_type
                    }, BaseColumns._ID + " = ?", // データ
                    new String[] { String.valueOf(id) }, // ID
                    null);
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                final FileResource fileResource = new FileResource();
                fileResource.setTitle(cursor.getString(cursor.getColumnIndex(MediaColumns.TITLE)));
                fileResource.setMimeType(cursor.getString(cursor.getColumnIndex(MediaColumns.MIME_TYPE)));
                fileResource.setSize(cursor.getLong(cursor.getColumnIndex(MediaColumns.SIZE)));
                fileResource.setDateModified(cursor.getLong(cursor.getColumnIndex(MediaColumns.DATE_MODIFIED)));
                return fileResource;
            }
        } finally {
            Closeables.closeQuietly(cursor);
        }
        return null;
    }

    /**
     * 指定したファイルを削除します。
     * 
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * @return true:ファイル削除成功<br>
     *         false:ファイル削除失敗<br>
     */
    public static boolean delete(final String filePath) {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            return false;
        }
        final File file = new File(getPath(filePath));
        if (file.exists() && file.isFile() && file.canWrite()) {
            return file.delete();
        }
        return false;
    }

    /**
     * ギャラリーからの削除処理
     * 
     * @param cr
     *            ContentResolver
     * @param filename
     *            削除するファイル名
     * @return 削除成功ならtrue、その他ならfalse
     */
    public static boolean deleteBitmapGallary(final ContentResolver cr, final String filename) {
        // ----------------------------------------------------
        // SDカードマウント判定
        // ----------------------------------------------------
        if (!isWrite()) { // SDカードマウント不可の場合
            return false;
        }

        Cursor cursor = null;
        try {
            cursor = cr.query(URI, // ストレージエリア
                    new String[] { BaseColumns._ID }, // ID
                    MediaColumns.DATA + " = ?", // データ
                    new String[] { filename }, // ファイル名
                    null);
            if (cursor.getCount() != 0) { // 削除データがある場合
                cursor.moveToFirst();
                final Uri uri = ContentUris.appendId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon(), cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))).build();
                cr.delete(uri, null, null);
                return true;
            }
        } finally {
            Closeables.closeQuietly(cursor);
        }
        return false;
    }

    /**
     * ファイル名の自動生成処理(時間名でファイル名を生成する)
     * 
     * @param dateTaken
     *            現在のmsec
     * @return ファイル名
     */
    private static String createFilename(final long dateTaken) {
        return DateFormat.format("yyyy-MM-dd_hhmmss", dateTaken).toString();
    }

    /**
     * SDカードマウント判定
     * 
     * @return true:マウント可能 false:マウント不可能
     */
    public static boolean isWrite() {
        final String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED); // SDカードのマウント判定
    }

    /**
     * SDカードのパス取得<br>
     * パスが取得できない場合は、nullを返却する。
     * 
     * @return SDカードのパス名
     */
    public static File getPath() {
        if (isWrite()) {
            return Environment.getExternalStorageDirectory();
        } else { // マウントできない場合
            return null;
        }
    }

    /**
     * SDカードのパス取得<br>
     * パスが取得できない場合は、nullを返却する。
     * 
     * @param filePath
     *            ファイル名(SDカードのパス以下から指定を行う)
     * 
     * @return SDカードのパス名
     */
    public static String getPath(final String filePath) {
        if (isWrite()) {
            return StringUtils.appendBuilder(getPath().getPath(), filePath);
        }
        return null;
    }

    /**
     * Get the external app cache directory.
     * 
     * @return SDカードのパス名
     */
    public static String getCachePath() {
        if (AplUtils.hasFroyo()) {
            if (sContext.getExternalCacheDir() != null) {
                final String cacheDir = sContext.getExternalCacheDir().getPath();
                if (cacheDir != null) {
                    return StringUtils.appendBuilder(cacheDir, "/");
                }
            }
        }
        // Froyo 以前は自前でディレクトリを作成する
        final String cacheDir = StringUtils.appendBuilder("/Android/data/", sContext.getPackageName(), "/cache/");
        // ----------------------------------------------------
        // ディレクトリが存在しない場合作成する
        // ----------------------------------------------------
        final File fdir = new File(cacheDir);
        if (!fdir.exists()) {
            fdir.mkdirs();
        }

        return StringUtils.appendBuilder(Environment.getExternalStorageDirectory().getPath(), cacheDir);
    }

    /**
     * Get the external app cache directory.
     * 
     * @param filename
     *            ファイル名
     * @return SDカードのパス名
     */
    public static String getCachePath(final String filename) {
        if (AplUtils.hasFroyo()) {
            if (sContext.getExternalCacheDir() != null) {
                final String cacheDir = sContext.getExternalCacheDir().getPath();
                if (cacheDir != null) {
                    return StringUtils.appendBuilder(cacheDir, "/", filename);
                }
            }
        }
        // Froyo 以前は自前でディレクトリを作成する
        final String cacheDir = StringUtils.appendBuilder("/Android/data/", sContext.getPackageName(), "/cache/");
        // ----------------------------------------------------
        // ディレクトリが存在しない場合作成する
        // ----------------------------------------------------
        final File fdir = new File(cacheDir);
        if (!fdir.exists()) {
            fdir.mkdirs();
        }

        return StringUtils.appendBuilder(Environment.getExternalStorageDirectory().getPath(), cacheDir, filename);
    }

    /**
     * ファイルの存在有無を返却する
     * 
     * @param filePath
     *            ファイルパス(フルパスで指定する。)
     * @return true:ファイルが存在する。<br>
     *         false:ファイルが存在しない。
     */
    public static boolean isExists(final String filePath) {
        final File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * SDカードが指定サイズ以上の容量があるかをチェックする
     * 
     * @param size
     *            指定容量
     * @return true:指定容量以上の空きサイズあり<br>
     *         false:指定容量以下の空きサイズしかない<br>
     * 
     */
    public static boolean isUsableSpace(final long size) {
        return getUsableSpace() >= size;
    }

    /**
     * SDカードの空きサイズ取得
     * 
     * @return SD空きカードサイズ
     */
    @SuppressWarnings("deprecation")
    public static long getUsableSpace() {
        final File file = Environment.getExternalStorageDirectory();
        final StatFs statFs = new StatFs(file.getAbsolutePath());
        try {
            return statFs.getFreeBytes();
        } catch (final NoSuchMethodError e) {
            return (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }
    }

    /**
     * SDカードの最大サイズ取得
     * 
     * @return SDカードサイズ
     */
    @SuppressWarnings("deprecation")
    public static long getTotalSpace() {
        final File file = Environment.getExternalStorageDirectory();
        final StatFs statFs = new StatFs(file.getAbsolutePath());
        try {
            return statFs.getTotalBytes();
        } catch (final NoSuchMethodError e) {
            return (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
        }
    }
}
