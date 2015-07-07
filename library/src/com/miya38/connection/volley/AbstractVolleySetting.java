package com.miya38.connection.volley;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.AndroidRuntimeException;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.miya38.utils.AplUtils;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
     * リクエストヘッダに追加で乗せたいものがある場合は、このメソッドの引数headersのaddしたものリターンすること。
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
    protected AbstractVolleySetting(final ImageCache imageCache) {
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

    /**
     * SSL証明書の検証スルー用のSSLSocketFactoryを返却する
     * 
     * @return {@link SSLSocketFactory}
     */
    private static SSLSocketFactory getAllAllowsSocketFactory() {
        try {
            // ホスト名検証をスキップする
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname, final SSLSession session) {
                    return true;
                }
            });

            // 証明書検証スキップする空の TrustManager
            final TrustManager[] manager = { new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                    // do nothing
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                    // do nothing
                }
            } };
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, manager, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            return sslContext.getSocketFactory();
        } catch (final NoSuchAlgorithmException e) {
            throw new AndroidRuntimeException(e);
        } catch (final KeyManagementException e) {
            throw new AndroidRuntimeException(e);
        }
    }
}
