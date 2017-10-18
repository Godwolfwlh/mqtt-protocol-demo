package com.example.bs.mqtt_cs_demo.service;

import android.content.Context;


/**
 * Created by Administrator on 2017/6/12.
 */

public class NetData {
    public String topic = "null";
    public String data = "null";

    // 设为单例模式
    private NetData() {}

    public static NetData getInstence() {
        return NetDateSingleton.sInstence;
    }

    Updatedata updatedata;

    public void update(Updatedata mainActivity) {
        updatedata = mainActivity;
    }

    /**
     * 静态内部类
     */
    private static class NetDateSingleton {
        private static final NetData sInstence = new NetData();
    }

    /**
     * 接收信息
     *
     * @param context 上下文
     * @param topic   订阅的主题
     * @param data    接收的信息
     */
    public void messageArrived(Context context, String topic, final String data) {
        this.topic = topic;
        this.data = data;
        updatedata.UpdataText(topic, data);
    }

}
