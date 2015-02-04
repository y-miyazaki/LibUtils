package com.miya38.common;

import java.io.IOException;
import java.util.Date;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.miya38.connection.NetworkRequest;

/**
 * 共通インタフェース
 *
 * @author y-miyazaki
 *
 */
public class CommonInterface {
    /**
     * GetLoaderインタフェース
     *
     * @author y-miyazaki
     */
    public interface OnGetLoaderFinishListerner {
        /**
         * <b>GET用ロードフィニッシュイベントメソッド</b>
         * <p>
         * このイベントメソッドは、getリクエストをコールした際に、非同期でコールバックイベントしてコールされる。 <br>
         * 200以外の場合はAbstractConnectionFragment /AbstractConnectionActivityで自動的にエラーダイアログを表示する。<br>
         * ※本メソッド内ではView側の処理を記載しないこと。あくまで、ステータスコード判定・データのパースをメインとするメソッドとする。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         * @throws JsonMappingException
         * @throws JsonParseException
         * @throws IOException
         */
        void onGetLoaderFinished(NetworkRequest networkRequest, NetworkResponse networkResponse, String data) throws JsonMappingException, JsonParseException, IOException;

        /**
         * 各種通信処理完了後にコールされるメソッド
         * <p>
         * onGetLoaderFinishedコール後に本メソッドがコールされる。<br>
         * ※本メソッド内ではView側の処理を記載すること。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         */
        void onGetLoadView(NetworkRequest networkRequest, NetworkResponse networkResponse, String data);
    }

    /**
     * PostLoaderインタフェース
     *
     * @author y-miyazaki
     *
     */
    public interface OnPostLoaderFinishListerner {
        /**
         * <b>POST用ロードフィニッシュイベントメソッド</b>
         * <p>
         * このイベントメソッドは、postリクエストをコールした際に、非同期でコールバックイベントしてコールされる。 <br>
         * 200以外の場合はAbstractConnectionFragment /AbstractConnectionActivityで自動的にエラーダイアログを表示する。<br>
         * ※本メソッド内ではView側の処理を記載しないこと。あくまで、ステータスコード判定・データのパースをメインとするメソッドとする。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         * @throws JsonMappingException
         * @throws JsonParseException
         * @throws IOException
         */
        void onPostLoaderFinished(NetworkRequest networkRequest, NetworkResponse networkResponse, String data) throws JsonMappingException, JsonParseException, IOException;

        /**
         * 各種通信処理完了後にコールされるメソッド
         * <p>
         * onPostLoaderFinishedコール後に本メソッドがコールされる。<br>
         * ※本メソッド内ではView側の処理を記載すること。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         */
        void onPostLoadView(NetworkRequest networkRequest, NetworkResponse networkResponse, String data);
    }

    /**
     * PutLoaderインタフェース
     *
     * @author y-miyazaki
     */
    public interface OnPutLoaderFinishListerner {
        /**
         * <b>PUT用ロードフィニッシュイベントメソッド</b>
         * <p>
         * このイベントメソッドは、putリクエストをコールした際に、非同期でコールバックイベントしてコールされる。 <br>
         * 200以外の場合はAbstractConnectionFragment /AbstractConnectionActivityで自動的にエラーダイアログを表示する。<br>
         * ※本メソッド内ではView側の処理を記載しないこと。あくまで、ステータスコード判定・データのパースをメインとするメソッドとする。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         * @throws JsonMappingException
         * @throws JsonParseException
         * @throws IOException
         */
        void onPutLoaderFinished(NetworkRequest networkRequest, NetworkResponse networkResponse, String data) throws JsonMappingException, JsonParseException, IOException;

        /**
         * 各種通信処理完了後にコールされるメソッド
         * <p>
         * onPutLoaderFinishedコール後に本メソッドがコールされる。<br>
         * ※本メソッド内ではView側の処理を記載すること。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         */
        void onPutLoadView(NetworkRequest networkRequest, NetworkResponse networkResponse, String data);
    }

