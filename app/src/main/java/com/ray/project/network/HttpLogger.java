package com.ray.project.network;

import com.ray.project.commons.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网络请求打印
 * @author ray
 * @date 2018/07/09
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Logger.e("HttpLogInfo", message);
    }
}
