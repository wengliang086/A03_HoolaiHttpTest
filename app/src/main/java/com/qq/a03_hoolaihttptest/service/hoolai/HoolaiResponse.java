package com.qq.a03_hoolaihttptest.service.hoolai;

import android.os.Parcel;

/**
 * Created by Administrator on 2017/3/14.
 */

public class HoolaiResponse<T> extends BaseResponse {

    private T value;

    protected HoolaiResponse(Parcel in) {
        super(in);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
