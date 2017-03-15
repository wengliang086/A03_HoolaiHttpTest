package com.qq.a03_hoolaihttptest.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.qq.a03_hoolaihttptest.service.ProgressCancelListener;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog progressDialog;
    private Context mContext;
    private ProgressCancelListener cancelListener;
    private boolean cancelable;

    public ProgressDialogHandler(Context context, ProgressCancelListener cancelListener, boolean cancelable) {
        super();
        this.mContext = context;
        this.cancelListener = cancelListener;
        this.cancelable = cancelable;
    }

    private void initProcessDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("网络请求中。。。");
            progressDialog.setTitle("Title");
            progressDialog.setCancelable(cancelable);
            if (cancelable) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelListener.onCancelProgress();
                    }
                });
            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProcessDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
