package com.example.bs.mqtt_cs_demo.service;

import android.content.Context;
import android.util.Log;

/**
 * Created by bs on 2017/8/28.
 * 用于广播数据，改变状态
 */

public class NetHint {

    private String TAG = "NetHint";
    public Context context;

    //设为单列模式
    private NetHint(){}

    public static NetHint getInstence(){return NetHintSingleton.sInstence;}

    /**
     * 静态内部类
     * **/
    private static class NetHintSingleton{
        private static final NetHint sInstence = new NetHint();
    }

    /**
     * 连接信息状态
     *
     * @param context 上下文
     * @param code    连接状态
     * @param notes   连接状态的详情信息
     * **/
    public void connectcondition(Context context, String code, String notes){
        Log.e(TAG, "连接状态:"+ code + "连接状态详情信息：" + notes);
    }

    /**
     * 重连信息状态
     *
     * @param context 上下文
     * @param code    重连状态
     * @param notes   重连状态详情信息
     * **/
    public void Reconnectcondition(Context context, String code, String notes){
        Log.e(TAG, "重连状态：" + code + "重连状态详情信息："+ notes);
    }

    /**
     * 订阅主题状态
     *
     * @param context 上下文
     * @param topic   订阅主题
     * @param notes   连接状态详情信息
     * **/
    public void subscribeToTopiccondition(Context context, String topic, String notes){
        Log.e(TAG, "订阅主题："+ topic + "连接状态详情信息："+ notes);
    }
}
