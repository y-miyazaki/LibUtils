package com.miya38.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.miya38.exception.AccountNotFoundException;

/**
 * コンテキストから情報を取得するヘルパーを提供します。
 * 
 * @author y-miyazaki
 * @version 1.0
 */
public final class ContextHelper {
    /** {@link TelephonyContext} */
    private static TelephonyContext sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private ContextHelper() {
    }

    /**
     * ヘルパーを初期化します。
     * 
     * @param context
     *            コンテキスト。
     */
    public static void configure(final Context context) {
        sContext = new TelephonyContext(context);
    }

    /**
     * 各コンテキストを明示的に初期化します。
     * 
     * @param telephonyContext
     */
    public static void configure(final TelephonyContext telephonyContext) {
        sContext = telephonyContext;
    }

    /**
     * Android ID を取得します。
     * 
     * @return Android ID。
     */
    public static String getAndroidId() {
        return sContext.getAndroidId();
    }

    /**
     * ICCID を取得します。
     * 
     * @return ICCID。
     */
    public static String getICCID() {
        return sContext.getICCID();
    }

    /**
     * デバイスモデル名を取得します。
     * 
     * @return デバイスモデル名
     */
    public static String getModelName() {
        return sContext.getModelName();
    }

    /**
     * IMEI を取得します。
     * 
     * @return IMEI
     */
    public static String getIMEI() {
        return sContext.getIMEI();
    }

    /**
     * IMSI を取得します。
     * 
     * @return IMSI
     */
    public static String getIMSI() {
        return sContext.getIMSI();
    }

    /**
     * メインのGoogleAccountを取得します。
     * 
     * @return GoogleAccount。
     * @throws AccountNotFoundException
     *             アカウントが登録されていない場合にスローします。
     */
    public static Account getGoogleAccount() throws AccountNotFoundException {
        final Account account = sContext.getGoogleAccount();
        if (null == account) {
            throw new AccountNotFoundException("google account not found.");
        }
        return account;
    }

    /**
     * Google Account から認証トークンを取得します。
     * 
     * @param service
     * @return トークン
     * @throws AccountNotFoundException
     */
    public static String getGoogleAuthToken(final String service) throws AccountNotFoundException {
        return sContext.getGoogleAuthToken(service);
    }

    /**
     * 移動機の情報を保持するコンテキスト。
     */
    public static class TelephonyContext {
        /** Context */
        private final Context mContext;
        /** {@link TelephonyManager} */
        private final TelephonyManager mTelephonyManager;

        /**
         * コンストラクタ
         * 
         * @param context
         *            Context
         */
        public TelephonyContext(final Context context) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            this.mContext = context;
        }

        /**
         * Googleアカウント取得
         * 
         * @return Account
         */
        public Account getGoogleAccount() {
            final AccountManager am = AccountManager.get(mContext);
            final Account[] accounts = am.getAccountsByType("com.google");
            if (null == accounts || 0 == accounts.length) {
                throw new AccountNotFoundException("google account not found.");
            }
            return accounts[0];
        }

        /**
         * Google認証トークン取得
         * 
         * @param service
         *            サービス
         * @return 認証トークン
         */
        @SuppressWarnings("deprecation")
        public String getGoogleAuthToken(final String service) {
            final AccountManager am = AccountManager.get(mContext);
            final Account[] accounts = am.getAccountsByType("com.google");
            if (null == accounts || 0 == accounts.length) {
                throw new AccountNotFoundException("google account not found.");
            }
            final AccountManagerFuture<Bundle> result = am.getAuthToken(accounts[0], service, false, null, null);
            try {
                return result.getResult().getString(AccountManager.KEY_AUTHTOKEN);
            } catch (final Exception e) {
                throw new AccountNotFoundException("get auth token failed.", e);
            }
        }

        /**
         * AndroidID取得
         * 
         * @return AndroidID
         */
        public String getAndroidId() {
            return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        /**
         * ICCID取得
         * 
         * @return ICCID
         */
        public String getICCID() {
            return mTelephonyManager.getSimSerialNumber();
        }

        /**
         * IMEI取得
         * 
         * @return IMEI
         */
        public String getIMEI() {
            return mTelephonyManager.getDeviceId();
        }

        /**
         * IMSI取得
         * 
         * @return IMSI
         */
        public String getIMSI() {
            return mTelephonyManager.getSubscriberId();
        }

        /**
         * モデルネーム取得
         * 
         * @return モデルネーム
         */
        public String getModelName() {
            return Build.MODEL;
        }
    }
}
