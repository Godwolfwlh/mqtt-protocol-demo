package com.example.bs.mqtt_cs_demo.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bs.mqtt_cs_demo.service.MQTT_Service;

/**
 * 订阅主题广播
 * Created by bs on 2017/8/29.
 */

public class SubscribeReceiver extends BroadcastReceiver {

    private MQTT_Service mqtt_service;

    public SubscribeReceiver(MQTT_Service mqtt_service){this.mqtt_service = mqtt_service;}

    @Override
    public void onReceive(Context context, Intent intent) {
        String topic = intent.getStringExtra("topic");
        if (intent.getStringExtra("qos") == null) {
            mqtt_service.subscribeToTopic(topic);
        } else {
            String qos = intent.getStringExtra("qos");
            mqtt_service.subscribeToTopic(topic, qos);
        }
    }
}
