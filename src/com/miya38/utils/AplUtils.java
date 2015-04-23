package com.miya38.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * アプリケーションユーティリティ
 * 
 * @author y-miyazaki
 */
public final class AplUtils {
    // ----------------------------------------------------------
    // ohter
    // ----------------------------------------------------------
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private AplUtils() {
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
     * ブランドを返却する 例：SHARP
     * 
     * @return ブランド
     */
    public static String getBuildBrand() {
        return Build.BRAND;
    }

    /**
     * 製品名を返却する 例：JNDK01
     * 
     * @return Product
     */
    public static String getBuildProduct() {
        return Build.PRODUCT;
    }

    /**
     * モデルを返却する 例：JN-DK01
     * 
     * @return モデル
     */
    public static String getBuildModel() {
        return Build.MODEL;
    }

    /**
     * BuildVersionを返却する 例：8
     * 
     * @return BuildVersion
     */
    public static int getBuildVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * ReleaseVersionを返却する 例：2.2
     * 
     * @return Release
     */
    public static String getBuildRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Eclair(5)以上のバージョンかチェック
     * 
     * @return true:Eclair以上 false;Eclair未満
     */
    public static boolean hasEclair() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    /**
     * Froyo(2.2/8)以上のバージョンかチェック
     * 
     * @return true:Froyo以上 false;Froyo未満
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Gingerbread(2.3/9)以上のバージョンかチェック
     * 
     * @return true:Gingerbread以上 false;Gingerbread未満
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Gingerbread Mr1(2.3.3-2.3.7/10)以上のバージョンかチェック
     * 
     * @return true:Gingerbread Mr1以上 false;Gingerbread Mr1未満
     */
    public static boolean hasGingerbreadMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    /**
     * Honeycomb(3.0/11)以上のバージョンかチェック
     * 
     * @return true:Honeycomb以上 false;Honeycomb未満
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Honeycomb Mr1(3.1/12)以上のバージョンかチェック
     * 
     * @return true:Honeycomb Mr1以上 false;Honeycomb Mr1未満
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Honeycomb Mr1(3.2/13)以上のバージョンかチェック
     * 
     * @return true:Honeycomb Mr1以上 false;Honeycomb Mr1未満
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * Ice Cream Sandwich(4.0/14)以上のバージョンかチェック
     * 
     * @return true:Ice Cream Sandwich以上 false;Ice Cream Sandwich未満
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Ice Cream Sandwich(4.0.03/15)以上のバージョンかチェック
     * 
     * @return true:Ice Cream Sandwich Mr1以上 false;Ice Cream Sandwich Mr1未満
     */
    public static boolean hasIceCreamSandwichMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * JellyBean(4.1/16)以上のバージョンかチェック
     * 
     * @return true:JellyBean以上 false;JellyBean未満
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * JellyBean(4.2/17)以上のバージョンかチェック
     * 
     * @return true:JellyBean Mr1以上 false;JellyBean Mr1未満
     */
    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * JellyBean(4.3/18)以上のバージョンかチェック
     * 
     * @return true:JellyBean Mr2以上 false;JellyBean Mr2未満
     */
    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * JellyBean(4.4/19)以上のバージョンかチェック
     * 
     * @return true:Kitkat以上 false;Kitkat未満
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * JellyBean(5.0/21)以上のバージョンかチェック
     * 
     * @return true:Lollipop以上 false;Lollipop未満
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * アプリケーションバージョンを返却する
     * 
     * @return アプリケーションバージョン文字列
     */
    public static String getAplVersion() {
        try {
            return sContext.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionName;
        } catch (final NameNotFoundException e) {
            return null;
        }
    }

    /**
     * アプリケーションバージョンコードを返却する
     * 
     * @return アプリケーションバージョンコード文字列
     */
    public static int getAplVersionCode() {
        try {
            return sContext.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionCode;
        } catch (final NameNotFoundException e) {
            return 1;
        }
    }

    /**
     * アプリケーションパッケージ名を返却する
     * 
     * @return アプリケーションパッケージ名
     */
    public static String getPackageName() {
        return sContext.getPackageName();
    }

    /**
     * 画面サイズの横幅を取得(px)
     * 
     * @return 画面の横幅
     */
    @SuppressWarnings("deprecation")
    public static int getWindowWidth() {
        final WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        if (AplUtils.hasHoneycombMR2()) {
            final Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    /**
     * 画面サイズの縦幅を取得(px)
     * 
     * @return 画面の縦幅
     */
    @SuppressWarnings("deprecation")
    public static int getWindowHeight() {
        final WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        if (AplUtils.hasHoneycombMR2()) {
            final Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    /**
     * キーボードを閉じる
     * 
     * @param activity
     *            Activity
     */
    public static void closeKeyboard(final Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * キーボードを閉じる
     * 
     * @param context
     *            Context
     * @param view
     *            View
     */
    public static void closeKeyboard(final Context context, final View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * dp→px変換
     * 
     * @param dp
     *            dp
     * 
     * @return dpからpxに変換された値を返却する
     */
    public static int getDpToPx(final int dp) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px→dp変換
     * 
     * @param px
     *            px
     * 
     * @return pxからdpに変換された値を返却する
     */
    public static int getPxToDp(final int px) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale);
    }

    /**
     * sp→px変換
     * 
     * @param sp
     *            sp
     * 
     * @return spからpxに変換された値を返却する
     */
    public static int getSpToPx(final int sp) {
        final float scale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * (scale + 0.5f));
    }

    /**
     * アプリが起動中か確認する
     * 
     * @param appName
     *            アプリケーション名
     * 
     * @return true:起動中<br>
     *         false:未起動
     */
    public static boolean hasRunningApplication(final String appName) {
        final ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);

        // 起動中のアプリ情報を取得
        final List<RunningAppProcessInfo> runningApp = activityManager.getRunningAppProcesses();
        final PackageManager packageManager = sContext.getPackageManager();
        if (!CollectionUtils.isNullOrEmpty(runningApp)) {
            for (final RunningAppProcessInfo app : runningApp) {
                try {
                    // アプリ名をリストに追加
                    final ApplicationInfo appInfo = packageManager.getApplicationInfo(app.processName, 0);
                    if (appName.equals(packageManager.getApplicationLabel(appInfo).toString())) {
                        return true;
                    }
                } catch (final NameNotFoundException e) {
                    // ネームが取れないことが普通にあるので、握りつぶす
                }
            }
        }
        return false;
    }

    /**
     * 自分のアプリのアクティビティが起動しているか確認する
     * 
     * @return true:起動中<br>
     *         false:未起動
     */
    public static boolean hasApplicationActivityRunning() {
        final String packageName = getPackageName();
        final ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(100);
        if (!CollectionUtils.isNullOrEmpty(tasks)) {
            for (final ActivityManager.RunningTaskInfo runningTaskInfo : tasks) {
                if (runningTaskInfo.topActivity.getClassName().startsWith(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定のパッケージに属するアクティビティが起動しているか確認する
     * 
     * @param activityName
     *            パッケージ名を含めたアクティビティ名を指定すること(ex...com.example.activity.
     *            HomeActivity)
     * 
     * @return true:起動中<br>
     *         false:未起動
     */
    public static boolean hasApplicationActivityRunning(final String activityName) {
        final ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(100);
        if (!CollectionUtils.isNullOrEmpty(tasks)) {
            for (final ActivityManager.RunningTaskInfo runningTaskInfo : tasks) {
                if (runningTaskInfo.topActivity.getClassName().equals(activityName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定のパッケージに属するアクティビティが起動しているか確認する
     * 
     * @param activityName
     *            パッケージ名まで指定すること(ex...com.example)
     * 
     * @return true:起動中<br>
     *         false:未起動
     */
    public static boolean hasApplicationActivityRunningByPackageName(final String activityName) {
        final ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(100);
        if (!CollectionUtils.isNullOrEmpty(tasks)) {
            for (final ActivityManager.RunningTaskInfo runningTaskInfo : tasks) {
                if (runningTaskInfo.topActivity.getClassName().contains(activityName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定のパッケージが端末にインストールされているか確認する。
     * 
     * @param packageName
     *            パッケージ名を含めたアクティビティ名を指定すること(ex...com.example.activity.
     *            HomeActivity)
     * 
     * @return true:起動中<br>
     *         false:未起動
     */
    public static boolean hasApplication(final String packageName) {
        try {
            PackageManager pm = sContext.getPackageManager();
            pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
            // インストールされていない場合は例外が発生
        }
        return false;
    }

    /**
     * 指定のパッケージのアプリケーションアイコンを取得する。
     * 
     * @param packageName
     *            パッケージ名を指定する。
     * 
     * @return アプリケーションがインストールされていない場合はnullを返却する。
     */
    public static Drawable getApplicationIcon(final String packageName) {
        PackageManager pm = sContext.getPackageManager();
        Drawable icon = null;
        try {
            icon = pm.getApplicationIcon(packageName);
        } catch (NameNotFoundException e) {
            // インストールされていない場合は例外が発生
        }
        return icon;
    }

    /**
     * 指定のパッケージのアプリケーション名を取得する。
     * 
     * @param packageName
     *            パッケージ名を指定する。
     * 
     * @return アプリケーションがインストールされていない場合はnullを返却する。
     */
    public static String getApplicationName(final String packageName) {
        try {
            PackageManager pm = sContext.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(applicationInfo).toString();
        } catch (NameNotFoundException e) {
            // インストールされていない場合は例外が発生
        }
        return null;
    }

    /**
     * バージョンアップデートチェック
     * <p>
     * バージョンの複数桁構成のチェックを可能とする。<br>
     * ex)1.0.0
     * </p>
     * 
     * @param serverAplVersion
     *            サーバ側のバージョン
     * @return true:バージョンアップ必要<br>
     *         false:バージョンアップ不必要
     */
    public static boolean isUpdate(final String serverAplVersion) {
        try {
            // サーバ側バージョン
            final String[] serverAplVersions = serverAplVersion.split("\\.");
            // アプリ側バージョン
            final String[] aplVersions = AplUtils.getAplVersion().split("\\.");
            final int length = aplVersions.length;
            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(serverAplVersions[i]) > Integer.parseInt(aplVersions[i])) {
                    return true;
                }
            }
        } catch (final Exception e) {
            // 握りつぶす
        }
        return false;
    }

    /**
     * 証明書ハッシュ値取得
     * 
     * @return 証明書ハッシュ値
     */
    public static String getSignatureHash() {
        final PackageManager pm = sContext.getPackageManager();
        try {
            final PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo.signatures.length == 1) {
                final Signature sig = packageInfo.signatures[0];
                final byte[] sha256 = computeSha256(sig.toByteArray());
                return byte2hex(sha256);
            }
        } catch (final NameNotFoundException e) {
            // 握りつぶす
        }
        return null;
    }

    /**
     * 証明書ハッシュ値チェック
     * 
     * @param signatureHash
     *            証明書ハッシュ値
     * @return true:正しい署名のハッシュ値<br>
     *         false:正しくない署名のハッシュ値
     */
    public static boolean isSignatureHash(final String signatureHash) {
        return StringUtils.equals(signatureHash, getSignatureHash());
    }

    /**
     * sha-256変換
     * 
     * @param data
     *            変換元
     * @return sha-256変換後データ
     */
    private static byte[] computeSha256(final byte[] data) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(data);
        } catch (final NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * byte→hex変換
     * 
     * @param data
     *            変換元
     * @return HEXデータ
     */
    private static String byte2hex(final byte[] data) {
        if (data == null) {
            return null;
        }
        final StringBuilder hexadecimal = new StringBuilder();
        for (final byte b : data) {
            hexadecimal.append(String.format("%02X", b));
        }
        return hexadecimal.toString();
    }
}
