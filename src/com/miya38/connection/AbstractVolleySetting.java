package com.miya38.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.miya38.utils.AplUtils;

/**
 * Volleyのシングルトンクラス
 * 
 * @author y-miyazaki
 */
public abstract class AbstractVolleySetting {
    /** Volley用のリクエストキュー */
    public RequestQueue mRequestQueue;
    /** ImageLoader */
    public ImageLoader mImageLoader;
    /** Context */
    public static Context sContext;

    /**
     * リクエストヘッダ設定
     * <p>
     * リクエストヘッダに追加で乗せたいものがある場合は、このメソッドの引数{@link headers}のaddしたものリターンすること。
     * </p>
     * 
     * @param headers
     *            リクエストヘッダ
     * @return 追加されたリクエストヘッダ
     */
    public abstract Map<String, String> putHeaders(Map<String, String> headers);

    /**
     * UserAgent設定
     * <p>
     * リクエストのUser-Agentは、このメソッドを通じて設定すること。
     * </p>
     * 
     * @return User-Agent
     */
    public abstract String getUserAgent();

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
     * コンストラクタを隠蔽し、インスタンス化を禁止します。
     * 
     * @param imageCache
     *            イメージキャッシュ指定(BitmapLruCach/BitmapDiskLruCache)
     */
    public AbstractVolleySetting(final ImageCache imageCache) {
        // ---------------------------------------------------------------
        // Volleyのqueue設定を行う
        // ---------------------------------------------------------------
        if (mRequestQueue == null) {
            if (AplUtils.hasGingerbread()) {
                final CustomHurlStack stack = new CustomHurlStack() {
                    @Override
                    protected HttpURLConnection createConnection(final URL url) throws IOException {
                        final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        // ユーザーエージェントを設定します
                        httpURLConnection.setRequestProperty("User-Agent", getUserAgent());
                        return httpURLConnection;
                    }

                    @Override
                    public HttpResponse performRequest(final Request<?> request, final Map<String, String> headers) throws IOException, AuthFailureError {
                        putHeaders(headers);
                        return super.performRequest(request, headers);
                    }
                };
                mRequestQueue = Volley.newRequestQueue(sContext, stack);
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                final HttpClientStack stack = new HttpClientStack(AndroidHttpClient.newInstance(getUserAgent())) {
                    @Override
                    public HttpResponse performRequest(final Request<?> request, final Map<String, String> headers)
                            throws IOException, AuthFailureError {
                        // gzip対応
                        putHeaders(headers);
                        return super.performRequest(request, headers);
                    }
                };
                mRequestQueue = Volley.newRequestQueue(sContext, stack);
            }
            mImageLoader = new ImageLoader(mRequestQueue, imageCache);
        }
    }

    /**
     * Context取得
     * 
     * @return {@link Context}
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * RequestQueue取得
     * 
     * @return {@link RequestQueue}
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * ImageLoader取得
     * 
     * @return {@link ImageLoader}
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
