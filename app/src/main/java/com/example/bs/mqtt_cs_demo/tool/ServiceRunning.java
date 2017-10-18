package com.example.bs.mqtt_cs_demo.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by bs on 2017/8/30.
 */

public class ServiceRunning {
    static String TAG = "ServiceRunning";
    public static boolean isServiceRunning(Context context,String className){
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(30);
        if (! (serviceInfos.size() > 0)){
            return false;
        }
        for (int i = 0; i<serviceInfos.size(); i++){
            String mName = serviceInfos.get(i).service.getClassName().toString();
            Log.e(TAG, "服务器名称：" +mName);
            if (mName.equals(className)){
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
