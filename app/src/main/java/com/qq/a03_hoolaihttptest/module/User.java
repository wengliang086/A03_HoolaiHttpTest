package com.qq.a03_hoolaihttptest.module;

/**
 * Created by Administrator on 2017/3/14.
 */

public class User {

    private Long uid;
    private String accessToken;
    private String channel;
    private String channelUid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelUid() {
        return channelUid;
    }

    public void setChannelUid(String channelUid) {
        this.channelUid = channelUid;
    }
}
