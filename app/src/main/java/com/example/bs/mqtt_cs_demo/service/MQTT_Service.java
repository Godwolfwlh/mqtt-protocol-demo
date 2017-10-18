package com.example.bs.mqtt_cs_demo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.bs.mqtt_cs_demo.recriver.PulishReceiver;
import com.example.bs.mqtt_cs_demo.recriver.SubscribeReceiver;
import com.example.bs.mqtt_cs_demo.recriver.UnSubscribeReceiver;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bs on 2017/8/28.
 */

public class MQTT_Service extends Service {

    //设置Tag
    public static final String TAG = MQTT_Service.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private NetData netData = NetData.getInstence();
    private NetHint netHint = NetHint.getInstence();

    PulishReceiver pulishReceiver;
    SubscribeReceiver subscribeReceiver;
    UnSubscribeReceiver unSubscribeReceiver;
    LocalBroadcastManager localBroadcastManager;

//    private String host = "tcp://10.0.2.2:61613";
//    private String host = "tcp://192.168.1.103:61613";
    private String host = "tcp://119.23.133.169:1883";
//    private String userName = "admin";
//    private String passWord = "password";
    private String clientId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        //开始连接
        if (!client.isConnected()) {
            startReconnect();
            Log.e(TAG, "没有连接");
            //广播接收器

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        localBroadcastManager.unregisterReceiver(pulishReceiver);
        localBroadcastManager.unregisterReceiver(subscribeReceiver);
        localBroadcastManager.unregisterReceiver(unSubscribeReceiver);
//        disconnect();
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        pulishReceiver = new PulishReceiver(this);
        subscribeReceiver = new SubscribeReceiver(this);
        unSubscribeReceiver = new UnSubscribeReceiver(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter subscribe_Filter = new IntentFilter();
        subscribe_Filter.addAction("subscribeToTopic_LOCAL_BROADCAST");
        IntentFilter unSubscribe_Filter = new IntentFilter();
        unSubscribe_Filter.addAction("unsubscribe_LOCAL_BROADCAST");
        IntentFilter pulish_Filter = new IntentFilter();
        pulish_Filter.addAction("pulish_LOCAL_BROADCAST");

        localBroadcastManager.registerReceiver(subscribeReceiver, subscribe_Filter);
        localBroadcastManager.registerReceiver(pulishReceiver, pulish_Filter);
        localBroadcastManager.registerReceiver(unSubscribeReceiver, unSubscribe_Filter);
    }

    private void init(){

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        clientId = "ID:_18084359293_Android" + time;
        //服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this,uri,clientId);
        //设置MQTT监听并且接受消息
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                //重连
                if(reconnect){
                    netHint.connectcondition(MQTT_Service.this, "重连服务器地址：" + serverURI, "重新连接");
                }else {
                    netHint.connectcondition(MQTT_Service.this, "连接服务器地址：" + serverURI, "初次连接");
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                //连接断开
                netHint.connectcondition(MQTT_Service.this, "回调The Connection was lost", "连接断开");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //接收信息
                netHint.connectcondition(MQTT_Service.this, "接收信息：" + topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //传送完成
                netHint.connectcondition(MQTT_Service.this, "回调DeliveryComplete", "传送完成");
            }
        });

        conOpt = new MqttConnectOptions();
        //自动连接
        conOpt.setAutomaticReconnect(true);
        //清除缓存
        conOpt.setCleanSession(true);
        //设置超时time，单位：s
        conOpt.setConnectionTimeout(10);
        //心跳包发送间隔，单位：s
        conOpt.setKeepAliveInterval(20);
        //用户名
//        conOpt.setUserName(userName);
        //密码
//        conOpt.setPassword(passWord.toCharArray());

    }

