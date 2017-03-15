package com.qq.a03_hoolaihttptest.service.flowable;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface SubscriberOnNextListener<T> {

    void onNext(T t);
}
