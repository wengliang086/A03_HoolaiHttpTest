package com.qq.a03_hoolaihttptest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/21.
 */
public class BaseResponse implements Parcelable {

    private String code;
    private String desc;
    private String group;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    protected BaseResponse(Parcel in) {
        this.code = in.readString();
        this.desc = in.readString();
        this.group = in.readString();
    }

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.code);
        parcel.writeString(this.desc);
        parcel.writeString(this.group);
    }
}
