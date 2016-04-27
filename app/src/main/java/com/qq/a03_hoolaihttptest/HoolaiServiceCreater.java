package com.qq.a03_hoolaihttptest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/4/21.
 */
public class HoolaiServiceCreater {

    //    private static final String BASE_URL = "http://61.148.167.74:11116/access_open_api";
    private static final String BASE_URL = "http://192.168.150.37:8080/access_open_api/";

    public static HoolaiService create() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (1 < 2) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newReq = request.newBuilder()
                            .addHeader("aaabbb", "12313123")
                            .build();
                    return chain.proceed(newReq);
                }
            }).build();
            builder.client(client);
        }

        HoolaiService service = builder.build().create(HoolaiService.class);
        return service;
    }
}
