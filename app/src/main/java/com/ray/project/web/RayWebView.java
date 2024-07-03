package com.ray.project.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Map;

/**
 * @Description: 类说明
 * @Author: ray
 * @Date: 2/7/2024
 */
public class RayWebView extends WebView {
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        @SuppressWarnings("unused")
        // Must match Javascript interface method of RayWebViewChromeClient
        public void notifyVideoEnd() {
            // This code is not executed in the UI thread, so we must force that to happen
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (videoEnabledWebChromeClient != null) {
                        videoEnabledWebChromeClient.onHideCustomView();
                    }
                }
            });
        }

        @android.webkit.JavascriptInterface
        @SuppressWarnings("unused")
        // Must match Javascript interface method of RayWebViewChromeClient
        public void notifyVideoStart() {
            // This code is not executed in the UI thread, so we must force that to happen
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    loadUrl("javascript:(" +
                            "function() { " +
                            " var videos = document.getElementsByTagName('video'); " +
                            " var video = videos[0]; " +
                            " if (!document.webkitFullScreen && video.webkitEnterFullscreen) {" +
                            " video.webkitEnterFullscreen(); " +
                            " } " +
                            " })()");
                }
            });
        }
    }

    private RayWebViewChromeClient videoEnabledWebChromeClient;
    private boolean addedJavascriptInterface;

    @SuppressWarnings("unused")
    public RayWebView(Context context) {
        super(context);
        addedJavascriptInterface = false;
    }

    @SuppressWarnings("unused")
    public RayWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addedJavascriptInterface = false;
    }

    @SuppressWarnings("unused")
    public RayWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addedJavascriptInterface = false;
    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     *
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    @SuppressWarnings("unused")
    public boolean isVideoFullscreen() {
        return videoEnabledWebChromeClient != null && videoEnabledWebChromeClient.isVideoFullscreen();
    }

    /**
     * Pass only a RayWebViewChromeClient instance.
     */
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void setWebChromeClient(WebChromeClient client) {
        getSettings().setJavaScriptEnabled(true);
        if (client instanceof RayWebViewChromeClient) {
            this.videoEnabledWebChromeClient = (RayWebViewChromeClient) client;
        }
        super.setWebChromeClient(client);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        addJavascriptInterface();
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        addJavascriptInterface();
        super.loadUrl(url, additionalHttpHeaders);
    }

    @SuppressLint("AddJavascriptInterface")
    private void addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            // Add javascript interface to be called when the video ends (must be done before page load)
            // Must match Javascript interface name of RayWebViewChromeClient
            addJavascriptInterface(new JavascriptInterface(), "_RagWebView");
            addedJavascriptInterface = true;
        }
    }
}
