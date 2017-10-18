package com.example.bs.mqtt_cs_demo.entity;


/**
 * 储存用户信息的单例类
 * Created by blitz on 2017/6/27.
 */

public class PathSingle {

    // 设为单例模式
    private PathSingle() {
    }

    public static PathSingle getInstence() {
        return PathSingle.PathSingleton.sInstence;
    }

    /**
     * 静态内部类
     */
    private static class PathSingleton {
        private static final PathSingle sInstence = new PathSingle();
    }

    public String nowpath = "";

    public String moveid;
}
