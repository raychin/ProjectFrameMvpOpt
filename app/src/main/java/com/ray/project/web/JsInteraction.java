package com.ray.project.web;

import android.webkit.JavascriptInterface;

import com.ray.project.base.BaseActivity;

/**
 * description ： js原生方法调用
 * @author : ray
 * @date : 17/6/2024
 */
public class JsInteraction {
    public static final String JS_INTERFACE = "app";

    private BaseActivity mContext;
    private CallBack callBack;


    public JsInteraction(BaseActivity context, CallBack callback) {
        this.mContext = context;
        this.callBack = callback;
    }

    /**
     * 关闭界面
     * window.app.closeActivity()
     */
    @JavascriptInterface
    public void closeActivity() {
        mContext.finish();
    }

    public interface CallBack {
        void callBack(Object o);
    }
}
