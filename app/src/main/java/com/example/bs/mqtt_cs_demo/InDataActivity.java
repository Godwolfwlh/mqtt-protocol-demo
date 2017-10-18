package com.example.bs.mqtt_cs_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.example.bs.mqtt_cs_demo.common.BaseActivity;
import com.example.bs.mqtt_cs_demo.common.UpdateLocation;
import com.example.bs.mqtt_cs_demo.entity.UserDriverInfosaveSingle;
import com.example.bs.mqtt_cs_demo.entity.UserEntity;
import com.example.bs.mqtt_cs_demo.service.NetData;

/**
 * Created by bs on 2017/8/31.
 */

public class InDataActivity extends BaseActivity {

    String TAG = "InDataActivity";

    private TextView showDataText;

    UpdateLocation updateLocation;

    UserEntity userEntity = UserEntity.getInstence();

    UserDriverInfosaveSingle userDriverInfosaveSingle = UserDriverInfosaveSingle.getInstence();

    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getdata_activity);

        NetData netData = NetData.getInstence();
        netData.update(this);

        initid();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetData netData = NetData.getInstence();
        netData.update(this);
    }

    private void initid() {
        showDataText = (TextView) findViewById(R.id.get_in_data_text);
    }

    private void init() {
        showDataText.setText(userEntity.toString() + "\n" +userDriverInfosaveSingle.toString());
    }

    @Override
    public void UpdataText(String topic, String data) {
        boolean start = true;
        userDriverInfosaveSingle = UserDriverInfosaveSingle.getInstence();
        if (topic.startsWith("MqttServicerDGS/get_AllUserDrivers/")) {
            Log.e(TAG, "清空缓存" + userDriverInfosaveSingle.folderinfo.size());
            if (userDriverInfosaveSingle.folderinfo.size() != 0 && start) {
                Log.e(TAG, "清空缓存");
                userDriverInfosaveSingle.deviceinfo.clear();
                userDriverInfosaveSingle.folderinfo.clear();
                start = false;
            }

//            Log.e(TAG, "得到设备信息topic"+topic+"得到设备信息data"+data);
            if (data.equals("end")) {
                //取消当前设备的监听
                Intent unsubscribeintent = new Intent("unsubscribe_LOCAL_BROADCAST");
                unsubscribeintent.putExtra("unsubscribeTopic", topic);
                localBroadcastManager.sendBroadcast(unsubscribeintent);

                String userid = userEntity.id;
                Intent subscribeintent2 = new Intent("subscribeToTopic_LOCAL_BROADCAST");
                Intent pulishintent2 = new Intent("pulish_LOCAL_BROADCAST");
                subscribeintent2.putExtra("topic", "MqttServicerDGS/get_AllFolder/" + userid);
                localBroadcastManager.sendBroadcast(subscribeintent2);

                pulishintent2.putExtra("publishTopic", "MqttServicerDGS/get_AllFolder/");
                pulishintent2.putExtra("publishMessage", userid);
                localBroadcastManager.sendBroadcast(pulishintent2);
            } else {
                userDriverInfosaveSingle.deviceinfo.add(data);
            }
        }

        if (topic.startsWith("MqttServicerDGS/get_AllFolder/")) {
//            Log.e(TAG, "得到设备信息topic"+topic+"得到设备信息data"+data);
            if (data.equals("end")) {
                //取消当前设备的监听
                Intent unsubscribeintent = new Intent("unsubscribe_LOCAL_BROADCAST");
                unsubscribeintent.putExtra("unsubscribeTopic", topic);
                localBroadcastManager.sendBroadcast(unsubscribeintent);
                start = true;
                Log.e(TAG, "信息接收完成");
                dataArrived();
            } else {
                userDriverInfosaveSingle.folderinfo.add(data);
            }
        }
    }

    public void setupdateLocationListener(UpdateLocation updateLocation) {
        this.updateLocation = updateLocation;
    }

    private void dataArrived() {
        updateLocation.updatebyLocation();
    }

}
