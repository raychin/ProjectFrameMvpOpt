package com.ray.project.web;

import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ray.project.base.BaseActivity;

/**
 * description ： WebView chrome client
 * @author : ray
 * @date : 17/6/2024
 */
public class RayWebViewChromeClient extends WebChromeClient {
    private BaseActivity mActivity;
    private CordovaDialogsHelper dialogsHelper;

    private OnWebViewChromeClientListener mListener;

    public RayWebViewChromeClient(BaseActivity activity) {
        mActivity = activity;
        dialogsHelper = new CordovaDialogsHelper(mActivity);

    }

    public RayWebViewChromeClient(BaseActivity activity, OnWebViewChromeClientListener listener) {
        mActivity = activity;
        dialogsHelper = new CordovaDialogsHelper(mActivity);
        mListener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        // 页面加载进度
        if (null != mListener) {
            mListener.onReceivedProgress(view, newProgress);
        }
    }

    /**
     * Tell the client to display a javascript alert dialog.
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        dialogsHelper.showAlert(message, new CordovaDialogsHelper.Result() {
            @Override
            public void gotResult(boolean success, String value) {
                if (success) {
                    result.confirm();
                } else {
                    result.cancel();
                }
            }
        });
        return true;
    }


    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        return true;
    }
    /**
     * Tell the client to display a confirm dialog to the user.
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        dialogsHelper.showConfirm(message, new CordovaDialogsHelper.Result() {
            @Override
            public void gotResult(boolean success, String value) {
                if (success) {
                    result.confirm();
                } else {
                    result.cancel();
                }
            }
        });
        return true;
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
        WebCameraHelper.getInstance().showOptions(mActivity);
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
        WebCameraHelper.getInstance().showOptions(mActivity);
    }

    // For Android > 5.0支持多张上传
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> uploadMsg,
                                     FileChooserParams fileChooserParams) {
        WebCameraHelper.getInstance().mUploadCallbackAboveL = uploadMsg;
        WebCameraHelper.getInstance().showOptions(mActivity);
        return true;
    }

    public interface OnWebViewChromeClientListener {
        void onReceivedProgress(WebView view, int newProgress);
    }
}
