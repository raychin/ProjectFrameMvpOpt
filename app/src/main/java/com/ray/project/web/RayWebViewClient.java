package com.ray.project.web;

import static com.ray.project.commons.AppTools.hasInstalled;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ray.project.base.BaseActivity;
import com.ray.project.commons.Logger;
import com.ray.project.commons.ToastUtils;

/**
 * description ：  WebView client
 *
 * @author : ray
 * @date : 17/6/2024
 */
public class RayWebViewClient extends WebViewClient {
    private BaseActivity mActivity;
    private OnWebViewClientListener mListener;

    public RayWebViewClient(BaseActivity activity) {
        mActivity = activity;
    }

    public RayWebViewClient(BaseActivity activity, OnWebViewClientListener listener) {
        mActivity = activity;
        mListener = listener;
    }

    @Override public void onPageFinished(WebView webView, String url) {
        // 页面加载完成
        if (null != mListener) {
            mListener.onReceivedFinish(webView, url);
        }
        webView.getSettings().setBlockNetworkImage(false);
        //////创建一个json对象
        //JsonObject jsonContainer = new JsonObject();
        ////为当前的json对象添加键值对
        //
        //LocationUploader.LocInfo lastKnownLocInfo
        //        = LocationUploader.getInstance().getLastKnownLocInfo();
        //jsonContainer.addProperty("lon", lastKnownLocInfo.lontitude);
        //jsonContainer.addProperty("lat", lastKnownLocInfo.latitude);
        //jsonContainer.addProperty("address", lastKnownLocInfo.addressInfo);
        //jsonContainer.addProperty("lon", "113.8922898");
        //jsonContainer.addProperty("lat", "22.7645511");
        //jsonContainer.addProperty("address", "广东省深圳市光明区马田街道石家社区工业总公司上石家工业区SZ1号");
        //
        //String lonAndLat = jsonContainer.toString();
        //String lonAndLatS="javascript:getLonAndLat('" + lonAndLat + "')";
        //
        //webView.loadUrl(lonAndLatS);

        String js = "javascript:";
        js += "var videos = document.getElementsByTagName('video');";
        js += "var video_last;";
        js += "var video = videos[videos.length-1];";
        js += "if (video != undefined && video != video_last) {";
        {
            js += "video_last = video;";
            js += "function video_start() {";
            {
                js += "_RayWebView.notifyVideoStart();";
            }
            js += "}";
            js += "video.addEventListener('play', video_start);";
        }
        js += "}";
        Logger.e("ray js = ", js);
        webView.loadUrl(js);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // 页面开始加载
        if (null != mListener) {
            mListener.onReceivedStart(view, url, favicon);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // 忽略SSL证书错误，继续加载页面
        handler.proceed();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        // 重写此方法表明点击网页里面的链接还是在当前的WebView里跳转，不另跳浏览器
        // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
        Logger.e("www url = ", url);
//        if (url.startsWith("http://") || url.startsWith("https://")) {
//            webView.loadUrl(url);
//            return true;
//        }
        if (url.startsWith("mailto:")) {
            // Handle mail Urls
            mActivity.startActivity((new Intent(Intent.ACTION_SENDTO, Uri.parse(url))));
            return true;
        }
        else if (url.startsWith("tel:")) {
            // Handle telephony Urls
            // Intent.ACTION_CALL直接拨打，Intent.ACTION_DIAL调用拨号界面
            mActivity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
            return true;
        } else if (url.startsWith("bdapp:") || url.startsWith("amapuri:")) {
            // 检测是否安装相关app
            String[] apps = {"com.autonavi.minimap", "com.baidu.BaiduMap"};
            String[] tips = {"请先安装高德地图再打开", "请先安装百度地图再打开"};
            int navPos = 0;
            if (url.startsWith("bdapp:")) {
                navPos = 1;
            }
            if (!hasInstalled(mActivity, apps[navPos])) {
                ToastUtils.showToast(mActivity, tips[navPos], 1000);
                return true;
            }
            // 百度和高德导航跳转功能 add by ray on 2023/07/13
            Intent intentNavigation = new Intent();
            intentNavigation.setAction(Intent.ACTION_VIEW);
            intentNavigation.addCategory(Intent.CATEGORY_DEFAULT);
            intentNavigation.setData(Uri.parse(url));
            mActivity.startActivity(intentNavigation);
            return true;
        } else if (url.startsWith("baiduboxapp:") ) {
            return false;
        }
        else {
            webView.loadUrl(url);
        }
        return super.shouldOverrideUrlLoading(webView, url);
    }

    public interface OnWebViewClientListener {
        void onReceivedStart(WebView view, String url, Bitmap favicon);
        void onReceivedFinish(WebView webView, String url);
    }
}
