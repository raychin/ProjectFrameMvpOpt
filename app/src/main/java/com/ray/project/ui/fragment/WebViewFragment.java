package com.ray.project.ui.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.ray.project.R;
import com.ray.project.base.BaseFragment;
import com.ray.project.base.BasePresenter;
import com.ray.project.commons.Logger;
import com.ray.project.databinding.FragmentWebViewBinding;
import com.ray.project.web.JsInteraction;
import com.ray.project.web.RayWebView;
import com.ray.project.web.RayWebViewChromeClient;
import com.ray.project.web.RayWebViewClient;

/**
 * WebView界面fragment
 * @author ray
 * @date 2018/07/03
 */
public class WebViewFragment extends BaseFragment<FragmentWebViewBinding, BasePresenter> {
//    private WebView webView;
    private RayWebView webView;
    private String webUrl = "";

    @Override
    protected boolean isImmersiveStatusHeight() {
        return true;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_web_view;
    }

    /**
     * 加载输入框中的内容
     */
    protected void loadUrlFromEdit () {
        if (webView == null) {
            return;
        }
        String url = String.valueOf(mBinding.webViewHref.getText());
        if (!TextUtils.isEmpty((url)) && (TextUtils.isEmpty(webView.getUrl()) || !url.equals(webView.getUrl()))) {

            if (url.startsWith("http://") || url.startsWith("https://")) {
                webView.loadUrl(url);
            } else if (url.startsWith("www.") || url.startsWith("m.")) {
                webView.loadUrl("https://" + url);
            }

        }
    }
    @Override
    protected void initView(View view) {
//        webView = mBinding.webView;

        webView = new RayWebView(mActivity);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // add the webview instance to the main layout
        mBinding.mainRlNonVideo.addView(webView);

        mBinding.webViewHref.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // 处理Enter键的逻辑
                    loadUrlFromEdit();
                    return true;
                }
                return false;
            }
        });

        mBinding.ivWebViewLoad.setOnClickListener(v -> {
            loadUrlFromEdit();
        });

        mBinding.ivWebViewRefresh.setOnClickListener(v -> {
            if (webView == null) {
                return;
            }
            webView.reload();
        });
    }

    public static final String WEB_VIEW_SHOW_NAVI_KEY = "webViewNavigation";
    public static final int WEB_VIEW_SHOW_NAVI_SHOW = 1;
    public static final int WEB_VIEW_SHOW_NAVI_GONE = 0;
    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey(WEB_VIEW_URL_KEY)) {
                webUrl = bundle.getString(WEB_VIEW_URL_KEY);
            }

            if (bundle.containsKey(WEB_VIEW_SHOW_NAVI_KEY) && WEB_VIEW_SHOW_NAVI_GONE == bundle.getInt(WEB_VIEW_SHOW_NAVI_KEY)) {
                mBinding.hrefLayout.setVisibility(View.GONE);
            } else {
                mBinding.hrefLayout.setVisibility(View.VISIBLE);
            }
        }

        String[] suggestions = {
                "https://m.baidu.com",
                "https://www.sina.cn/",
                "https://qq.com",
                "https://www.examcoo.com/",
                "https://m.zhibo8.com/",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_dropdown_item_1line, suggestions);
        mBinding.webViewHref.setAdapter(adapter);

        if (!TextUtils.isEmpty(webUrl)) {
            webView.loadUrl(webUrl);
        }

        initWebView();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 这里还要判断RayWebViewChromeClient.onBackPressed()方法，退出全屏
            if (null != rayWebViewChromeClient && rayWebViewChromeClient.isVideoFullscreen()) {
                rayWebViewChromeClient.onHideCustomView();
                return true;
            }
            if (webView.canGoBack()) {
                // 后退
                webView.goBack();
                return true;
            }
        }
        return false;
    }

    RayWebViewChromeClient rayWebViewChromeClient;
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
//        webView.setWebChromeClient(new RayWebViewChromeClient(mActivity, new RayWebViewChromeClient.OnWebViewChromeClientListener() {
//            @Override
//            public void onReceivedProgress(WebView view, int newProgress) {
//                mBinding.progressWeb.setProgress(newProgress);
//            }
//        }));
        rayWebViewChromeClient = new RayWebViewChromeClient(mActivity, new RayWebViewChromeClient.OnWebViewChromeClientListener() {
            @Override
            public void onReceivedProgress(WebView view, int newProgress) {
                mBinding.progressWeb.setProgress(newProgress);
            }
        }, mBinding.mainRlNonVideo, mBinding.mainRlVideo, null, webView);
        rayWebViewChromeClient.setOnToggledFullscreen(new RayWebViewChromeClient.ToggledFullscreenCallback() {;
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                Logger.d("toggledFullscreen", String.valueOf(fullscreen));
                if (null != mActivity) {
                    if (fullscreen) {
                        // // 设置全屏
                        // mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        mActivity.setStatusView(0);
                        // 设置横屏
                        int requestedOrientation = mActivity.getRequestedOrientation();
                        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                            // 反横屏下设置跟随传感器
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        } else {
                            // 其他情况设置横屏
                            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        }
                    } else {
                        // 恢复为用户方向
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                        setStatusViewWithColor(statusColor());
                        setTitleNavigationShow(showTitleNavigation());
                        if (isImmersiveStatusHeight()) { setImmersiveStatusHeight(); }
                    }
                }
            }
        });
        webView.setWebChromeClient(rayWebViewChromeClient);

        webView.setWebViewClient(new RayWebViewClient(mActivity, new RayWebViewClient.OnWebViewClientListener() {
            @Override
            public void onReceivedStart(WebView view, String url, Bitmap favicon) {
                closeSoftInput(mBinding.webViewHref);
                hideSuggestion();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    mBinding.webViewHref.setText(url);
                }
                mBinding.progressWeb.setVisibility(View.VISIBLE);
                if (null != mActivity) {
                    mActivity.pageLoading();
//                    Loading.getInstance().show(mActivity);
                }
            }

            @Override
            public void onReceivedFinish(WebView webView, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    mBinding.webViewHref.setText(url);
                }
                mBinding.progressWeb.setVisibility(View.GONE);
                if (null != mActivity) {
                    mActivity.pageLoading();
//                    Loading.getInstance().dismiss();
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
        // 设置自适应任意大小网页
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-cosetLoadWithOverviewModentrol决定是否从网络上取数据。
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
         * webview默认不允许混合模式，https当中不能加载http资源，
         * 而开发的时候可能使用的是https的链接，但是链接中的图片可能是http的，所以需要设置开启。
         */
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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

    /**
     * 关闭软键盘
     * @param view 输入框控件
     */
    protected void closeSoftInput (View view) {
        // 获取InputMethodManager服务
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);

        // 关闭软键盘
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 输入框提示关闭
     */
    protected void hideSuggestion () {
        mBinding.webViewHref.clearFocus();
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
