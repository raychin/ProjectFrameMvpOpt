package com.ray.project.network;

import android.content.Context;

import com.ray.project.R;
import com.ray.project.commons.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * retrofit网络加载
 * @author ray
 * @date 2018/07/03
 */
public class Net {
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 超时
     */
    private static final int CONNECT_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 20;
    private static final int WRITE_TIME_OUT = 20;

    public static final String BASE_API = "http://10.200.66.39:9000/";
    private static Retrofit retrofit;
    private static ApiService apiService;

    /**
     * 请求访问quest
     * response拦截器
     */
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // 获取用户token
            final String token = "";
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Logger.e("Net", "----------Request Start----------------");
            Logger.e("Net", "| " + request.toString() + request.headers().toString());
            Logger.e("Net", "| Response:" + content);
            Logger.e("Net", "----------Request End:" + duration + "毫秒----------");
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .addHeader("token", token)
                    .addHeader("platform", "1")
                    .build();
        }
    };

    public static void init(final Context context) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        // 获取用户token
//        final String token = "";
        // Install the all-trusting trust manager
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        //* 方式一：添加ssl
                        .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())/**/
                        /* 方式二：添加ssl
                        .sslSocketFactory(getSocketFactory(context))/**/
                        /* 方式一：添加请求拦截器
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request newRequest = chain.request()
                                        .newBuilder()
                                        .addHeader("token", token)
                                        .addHeader("platform", "1")
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })/**/
                        //* 方式二：添加请求拦截器并打印
                        .addInterceptor(interceptor)/**/
                        // 添加请求打印
                        .addNetworkInterceptor(logInterceptor)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static ApiService getService() {
        if (apiService == null) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";//授信证书密码（CA机构证书密码）
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    /**
     * 创建ssl证书
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     * 获取本地ssl证书
     * @param context
     * @return
     */
    public static SSLSocketFactory getSocketFactory(Context context) {
        // CA机构授信证书
        InputStream trust_input = context.getResources().openRawResource(R.raw.ad);
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, null);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                trust_input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
