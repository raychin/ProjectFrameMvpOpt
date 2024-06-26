package com.ray.project.config;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.ray.project.BuildConfig;
import com.ray.project.commons.Logger;
import com.ray.project.network.Net;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 应用配置
 * @author ray
 * @date 2018/06/22
 */
public class ProjectApplication extends Application {
    private static ProjectApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        String buglyId = getPlaceHolderValue("BUGLY_APPID");
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        strategy.setDeviceID("userdefinedId");
        CrashReport.initCrashReport(getApplicationContext(), buglyId, BuildConfig.releasePackage, strategy);

        AppConfig.getAppConfig(this);
        Net.init(this);
        MMKVManager.getInstance();
        handleSSLHandshake();
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUE_ID);
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUE_ID, uniqueID);
        }
        return uniqueID;
    }
    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public boolean isAppSound() {
        return true;
    }

    /**
     * 是否启动检查更新
     * @return
     */
    public boolean isCheckUp() {
        return true;
    }

    public boolean isPlayAnim() {
        return false;
    }

    public static ProjectApplication get() {
        return sInstance;
    }

    /**
     * 利用反射获取状态栏高度
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 24;
//        // 获取状态栏高度的资源id
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(object);
            result = getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T getPlaceHolderValue(String key) {
        T value = (T) "";
        try {
            // Application中
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            // // Activity中
            // ActivityInfo appInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            // services receiver只是改个名字类似
            value = (T) appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return value;
    }
}
