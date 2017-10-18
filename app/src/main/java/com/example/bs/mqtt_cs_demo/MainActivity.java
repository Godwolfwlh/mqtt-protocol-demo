package com.example.bs.mqtt_cs_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bs.mqtt_cs_demo.common.BaseActivity;
import com.example.bs.mqtt_cs_demo.entity.UserEntity;
import com.example.bs.mqtt_cs_demo.service.MQTT_Service;
import com.example.bs.mqtt_cs_demo.service.NetData;
import com.example.bs.mqtt_cs_demo.service.Updatedata;
import com.example.bs.mqtt_cs_demo.tool.ServiceRunning;


public class MainActivity extends BaseActivity implements View.OnClickListener, Updatedata {

    private String username = "W";
    private String password = "123456";
    private String TAG = "Aty_Login";

    private Button tv_login_login;


    ProgressDialog dialog;
    LocalBroadcastManager localBroadcastManager;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent unsubscribeintent = new Intent("unsubscribe_LOCAL_BROADCAST");
            Log.e(TAG,"handler");
            switch (msg.what) {
                case 0:
                    Toast.makeText(MainActivity.this, "没有此用户", Toast.LENGTH_SHORT).show();
                    unsubscribeintent.putExtra("unsubscribeTopic", "MqttServicerDGS/login/" + username + "_r");
                    localBroadcastManager.sendBroadcast(unsubscribeintent);
                    break;
                case 3:
//                    Toast.makeText(Aty_Login.this, "密码错误", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    unsubscribeintent.putExtra("unsubscribeTopic", "MqttServicerDGS/login/" + username + "_r");
                    localBroadcastManager.sendBroadcast(unsubscribeintent);
                    break;
                case 4:
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "成功获取数据", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, InDataActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent unsubscribeintent = new Intent("unsubscribe_LOCAL_BROADCAST");
        unsubscribeintent.putExtra("unsubscribeTopic", "MqttServicerDGS/login/" + username + "_r");
        unsubscribeintent.putExtra("unsubscribeTopic", "MqttServicerDGS/get_UserInfo/" + username);
        localBroadcastManager.sendBroadcast(unsubscribeintent);
        localBroadcastManager = null;
        stopService(new Intent(this, MQTT_Service.class));

        SharedPreferences preferences = getSharedPreferences("user", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String name = "0";
        editor.putString("username", name);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume");
        NetData netData = NetData.getInstence();
        netData.update(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ServiceRunning.isServiceRunning(this, "Ser_MQTTService")) {
            Log.e(TAG, "onCreate: 没有启动服务，开始启动服务");
            startService(new Intent(this, MQTT_Service.class));
        }
        initid();
        init();

    }

    private void initid() {
        tv_login_login = (Button) findViewById(R.id.get_in_data);
    }


    private void init() {
        tv_login_login.setOnClickListener(this);

        //得到本地广播管理器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        //设置网络数据监听
        NetData netData = NetData.getInstence();
        netData.update(this);

        SharedPreferences preferences = getSharedPreferences("user", this.MODE_PRIVATE);
        String name = preferences.getString("username", "0");
        String age = preferences.getString("password", "0");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_in_data:
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(this, "上面的信息不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "向服务器发出请求，订阅");
                    Intent subscribeToTopicintent = new Intent("subscribeToTopic_LOCAL_BROADCAST");
                    subscribeToTopicintent.putExtra("topic", "MqttServicerDGS/login/" + username + "_r");
                    localBroadcastManager.sendBroadcast(subscribeToTopicintent);
                    Log.e(TAG, "向服务器发出请求，发送消息");
                    Intent pulishintent = new Intent("pulish_LOCAL_BROADCAST");
                    pulishintent.putExtra("publishTopic", "MqttServicerDGS/login/");
                    pulishintent.putExtra("publishMessage", username);
                    localBroadcastManager.sendBroadcast(pulishintent);

                    dialog = ProgressDialog.show(this, "获取数据", "正在获取数据，请稍等...", false, true);
                    break;
                }
        }
    }

    @Override
    public void UpdataText(String topic, String data) {
        if (topic.startsWith("MqttServicerDGS/login/")) {
            Log.e(TAG, "topic:" + topic + "登录信息" + data);
            if (data.equals("1")) {
                Intent pulishintent = new Intent("pulish_LOCAL_BROADCAST");
                pulishintent.putExtra("publishTopic", "MqttServicerDGS/login/" + username + "_w");
                pulishintent.putExtra("publishMessage", username + "&" + password);
                localBroadcastManager.sendBroadcast(pulishintent);

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
            if (data.equals("0")) {
                dialog.dismiss();
                Log.e(TAG, "用户不存在" + data);
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
            if (data.equals("3")) {
                dialog.dismiss();
                Log.e(TAG, "密码错误" + data);
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
            if (data.equals("4")) {

                Log.e(TAG, "登录成功" + data);

                Intent unsubscribeintent = new Intent("unsubscribe_LOCAL_BROADCAST");
                unsubscribeintent.putExtra("unsubscribeTopic", "MqttServicerDGS/login/" + username + "_r");
                localBroadcastManager.sendBroadcast(unsubscribeintent);

                //去请求用户的信息
                Intent subscribeToTopicintent = new Intent("subscribeToTopic_LOCAL_BROADCAST");
                subscribeToTopicintent.putExtra("topic", "MqttServicerDGS/get_UserInfo/" + username);
                localBroadcastManager.sendBroadcast(subscribeToTopicintent);
                Log.e(TAG, "向服务器发出请求，发送消息");
                Intent pulishintent = new Intent("pulish_LOCAL_BROADCAST");
                pulishintent.putExtra("publishTopic", "MqttServicerDGS/get_UserInfo/");
                pulishintent.putExtra("publishMessage", username);
                localBroadcastManager.sendBroadcast(pulishintent);
            }
        }

        if (topic.startsWith("MqttServicerDGS/get_UserInfo/")) {

            String[] infoArray = data.split("&");
            //写入用户信息单例
            UserEntity userinfo = UserEntity.getInstence();
            userinfo.id = infoArray[0];
            userinfo.username = infoArray[1];
            userinfo.password = infoArray[2];
            userinfo.phone = infoArray[3];
            userinfo.icon = infoArray[4];
            userinfo.nickname = infoArray[5];
            userinfo.sex = infoArray[6];
            userinfo.age = infoArray[7];
            userinfo.profession = infoArray[8];
            userinfo.company = infoArray[9];
            userinfo.address = infoArray[10];
            userinfo.email = infoArray[11];
//                Log.e(TAG, "得到用户信息:" + userinfo.umodle +"得到用户信息:" + userinfo.utimemodle + "得到用户信息:" +userinfo.ulocmodel);


            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);

        }
    }
}