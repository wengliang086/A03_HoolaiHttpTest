package com.qq.a03_hoolaihttptest.service.flowable;

import android.content.Context;
import android.widget.Toast;

import com.qq.a03_hoolaihttptest.service.ProgressCancelListener;
import com.qq.a03_hoolaihttptest.service.ProgressDialogHandler;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ProgressSubscriber<T> implements Subscriber<T>, ProgressCancelListener {

    private Context mContext;
    private SubscriberOnNextListener<T> mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(Context context, SubscriberOnNextListener<T> subscriberOnNextListener) {
        this.mContext = context;
        this.mSubscriberOnNextListener = subscriberOnNextListener;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onSubscribe(Subscription s) {
        showProgressDialog();
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        dismissProgressDialog();
        Toast.makeText(mContext, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Toast.makeText(mContext, "onComplete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelProgress() {
        /**
         * RxJava1 这么写
         */
//        if (!this.isUnsubscribed()) {
//            this.unsubscribe();
//        }
        //Todo TxJava2 返回值必须是Observable才可以取消
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
