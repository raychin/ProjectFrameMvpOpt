package com.ray.project.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.databinding.FragmentWebViewBinding;
import com.ray.project.web.JsInteraction;
import com.ray.project.web.RayWebViewChromeClient;
import com.ray.project.web.RayWebViewClient;

/**
 * 更多功能界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class WebViewFragment extends BaseFragment<FragmentWebViewBinding, BasePresenter> {
    private WebView webView;
    private String webUrl = "file:///android_asset/web/ray-template/index.html";

    @Override
    protected FragmentWebViewBinding inflateViewBinding(LayoutInflater layoutInflater, ViewGroup container) {
        return FragmentWebViewBinding.inflate(layoutInflater, container, false);
    }

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void initView(View view) {
        webView = mBinding.webView;
//        webView = mContentView.findViewById(R.id.webView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey(WEB_VIEW_URL_KEY)) {
                webUrl = bundle.getString(WEB_VIEW_URL_KEY);
            }
        }
        webView.loadUrl(webUrl);
        initWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // 后退
            return true;
        }
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView () {
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setBackgroundResource(R.color.transparent);

        webView.addJavascriptInterface(new JsInteraction(mActivity,
                new JsInteraction.CallBack() {
                @Override public void callBack(Object o) {
                }
            }
        ), JsInteraction.JS_INTERFACE);
        webView.setWebChromeClient(new RayWebViewChromeClient(mActivity));
        webView.setWebViewClient(new RayWebViewClient(mActivity, new RayWebViewClient.OnWebViewClientListener() {
            @Override
            public void onReceivedStart(WebView view, String url, Bitmap favicon) {
                if (null != mActivity) {
                    mActivity.pageLoading();
                }
            }

            @Override
            public void onReceivedFinish(WebView webView, String url) {
                if (null != mActivity) {
                    mActivity.pageLoading();
                }
            }
        }));

        WebSettings webSettings = webView.getSettings();
        // 允许使用js
        webSettings.setJavaScriptEnabled(true);
        // 设置适应Html5,不然无法使用h5界面
        webSettings.setDomStorageEnabled(true);
        // 支持openWindow
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置允许开启多窗口
        webSettings.setSupportMultipleWindows(true);

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         * 不使用缓存，只从网络获取数据.
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 清缓存和记录，缓存引起的白屏
        webView.clearCache(true);
        webView.clearHistory();
        webView.requestFocus();

        webSettings.setDatabaseEnabled(true);
        // 缓存白屏
        String appCachePath = mActivity.getCacheDir().getAbsolutePath() +
                "/webcache";
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(appCachePath);
        webSettings.setDatabasePath(appCachePath);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDomStorageEnabled(true);

        /**
         * 静态图片资源加载加速
         * https://blog.csdn.net/square_l/article/details/17681885#:~:text=%E4%B8%80%E8%88%AC%E4%BA%BA%E5%A0%86WebView%E7%9A%84%E5%8A%A0%E9%80%9F%EF%BC%8C%E9%83%BD%E6%98%AF%E5%BB%BA%E8%AE%AE%E5%85%88%E7%94%A8webView.getSettings%20%28%29.setBlockNetworkImage%20%28true%29%3B,%E5%B0%86%E5%9B%BE%E7%89%87%E4%B8%8B%E8%BD%BD%E9%98%BB%E5%A1%9E%EF%BC%8C%E7%84%B6%E5%90%8E%E5%9C%A8%E6%B5%8F%E8%A7%88%E5%99%A8%E7%9A%84OnPageFinished%E4%BA%8B%E4%BB%B6%E4%B8%AD%E8%AE%BE%E7%BD%AEwebView.getSettings%20%28%29.setBlockNetworkImage%20%28false%29%3B%20%E9%80%9A%E8%BF%87%E5%9B%BE%E7%89%87%E7%9A%84%E5%BB%B6%E8%BF%9F%E8%BD%BD%E5%85%A5%EF%BC%8C%E8%AE%A9%E7%BD%91%E9%A1%B5%E8%83%BD%E6%9B%B4%E5%BF%AB%E5%9C%B0%E6%98%BE%E7%A4%BA%E3%80%82
         * add by ray on 2023/06/29
         */
        webSettings.setBlockNetworkImage(true);

        webView.setLayerType(View.LAYER_TYPE_NONE, null);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // 支持屏幕缩放
        //        webSettings.setSupportZoom(true);
        //        webSettings.setBuiltInZoomControls(true);

        // 不显示webview缩放按钮
        //        webSettings.setDisplayZoomControls(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 释放资源
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
}
