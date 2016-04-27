package com.qq.a03_hoolaihttptest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface HoolaiService {

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "os: android"
    })
    @GET("login/trialLogin.hl")
    Call<BaseResponse> trialLogin(@Query("channelId") String channelId, @Query("productId") int productId, @Header("udid") String udid);
}
