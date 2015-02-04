package com.miya38.triming;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.widget.LinearLayout;

import com.miya38.R;
import com.miya38.utils.AplUtils;
import com.miya38.utils.FileSdCardUtils;
import com.miya38.utils.ImageUtils;
import com.miya38.utils.LogUtils;

/**
 * 画像トリミングクラス
 *
 * @author y-miyazaki
 *
 */
@SuppressWarnings ("javadoc")
public class TrimingActivity extends MonitoredActivity {
    private static final String TAG = TrimingActivity.class.getSimpleName();

    // These are various options can be specified in the intent.
    private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only used with mSaveUri
    private Uri mSaveUri = null;
    private int mAspectX, mAspectY;
    private boolean mCircleCrop = false;
    private final Handler mHandler = new Handler();

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mOutputX, mOutputY;
    private boolean mScale;
    private boolean mScaleUp = true;

    // 顔認識フラグオフ
    private boolean mDoFaceDetection = false;

    public boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    public boolean mSaving; // Whether the "save" button is already clicked.

    private CropImageView mImageView;
    private ContentResolver mContentResolver;

    private Bitmap _bmOriginal;

    private Bitmap mBitmap;
    private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
    public HighlightView mCrop;

    private IImage mImage;

    private String mImagePath;

    private boolean isStartTrim = false;

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.activity.common.MonitoredActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_triming_layout);
        mContentResolver = getContentResolver();

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        mImageView = (CropImageView) findViewById(R.id.CustomImageView01);

        isStartTrim = false;

        // パラメータ取得
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getString("circleCrop") != null) {
                mCircleCrop = true;
                mAspectX = 1;
                mAspectY = 1;
            }
            // 画像ファイルパス
            mImagePath = extras.getString("mImageUri");
            LogUtils.d(TAG, "onCreate mImagePath[%s]", mImagePath);

            // ----------------------------------------------
            // サイズを考慮し画面サイズ程度に抑える。
            // ----------------------------------------------
            int width = AplUtils.getWindowWidth();
            int height = AplUtils.getWindowHeight();
            _bmOriginal = ImageUtils.imageResize(mImagePath, (width > height) ? height : width, (width > height) ? height : width);

            if (_bmOriginal == null) {
                finish();
                return;
            }

            // その他パラメータ(とりあえずデフォルトのまま使用)
            mSaveUri = getImageUri(mImagePath);
            mAspectX = extras.getInt("aspectX");
            mAspectY = extras.getInt("aspectY");

            // 切り抜き画像のリサイズ設定(200*200)
            // mOutputX = extras.getInt("outputX");
            mOutputX = 200;
            // mOutputY = extras.getInt("outputY");
            mOutputY = 200;

            // 切り抜き画像のリサイズ有効(True)
            mScale = extras.getBoolean("scale", true);
            mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);

            // Make UI fullscreen.
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // findViewById(R.id.CustomButton01).setOnClickListener(new View.OnClickListener() {
            // public void onClick(View v) {
            // setResult(RESULT_CANCELED);
            // finish();
            // }
            // });

            // トリミングボタン
            findViewById(R.id.CustomButton01).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onSaveClicked();
                }
            });

            // 左へ回転ボタン
            findViewById(R.id.CustomButton02).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // mBitmap = scaleFramLayout(TrimingUtil.rotateImage(mBitmap, -90));
                    mBitmap = TrimingUtil.rotateImage(mBitmap, -90);
                    RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                    mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                    mRunFaceDetection.run();
                }
            });
            // 右へ回転ボタン
            findViewById(R.id.CustomButton03).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // mBitmap = scaleFramLayout(TrimingUtil.rotateImage(mBitmap, 90));
                    mBitmap = TrimingUtil.rotateImage(mBitmap, 90);
                    RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
                    mImageView.setImageRotateBitmapResetBase(rotateBitmap, true);
                    mRunFaceDetection.run();
                }
            });
        }
    }

    /*
     * (非 Javadoc)
     * @see android.app.Activity#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!isStartTrim) {
            isStartTrim = true;
            if (_bmOriginal != null) {
                mBitmap = scaleFramLayout(_bmOriginal);
                // "Canvas: trying to use a recycled bitmap android.graphics"対策
                // createScaledBitmap can return source bitmap when size is equal as target.
                // check if we can just return our argument unchanged
                // 参考URL：http://stackoverflow.com/questions/7983321/android-trying-to-use-a-recycled-bitmap-error-with-temporary-bitmaps
                if (_bmOriginal != mBitmap) {
                    // サイズ調整前ファイルとサイズ調整後ファイルが異なる場合は調整前ファイルを破棄
                    LogUtils.d(TAG, "onWindowFocusChanged _bmOriginal.recycle()");
                    _bmOriginal.recycle();
                }
            }
            // トリミング開始
            startFaceDetection();
        }

        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * FramLayoutのサイズに合わせて画像をリサイズ
     *
     * @param bitmap
     *            オリジナル画像ファイル
     * @return
     *         サイズ調整後画像ファイル
     */
    private Bitmap scaleFramLayout(Bitmap bitmap) {

        int dstWidth = 0;
        int dstHeight = 0;

        // ----------------------------------------------
        // LinearLayoutの高さ・幅を取得する
        // ----------------------------------------------
        int _width = ((LinearLayout) findViewById(R.id.li1)).getWidth();
        int _height = ((LinearLayout) findViewById(R.id.li1)).getHeight();

        // ----------------------------------------------
        // FrameLayoutに対して画像のスケールを計算し、スケールの小さい方を取得しビットマップを作成する。
        // ----------------------------------------------
        float _scaleW = (float) _width / (float) bitmap.getWidth();
        float _scaleH = (float) _height / (float) bitmap.getHeight();
        final float _scale = Math.min(_scaleW, _scaleH);

        // "IllegalArgumentException: width and height must be > 0 "対策
        // 参考URL：http://stackoverflow.com/questions/2643705/illegalargumentexception-width-and-height-must-be-0-during-zoomout-in-google
        // Matrix matrix = new Matrix();
        // matrix.postScale(_scale, _scale);
        // return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        dstWidth = (int) (bitmap.getWidth() * _scale);
        dstHeight = (int) (bitmap.getHeight() * _scale);
        LogUtils.d(TAG, "scaleFramLayout width[%d->%d] height[%d->%d]", bitmap.getWidth(), dstWidth, bitmap.getHeight(), dstHeight);
        return Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
    }

    /**
     * getImageUri
     *
     * @param path
     * @return
     */
    private Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    /**
     * BitMap取得
     *
     * @param path
     * @return
     */
    private Bitmap getBitmap(String path) {
        Uri uri = getImageUri(path);
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 2048;
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            return b;
        } catch (FileNotFoundException e) {
            LogUtils.e(TAG, "file %s not found", path);
        } catch (IOException e) {
            LogUtils.e(TAG, "file %s not found", path);
        }
        return null;
    }

    /**
     * トリミング処理開始
     */
    private void startFaceDetection() {
        if (isFinishing()) {
            return;
        }

        mImageView.setImageBitmapResetBase(mBitmap, true);

        TrimingUtil.startBackgroundJob((MonitoredActivity) this, null, "Please wait\u2026", new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = (mImage != null) ? mImage.fullSizeBitmap(IImage.UNCONSTRAINED, 1024 * 1024) : mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        if (b != mBitmap && b != null) {
                            mImageView.setImageBitmapResetBase(b, true);
                            mBitmap.recycle();
                            mBitmap = b;
                        }
                        if (mImageView.getScale() == 1F) {
                            mImageView.center(true, true);
                        }
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                mRunFaceDetection.run();
            }
        }, mHandler);
    }

    /**
     * トリミングボタン押下
     */
    private void onSaveClicked() {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving)
            return;

        if (mCrop == null) {
            return;
        }

        mSaving = true;

        try {
            Rect r = mCrop.getCropRect();
            int width = r.width();
            int height = r.height();

            // If we are circle cropping, we want alpha channel, which is the
            // third param here.
            Bitmap croppedImage = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            {
                Canvas canvas = new Canvas(croppedImage);
                Rect dstRect = new Rect(0, 0, width, height);
                canvas.drawBitmap(mBitmap, r, dstRect, null);
            }

            // Intentの引数に「circleCrop」が設定されていなければ、通らない
            // if (mCircleCrop) {
            // // OK, so what's all this about?
            // // Bitmaps are inherently rectangular but we want to return
            // // something that's basically a circle. So we fill in the
            // // area around the circle with alpha. Note the all important
            // // PortDuff.Mode.CLEAR.
            // Canvas c = new Canvas(croppedImage);
            // Path p = new Path();
            // p.addCircle(width / 2F, height / 2F, width / 2F,
            // Path.Direction.CW);
            // c.clipPath(p, Region.Op.DIFFERENCE);
            // c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
            // }

            /* If the output is required to a specific size then scale or fill */

            // スケール指定
            if (mOutputX != 0 && mOutputY != 0) {
                if (mScale) {
                    /* Scale the image to the required dimensions */
                    Bitmap old = croppedImage;
                    LogUtils.d(TAG, "Scale Org size width[%d]height[%d]", old.getWidth(), old.getHeight());

                    // 画像を200*200ピクセルにリサイズ
                    croppedImage = TrimingUtil.transform(new Matrix(), croppedImage, mOutputX, mOutputY, mScaleUp);
                    if (old != croppedImage) {
                        old.recycle();
                    }
                } else {

                    /*
                     * Don't scale the image crop it to the size requested.
                     * Create an new image with the cropped image in the center and
                     * the extra space filled.
                     */

                    // Don't scale the image but instead fill it so it's the
                    // required dimension
                    Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(b);

                    Rect srcRect = mCrop.getCropRect();
                    Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

                    int dx = (srcRect.width() - dstRect.width()) / 2;
                    int dy = (srcRect.height() - dstRect.height()) / 2;

                    /* If the srcRect is too big, use the center part of it. */
                    srcRect.inset(Math.max(0, dx), Math.max(0, dy));

                    /* If the dstRect is too big, use the center part of it. */
                    dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

                    /* Draw the cropped bitmap in the center */
                    canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

                    /* Set the cropped bitmap as the new bitmap */
                    croppedImage.recycle();
                    croppedImage = b;
                }
            }

            // Return the cropped image directly or save it to the specified URI.

            // 別名でファイルに書き込む
            // ファイル名_trim.xxx
            mImagePath = String.format("%s_trim%s", mImagePath.substring(0, mImagePath.lastIndexOf('.')), mImagePath.substring(mImagePath.lastIndexOf('.'), mImagePath.length()));
            FileSdCardUtils.write(mImagePath, croppedImage);
            LogUtils.d(TAG, "setResult OK writeBitmapToFile After[%s] width[%d]height[%d]", mImagePath, croppedImage.getWidth(), croppedImage.getHeight());

            // 呼び出し元画面へ戻す
            Intent intent = new Intent();
            intent.putExtra("file", mImagePath);
            setResult(RESULT_OK, intent);
        } catch (Exception e) {
            LogUtils.d(TAG, "setResult Cancel");
            setResult(RESULT_CANCELED);
        } finally {
            finish();
        }
    }

    private void saveOutput(Bitmap croppedImage) {
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 75, outputStream);
                }
            } catch (IOException ex) {
                // TODO: report error to caller
                LogUtils.e(TAG, "Cannot open file %s", mSaveUri, ex);
            } finally {
                TrimingUtil.closeSilently(outputStream);
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString()).putExtras(extras));
        } else {
            LogUtils.e(TAG, "not defined image url");
        }
        croppedImage.recycle();
        finish();
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.activity.AbstractActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
    }

    /*
     * (非 Javadoc)
     * @see jp.co.mote.aune.activity.common.MonitoredActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    Runnable mRunFaceDetection = new Runnable() {
        @SuppressWarnings ("hiding")
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);

            mImageView.mHighlightViews.clear(); // Thong added for rotate

            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        // Toast t = Toast.makeText(TrimingActivity.this,
                        // "Multi face crop help",
                        // Toast.LENGTH_SHORT);
                        // t.show();
                    }
                }
            });
        }
    };

    public static final int NO_STORAGE_ERROR = -1;
    public static final int CANNOT_STAT_ERROR = -2;

    public static void showStorageToast(Activity activity) {
        showStorageToast(activity, calculatePicturesRemaining());
    }

    public static void showStorageToast(Activity activity, int remaining) {
        String noStorageText = null;

        if (remaining == NO_STORAGE_ERROR) {
            String state = Environment.getExternalStorageState();
            if (state == Environment.MEDIA_CHECKING) {
                noStorageText = "Preparing card";
            } else {
                noStorageText = "No storage card";
            }
        } else if (remaining < 1) {
            noStorageText = "Not enough space";
        }

        if (noStorageText != null) {
            // Toast.makeText(activity, noStorageText, 5000).show();
        }
    }

    @SuppressWarnings("deprecation")
    public static int calculatePicturesRemaining() {
        try {
            /*
             * if (!ImageManager.hasStorage()) {
             * return NO_STORAGE_ERROR;
             * } else {
             */
            String storageDirectory = Environment.getExternalStorageDirectory().toString();
            StatFs stat = new StatFs(storageDirectory);
            float remaining = ((float) stat.getAvailableBlocks() * (float) stat.getBlockSize()) / 400000F;
            return (int) remaining;
            // }
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining. it might be zero but just leave it
            // blank since we really don't know.
            return CANNOT_STAT_ERROR;
        }
    }
}
