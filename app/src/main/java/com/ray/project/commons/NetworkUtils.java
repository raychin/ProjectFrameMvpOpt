package com.ray.project.commons;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description ： 网络检查
 * 获取网络信息需要在AndroidManifest.xml文件中加入相应的权限。
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * @Author : ray
 * @Date : 2024/06/27
 */
public class NetworkUtils {
    /**
     * 判断是否有网络连接
     * @param activity
     * @return
     */
    public boolean networkConnected(Activity activity) {
        if (activity != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * @param activity
     * @return
     */
    public static int getConnectedType(Activity activity) {
        if (activity != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 判断WIFI网络是否可用
     * @param activity
     * @return
     */
    public boolean wifiConnectedEnable(Activity activity) {
        if (activity != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @param activity
     * @return
     */
    public boolean mobileConnectedEnable(Activity activity) {
        if (activity != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static final int CM_NET = 4;
    public static final int CM_WAP = 3;
    public static final int WIFI = 2;
    public static final int NET_ENABLE = 1;
    /**
     *  1.无网络（这种状态可能是因为手机停机，网络没有开启，信号不好等原因）
     *  2.使用WIFI上网
     *  3.CMWAP（中国移动代理）
     *  4.CMNET上网
     */
    public static int getAPNType(Context context){
        int netType = NET_ENABLE;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null){
            return netType;
        }

        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            Logger.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is "+networkInfo.getExtraInfo());
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                netType = CM_NET;
            }
            else{
                netType = CM_WAP;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }
        return netType;
    }

    /*
     * @author ray
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */
    public static final boolean ping(String ip, int timeout) {
        String result = null;
        try {
            // ping 的地址，可以换成任何一种可靠的外网
//            String ip = "www.baidu.com";
            // ping网址3次
            Process p = Runtime.getRuntime().exec("ping -c 3 -w " + timeout + " " + ip);
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Logger.e("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Logger.e("----result---", "result = " + result);
        }
        return false;
    }
}
