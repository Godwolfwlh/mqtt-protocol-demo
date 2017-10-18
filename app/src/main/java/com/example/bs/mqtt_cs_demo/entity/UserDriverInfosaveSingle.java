package com.example.bs.mqtt_cs_demo.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 储存用户文件夹和用户所拥有的设备信息的单例类
 * Created by blitz on 2017/6/27.
 */

public class UserDriverInfosaveSingle {

    // 设为单例模式
    private UserDriverInfosaveSingle() {
    }

    public static UserDriverInfosaveSingle getInstence() {
        return UserDriverInfosaveSingle.UserSingleton.sInstence;
    }

    /**
     * 静态内部类
     */
    private static class UserSingleton {
        private static final UserDriverInfosaveSingle sInstence = new UserDriverInfosaveSingle();
    }

    public List<String> folderinfo = new ArrayList();
    public List<String> deviceinfo = new ArrayList();

    @Override
    public String toString() {
        return "UserDriverInfosaveSingle{" +
                "folderinfo=" + folderinfo +
                ", deviceinfo=" + deviceinfo +
                '}';
    }
}
