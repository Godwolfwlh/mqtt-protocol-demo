package com.example.bs.mqtt_cs_demo.common;

import android.app.Activity;
import android.os.Bundle;

import com.example.bs.mqtt_cs_demo.service.Updatedata;

/**
 * Created by bs on 2017/8/30.
 */

public class BaseActivity extends Activity implements Updatedata {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void UpdataText(String topic, String data) {

    }
}
