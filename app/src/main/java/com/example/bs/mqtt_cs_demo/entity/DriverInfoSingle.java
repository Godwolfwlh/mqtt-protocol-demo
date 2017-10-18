package com.example.bs.mqtt_cs_demo.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 储存设备信息的单例类
 * Created by Administrator on 2017/6/27.
 */

public class DriverInfoSingle {

    // 设为单例模式
    private DriverInfoSingle() {
    }

    public static DriverInfoSingle getInstence() {
        return DriverInfoSingle.UserSingleton.sInstence;
    }

    /**
     * 静态内部类
     */
    private static class UserSingleton {
        private static final DriverInfoSingle sInstence = new DriverInfoSingle();
    }

    public List<String> deviceinfo = new ArrayList();

    public String driverid="null";

    public String driveronline="null";

    public String drivername="null";

    public String DGS_ID ="null";

    public String JSON="null";

    /**
     * 设备回路开关,boolean型
     */
    public String out1="0";
    public String out2="0";
    public String out3="0";
    public String out4="0";
    public String out5="0";
    public String out6="0";
    public String out7="0";
    public String out8="0";

    public String time_open_1="0";
    public String time_open_2="0";
    public String time_open_3="0";
    public String time_open_4="0";
    public String time_open_5="0";

    public String time_close_1="0";
    public String time_close_2="0";
    public String time_close_3="0";
    public String time_close_4="0";
    public String time_close_5="0";

    public String time_choose="0";

    public String time_1_week="0";
    public String time_2_week="0";
    public String time_3_week="0";
    public String time_4_week="0";
    public String time_5_week="0";

    public String time_1_line="0";
    public String time_2_line="0";
    public String time_3_line="0";
    public String time_4_line="0";
    public String time_5_line="0";

    public String sundown="0";
    public String sunrise="0";

    public String longitude="0";
    public String latitude="0";

    public String control_mode="0";

    public String before_time="0";
    public String after_time="0";

    public String M8_number="0";
    public String M8_status="0";
}
