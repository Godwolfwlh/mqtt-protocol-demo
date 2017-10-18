package com.example.bs.mqtt_cs_demo.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.bs.mqtt_cs_demo.service.MQTT_Service;

/**
 * 取消订阅广播
 * Created by bs on 2017/8/29.
 */

public class UnSubscribeReceiver extends BroadcastReceiver {

    private MQTT_Service mqtt_service;

    public UnSubscribeReceiver(MQTT_Service mqtt_service){this.mqtt_service = mqtt_service;}
    @Override
    public void onReceive(Context context, Intent intent) {
        String topic = intent.getStringExtra("unsubscribeTopic");
        Log.e("UnSubscribeReceiver","收到取消订阅的广播"+topic);
        mqtt_service.unSubscribeMessage(topic);
    }
}
