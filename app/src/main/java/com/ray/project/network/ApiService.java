package com.ray.project.network;

import com.ray.project.model.LoginModel;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 网络请求
 * @author ray
 * @date 2018/07/05
 */
public interface ApiService {
    @GET("ksj_api/getToken")
    rx.Observable<Result<LoginModel>> getToken(
            @Query("user") String user,
            @Query("secret") String secret,
            @Query("time") String time
    );
}
