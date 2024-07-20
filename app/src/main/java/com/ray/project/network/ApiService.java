package com.ray.project.network;

import com.ray.project.model.LoginModel;
import com.ray.project.ui.login.UserLogin;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @POST("/geosocial-sso-manage/user/login")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
//    rx.Observable<Result<LoginModel>> login(@Body UserLogin userLogin);
    rx.Observable<Result<LoginModel>> login(
            @Query("userid") String user,
            @Query("password") String password
    );

    @POST("https://rioweb.szns.gov.cn/zsdsjdy/zzapi/geosocial-sso-manage/user/loginNoCap")
    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
    rx.Observable<Result<LoginModel>> getToken1(
            @Query("userid") String userid,
            @Query("password") String password
    );
}
