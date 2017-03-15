package com.qq.a03_hoolaihttptest.service.observable;

import android.content.Context;
import android.widget.Toast;

import com.qq.a03_hoolaihttptest.service.ProgressCancelListener;
import com.qq.a03_hoolaihttptest.service.ProgressDialogHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ProgressObservable<T> implements Observer<T>, ProgressCancelListener {

    private Context mContext;
    private ObservableOnNextListener<T> mObservableOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable disposable;

    public ProgressObservable(Context context, ObservableOnNextListener<T> observableOnNextListener) {
        this.mContext = context;
        this.mObservableOnNextListener = observableOnNextListener;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        mObservableOnNextListener.onNext(t);
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
        //TxJava2
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
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
