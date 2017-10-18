package com.example.bs.mqtt_cs_demo.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通用储存用户所有的设备（List）的所有信息（MAP）
 * Created by Administrator on 2017/6/27.
 */

public class DriversTopicSingle {

    // 设为单例模式
    private DriversTopicSingle() {
    }

    public static DriversTopicSingle getInstence() {
        return DriversTopicSingle.UserSingleton.sInstence;
    }

    /**
     * 静态内部类
     */
    private static class UserSingleton {
        private static final DriversTopicSingle sInstence = new DriversTopicSingle();
    }

    /**
     * 所有设备信息的列表
     */
    public List<Map> driverslist = new ArrayList();


}
