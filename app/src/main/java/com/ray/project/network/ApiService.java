package com.ray.project.network;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 网络请求
 * @author ray
 * @date 2018/07/05
 */
public interface ApiService {
    @GET("content/text.php")
    rx.Observable<Result> text(@Query("key") String key);
}
