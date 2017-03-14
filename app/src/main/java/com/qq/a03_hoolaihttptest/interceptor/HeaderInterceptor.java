package com.qq.a03_hoolaihttptest.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/14.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newReq = request.newBuilder()
                .addHeader("HeaderInterceptor-aaabbb", "12313123")
                .addHeader("HeaderInterceptor-test2", "test2")
                .build();
        return chain.proceed(newReq);
    }
}