    /**
     * DeleteLoaderインタフェース
     *
     * @author y-miyazaki
     */
    public interface OnDeleteLoaderFinishListerner {
        /**
         * <b>DELETE用ロードフィニッシュイベントメソッド</b>
         * <p>
         * このイベントメソッドは、deleteリクエストをコールした際に、非同期でコールバックイベントしてコールされる。 <br>
         * 200以外の場合はAbstractConnectionFragment /AbstractConnectionActivityで自動的にエラーダイアログを表示する。<br>
         * ※本メソッド内ではView側の処理を記載しないこと。あくまで、ステータスコード判定・データのパースをメインとするメソッドとする。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         * @throws JsonMappingException
         * @throws JsonParseException
         * @throws IOException
         */
        void onDeleteLoaderFinished(NetworkRequest networkRequest, NetworkResponse networkResponse, String data) throws JsonMappingException, JsonParseException, IOException;

        /**
         * 各種通信処理完了後にコールされるメソッド
         * <p>
         * onDeleteLoaderFinishedコール後に本メソッドがコールされる。<br>
         * ※本メソッド内ではView側の処理を記載すること。<br>
         * </p>
         *
         * @param networkRequest
         *            {@link NetworkRequest}
         * @param networkResponse
         *            {@link NetworkResponse}
         * @param data
         *            受信データ
         */
        void onDeleteLoadView(NetworkRequest networkRequest, NetworkResponse networkResponse, String data);
    }

    /**
     * 各種Adapter内イベントクリックリスナー
     */
    public interface OnAdapterClickListener {
        /**
         * クリック時の処理
         *
         * @param position
         *            Adapterのposition
         */
        void onClick(View view, int position);
    }

    /**
     * Gallery用ボタンクリックリスナー
     */
    public interface OnGallerywButtonClickListener {
        /**
         * クリック時の処理
         *
         * @param position
         *            Galleryのposition
         */
        void onClick(View view, int position);
    }

    /**
     * ListView用イメージクリックリスナー
     *
     * @author y-miyazaki
     */
    public interface OnListViewItemClickListener {
        /**
         * クリック時の処理
         *
         * @param position
         *            ListViewのposition
         * @param item
         *            ListView内のアイテム
         * @param view
         *            ListViewの選択されたView
         * @param intent
         *            その他引き渡したいデータ
         */
        void onClick(int position, int item, View view, Intent intent);
    }

    /**
     * FragmentTabHost⇔Fragment間コールバックリスナー
     *
     * @author y-miyazaki
     */
    public interface OnFragmentEventListener {
        /**
         * クリック時の処理
         *
         * @param args
         *            コールバック内容は自由
         */
        void onEvent(Object... args);
    }

    /**
     * タイマー用リスナー 通信後の経過時間を取得することが可能
     *
     * @author y-miyazaki
     */
    public interface OnTimerListener {
        /**
         * クリック時の処理
         *
         * @param serverTime
         *            サーバータイム
         * @param sec
         *            サーバータイムからの経過時間
         */
        void onTimer(Date serverTime, int sec);
    }

    /**
     * キーダウンリスナー
     *
     * @author y-miyazaki
     */
    public interface OnKeyDownListener {
        /**
         * キーダウンイベント
         *
         * @param keyCode
         *            キーコード
         * @param event
         *            イベント
         * @return 結果<br>
         *         true:キー処理をsuperクラスにdispatchしない<br>
         *         false:キー処理をdispatchする。<br>
         */
        boolean onKeyDown(int keyCode, KeyEvent event);
    }

    /**
     * アニメーション終了リスナー
     *
     * @author y-miyazaki
     */
    public interface OnAnimationFinishedListener {
        /**
         * アニメーション終了リスナー
         */
        void onAnimationFinished(View v);
    }

    /**
     * View確定リスナー
     *
     * @author y-miyazaki
     */
    public interface OnWindowFocusChangedListener {
        void onWindowFocusChanged(boolean hasFocus);
    }
}
