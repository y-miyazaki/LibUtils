package com.miya38.utils;

import java.util.Arrays;

/**
 * iBeaconユーティリティ
 * <p>
 * アップル社で発売されているiBeacon用の受信用のユーティリティクラスである。
 * </p>
 * 
 * @author y-miyazaki
 */
public final class IBeaconUtils {
    // ----------------------------------------------------
    // 4段階距離を測る際の距離define
    // ----------------------------------------------------
    /** 不明の場合 */
    private static final double DISTANCE_THRESHOLD_WTF = 0.0;
    /** 0.5m以下(immediate)の場合 */
    private static final double DISTANCE_THRESHOLD_IMMEDIATE = 0.5;
    /** 3m以下(near)の場合 */
    private static final double DISTANCE_THRESHOLD_NEAR = 3.0;

    // ----------------------------------------------------
    // iBeacon用データdefine
    // ----------------------------------------------------
    /** iBeaconデータ長 */
    public static final int IBEACON_DATA_LENGTH = 30;
    /** iBeacon会社コード(1) */
    public static final byte IBEACON_COMPANY_CODE_APPLE_00 = 0x4c;
    /** iBeacon会社コード(2) */
    public static final byte IBEACON_COMPANY_CODE_APPLE_01 = 0x00;
    /** iBeaconデータタイプ(iBeacon) */
    public static final byte IBEACON_TYPE_IBEACON = 0x02;

    /**
     * IBeacon距離を4段階で取得するためのENUM
     * 
     * @author y-miyazaki
     */
    public enum IBeaconDistance {
        /** 密着レベル(0.5m未満) */
        IMMEDIATE,
        /** 近いレベル(3.0m未満) */
        NEAR,
        /** 遠いレベル(3.0m以上) */
        FAR,
        /** 不明 */
        UNKNOWN,
    }

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private IBeaconUtils() {
    }

    /**
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @return
     */
    public static String getAdvertisment(final byte[] scanRecord) {
        return intToHex2(scanRecord[5] & 0xff) + intToHex2(scanRecord[6] & 0xff);
    }

    /**
     * iBeacon用UUIDを取得する
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @return UUID
     */
    public static String getUUID(final byte[] scanRecord) {
        final StringBuffer sb = new StringBuffer();
        final byte[] uUID = Arrays.copyOfRange(scanRecord, 9, 25);
        final int length = uUID.length;
        for (int i = 0; i < length; i++) {
            if (i == 4) {
                sb.append('-');
            }
            if (i == 6) {
                sb.append('-');
            }
            if (i == 8) {
                sb.append('-');
            }
            if (i == 10) {
                sb.append('-');
            }
            sb.append(intToHex2(uUID[i] & 0xff));
        }
        return sb.toString();
    }

    /**
     * iBeacon用Majorを取得する
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @return Major
     */
    public static String getMajor(final byte[] scanRecord) {
        return intToHex2(scanRecord[25] & 0xff) + intToHex2(scanRecord[26] & 0xff);
    }

    /**
     * iBeacon用Minorを取得する
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @return Minor
     */
    public static String getMinor(final byte[] scanRecord) {
        return intToHex2(scanRecord[27] & 0xff) + intToHex2(scanRecord[28] & 0xff);
    }

    /**
     * iBeacon用データであるかチェックをする
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @param UUID
     *            UUID
     * @return true: UUIDが一致する。<br>
     *         false: UUIDが一致しない、もしくはiBeacon用データではない。<br>
     */
    public static boolean equalUUID(final byte[] scanRecord, final String uUID) {
        if (isIBeacon(scanRecord) && !StringUtils.isEmpty(uUID)) {
            return StringUtils.equals(getUUID(scanRecord), uUID.toUpperCase());
        }
        return false;
    }

    /**
     * iBeacon用データであるかチェックをする
     * 
     * @param scanRecord
     *            The content of the advertisement record offered by the remote
     *            device.
     * @return true: iBeaconデータである<br>
     *         false:iBeaconデータではない
     */
    public static boolean isIBeacon(final byte[] scanRecord) {
        if (scanRecord.length > IBEACON_DATA_LENGTH) {
            if ((scanRecord[5] == IBEACON_COMPANY_CODE_APPLE_00) && (scanRecord[6] == IBEACON_COMPANY_CODE_APPLE_01) &&
                    (scanRecord[7] == IBEACON_TYPE_IBEACON) && (scanRecord[8] == (byte) 0x15)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the accuracy of an RSSI reading.
     * 
     * The code was taken from {@link http
     * ://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing}
     * 
     * @param txPower
     *            the calibrated TX power of an iBeacon
     * @param rssi
     *            the RSSI value of the iBeacon
     * @return
     */
    public static double getAccuracy(final int txPower, final double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        final double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        }
        else {
            final double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    /**
     * 距離を4段階で取得する。
     * 
     * @param txPower
     *            the calibrated TX power of an iBeacon
     * @param rssi
     *            the RSSI value of the iBeacon
     * @return {@link IBeaconDistance}
     */
    public static IBeaconDistance getDistance(final int txPower, final double rssi) {
        return getDistance(getAccuracy(txPower, rssi));
    }

    /**
     * 距離を4段階で取得する。
     * 
     * @param accuracy
     *            距離(m)
     * @return {@link IBeaconDistance}
     */
    public static IBeaconDistance getDistance(final double accuracy) {
        if (accuracy < DISTANCE_THRESHOLD_WTF) {
            return IBeaconDistance.UNKNOWN;
        } else if (accuracy < DISTANCE_THRESHOLD_IMMEDIATE) {
            return IBeaconDistance.IMMEDIATE;
        } else if (accuracy < DISTANCE_THRESHOLD_NEAR) {
            return IBeaconDistance.NEAR;
        }
        return IBeaconDistance.FAR;
    }

    /**
     * intデータを 2桁16進数に変換するメソッド
     * 
     * @param i
     *            intデータ
     * @return 16進数
     */
    private static String intToHex2(final int i) {
        final char hex_2[] = { Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16) };
        return new String(hex_2).toUpperCase();
    }
}
