package com.qq.a03_hoolaihttptest;

import android.content.Context;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ProgressSubscriber<T> implements Subscriber<T> {

    private Context mContext;
    private SubscriberOnNextListener<T> mSubscriberOnNextListener;

    public ProgressSubscriber(Context context, SubscriberOnNextListener<T> subscriberOnNextListener) {
        this.mContext = context;
        this.mSubscriberOnNextListener = subscriberOnNextListener;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(mContext, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        Toast.makeText(mContext, "onComplete", Toast.LENGTH_SHORT).show();
    }
}
