package com.miya38.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

/**
 * Bluetoothユーティリティ
 *
 * @author y-miyazaki
 */
public final class BluetoothUtils {
    // ----------------------------------------------------------
    // ohter
    // ----------------------------------------------------------
    /** Context */
    private static Context sContext;

    /**
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     */
    private BluetoothUtils() {
    }

    /**
     * Bluetooth対応端末であるか？
     *
     * @return {@code true}の場合は、Bluetooth対応端末
     * {@code false}の場合は、Bluetooth非対応端末
     */
    public static boolean hasBluetooth() {
        // BluetoothAdapter取得
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    /**
     * Bluetoothが有効であるか？
     *
     * @return {@code true}の場合は、Bluetooth有効
     * {@code false}の場合は、Bluetooth無効
     */
    public static boolean isEnable() {
        // BluetoothAdapter取得
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    /**
     * Bluetooth許可リクエスト
     * INTENT起動でBluetoothの許可リクエストを出す。
     * リターンはonActivityResultで返却されるため、ハンドリングが必要な場合は必ず処理を記載すること。
     */
    public static void enableRequest(Activity activity, int requestCode) {
        Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(btOn, requestCode);
    }

    /**
     * 接続履歴のあるBluetoothデバイスをリストアップする。
     *
     * @return 接続履歴のあるBluetoothデバイスリスト
     * Bluetooth非対応、接続先デバイスが存在しない場合はSetが0で帰る。
     */
    public static Set<BluetoothDevice> getBondedDevices() {
        // BluetoothAdapter取得
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return new HashSet<BluetoothDevice>();
        }
        return bluetoothAdapter.getBondedDevices();
    }
}
