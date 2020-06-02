/**
 * LYX Statics SDK FOR Android
 * <p>
 * An open source analytics android sdk for mobile applications
 *
 * @package LYX 利伊享
 * @author yqshi
 * @copyright Copyright (c) All Rights Reserved.
 * @since Version 1.0
 */

package com.yqshi.xbums;

import android.content.Context;
import android.os.Looper;
import android.util.Log;


import com.yqshi.xbums.constants.UmsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

class MyCrashHandler implements UncaughtExceptionHandler {    
    private static MyCrashHandler myCrashHandler;
    private Context context;
    private Object stacktrace;
    private String activities;
    private String time;
    private String appkey;
    private String os_version;

    public static synchronized MyCrashHandler getInstance() {
        if (myCrashHandler != null) {
            return myCrashHandler;
        } else {
            myCrashHandler = new MyCrashHandler();
            return myCrashHandler;
        }
    }

    private MyCrashHandler() {
        super();
    }

    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable arg1) {
        Log.e("AndroidRuntime", getErrorInfo(arg1));
        new Thread() {
            @Override
            public void run() {
                super.run();

                Looper.prepare();
                String errorinfo = getErrorInfo(arg1);

                String[] ss = errorinfo.split("\n\t");
                String headstring = ss[0] + "\n\t" + ss[1] + "\n\t" + ss[2];

                if (headstring.length() > 255) {
                    headstring = headstring.substring(0, 255) + "\n\t";
                } else {
                    headstring = headstring + "\n\t";
                }

                stacktrace = headstring + errorinfo;
                activities = CommonUtil.getActivityName(context);
                time = DeviceInfo.getDeviceTime();
                appkey = AppInfo.getAppKey(context);
                os_version = DeviceInfo.getOsVersion();
                JSONObject errorInfo = getErrorInfoJSONString(context);
                CobubLog.i(UmsConstants.LOG_TAG,MyCrashHandler.class, errorinfo);
                CommonUtil.saveInfoToFile("errorInfo", errorInfo, context);

                android.os.Process.killProcess(android.os.Process.myPid());
                Looper.loop();
            }

        }.start();
    }

    private JSONObject getErrorInfoJSONString(Context context) {
        JSONObject errorInfo = new JSONObject();
        try {
            errorInfo.put("stacktrace", stacktrace);
            errorInfo.put("time", time);
            errorInfo.put("version", AppInfo.getAppVersion(context));
            errorInfo.put("activity", activities);
            errorInfo.put("devicename", DeviceInfo.getDeviceName());
            errorInfo.put("appkey", appkey);
            errorInfo.put("os_version", os_version);
            errorInfo.put("deviceid", DeviceInfo.getDeviceId());
            errorInfo.put("useridentifier", CommonUtil.getUserIdentifier(context));
            errorInfo.put("error_type", 1);//系统捕捉到的错误
            errorInfo.put("lib_version", UmsConstants.LIB_VERSION);
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
        return errorInfo;
    }

    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

}
