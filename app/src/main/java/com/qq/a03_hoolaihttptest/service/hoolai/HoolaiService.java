package com.qq.a03_hoolaihttptest.service.hoolai;

import com.qq.a03_hoolaihttptest.module.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface HoolaiService {

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "os: android"
    })
    @GET("login/trialLogin.hl")
    Call<HoolaiResponse<User>> trialLogin(@Query("channelId") String channelId, @Query("productId") int productId, @Header("udid") String udid);

    /**
     * 这里不知道是不是Bug，@Header("udid") 使用RxJava的方式时取不到值
     * @param channelId
     * @param productId
     * @param udid @Header("udid") 使用RxJava的方式时取不到值
     * @return
     */
    @GET("login/trialLogin.hl")
    Observable<HoolaiResponse<User>> rxJavaLogin(@Query("channelId") String channelId, @Query("productId") int productId, @Query("udid") String udid);

}
