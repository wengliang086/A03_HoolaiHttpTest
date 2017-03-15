package com.qq.a03_hoolaihttptest.service.hoolai;

import com.qq.a03_hoolaihttptest.interceptor.HeaderInterceptor;
import com.qq.a03_hoolaihttptest.interceptor.LoggingInterceptor;
import com.qq.a03_hoolaihttptest.module.User;

import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/4/21.
 */
public class HoolaiServiceCreater {

    private static final int DEFAULT_TIMEOUT = 5;

    private static final String BASE_URL = "http://61.148.167.74:11116/access_open_api/";
//    private static final String BASE_URL = "http://192.168.150.37:8080/access_open_api/";

    public static HoolaiService create() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //如果没有这一行 会报异常Caused by: java.lang.IllegalArgumentException: Could not locate call adapter for rx.Observable
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient client = new OkHttpClient.Builder()
                //拦截器是一种强大的机制,可以监视、重写和重试调用
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())//拦截发出的请求和传入的响应的日志.
                .addNetworkInterceptor(new LoggingInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        builder.client(client);

        HoolaiService service = builder.build().create(HoolaiService.class);
        return service;
    }

    private static class HttpResultFunc<T> implements Function<HoolaiResponse<T>, T> {

        @Override
        public T apply(HoolaiResponse<T> response) throws Exception {
            if (!response.getCode().equalsIgnoreCase("success")) {
                throw new ApiException(response.getDesc());
            }
            return response.getValue();
        }
    }

    public static void rxJavaLogin(Subscriber<User> subscriber, String channelId, int productId, String udid) {
        create().rxJavaLogin(channelId, productId, udid)
                .map(new HttpResultFunc<User>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
