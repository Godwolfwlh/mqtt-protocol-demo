package com.example.bs.mqtt_cs_demo.recriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bs.mqtt_cs_demo.service.MQTT_Service;

/**
 * 发送消息广播
 * Created by bs on 2017/8/29.
 */

public class PulishReceiver extends BroadcastReceiver {

    private MQTT_Service mqtt_service;

    public PulishReceiver(MQTT_Service mqtt_service){
        this.mqtt_service = mqtt_service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String topic = intent.getStringExtra("publishTopic");
        String message = intent.getStringExtra("publishMessage");

        if (intent.getStringExtra("qos") == null){
            mqtt_service.publishMessage(topic, message);
        }else {
            String qos = intent.getStringExtra("qos");
            mqtt_service.publishMessage(topic, message, qos);
        }
    }
}