    /**
     * 连接MQTT服务器
     * **/
    private void startReconnect(){
        if (!client.isConnected() && isConnectIsNomarl()){
            try {
                client.connect(conOpt, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        netHint.Reconnectcondition(MQTT_Service.this,"startReconnect onSuccess","连接成功");
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        client.setBufferOpts(disconnectedBufferOptions);
                        Log.e(TAG,"connect Success:"+ host);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        String exc;
                        if (exception == null){
                            exc = "null";
                        }else {
                            exc = exception.getMessage();
                        }
                        netHint.Reconnectcondition(MQTT_Service.this,"Failed to connect to:"+ exc,"--asyncActionToken : " +asyncActionToken.toString());
                        Toast.makeText(MQTT_Service.this,"无法连接至MQTT通讯服务器！",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 订阅主题
     *
     * @param topic 主题字符串
     * **/
    public void subscribeToTopic(String topic){
        Log.e(TAG,"订阅主题" +topic);
        try {
            client.subscribe(topic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    netHint.subscribeToTopiccondition(MQTT_Service.this,asyncActionToken.toString(),"订阅成功！");
                    Log.e(TAG,"Topic Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    String exc;
                    if (exception == null){
                        exc = "null";
                    }else {
                        exc = exception.getMessage();
                    }
                    netHint.subscribeToTopiccondition(MQTT_Service.this,"订阅失败！" + asyncActionToken.getTopics(),"Failed to subscribe错误为：" + exc);
                }
            });

            //THIS DOES NOT WORK
            client.subscribe(topic, 2, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.e(TAG,"收到消息：" + new String(message.getPayload()) + "来自主题：" + topic);
                    //message Arrived
                    netData.messageArrived(MQTT_Service.this,topic,new String(message.getPayload()));
                }
            });
        } catch (MqttException e) {
            Log.e(TAG,"Exception whilst subscribing错误：" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     *
     * @param topic 主题字符串
     * @param qos   信息的质量
     * **/
    public void subscribeToTopic(String topic, String qos){
        Log.e(TAG, "订阅主题：" + topic);
        try {
            client.subscribe(topic, Integer.getInteger(qos), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    netHint.subscribeToTopiccondition(MQTT_Service.this,asyncActionToken.toString(),"订阅成功！");
                    Log.e(TAG,"Topic Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    String exc;
                    if (exception == null){
                        exc = "null";
                    }else {
                        exc = exception.getMessage();
                    }
                    netHint.subscribeToTopiccondition(MQTT_Service.this,asyncActionToken.toString(),"Failed to subscride错误为：" + exc);
                }
            });
            //THIS DOES NOT WORK
            client.subscribe(topic, Integer.getInteger(qos), new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.e(TAG,"收到消息：" + new String(message.getPayload()) + "来自主题：" + topic);
                    //message Arrived
                    netData.messageArrived(MQTT_Service.this, topic, new String(message.getPayload()));
                }
            });
        } catch (MqttException e) {
            Log.e(TAG,"Exception whilst subscribing错误：" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param publishTopic   发送的主题
     * @param publishMessage 发送的消息
     * **/
    public void publishMessage(String publishTopic, String publishMessage){
        Log.e(TAG, "发送消息主题：" + publishTopic + "发送消息内容：" + publishMessage);
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            message.setQos(2);
            message.setRetained(false);
            client.publish(publishTopic,message);
            Log.e(TAG, "Message Published");
            if (!client.isConnected()){
                Log.e(TAG,"publishMessage：没有连接" );
                Toast.makeText(this, "无法连接至通讯服务器，通讯失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error Publishing：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param publishTopic   发送的主题
     * @param publishMessage 发送的消息
     * @param qos            信息的质量
     * **/
    public void publishMessage(String publishTopic, String publishMessage, String qos){
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            message.setQos(Integer.getInteger(qos));
            message.setRetained(false);
            client.publish(publishTopic, message);
            Log.e(TAG, "Message Published");
            if (!client.isConnected()) {
                Log.e(TAG, client.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 取消订阅
     *
     * @param topic 取消订阅主题
     * **/
    public void unSubscribeMessage(String topic) {
        Log.e(TAG, "取消订阅主题：" + topic);
        try {
            client.unsubscribe(topic, getApplicationContext(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    netHint.subscribeToTopiccondition(MQTT_Service.this, asyncActionToken.toString(), "取消订阅");
                    Log.e(TAG, "Topic Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    netHint.subscribeToTopiccondition(MQTT_Service.this, "失败", "Failed to subscribe错误为" + exception.toString());
                }
            });
//            mqttAndroidClient.unsubscribe(topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否连接
     * **/
    private boolean isConnectIsNomarl(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()){
            String name = info.getTypeName();
            Log.i(TAG,"MQTT当前网络名称："+name);
            return true;
        }else {
            Log.i(TAG,"MQTT当前没有网络可用");
            return false;
        }
    }

    public void disconnect() {
        Log.e(TAG, "断开服务器连接 ");
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
