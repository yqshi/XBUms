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

import com.yqshi.xbums.constants.UmsConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ErrorManager {
    private Context context;

    public ErrorManager(Context context) {
        this.context = context;
    }

    private JSONObject prepareErrorJSON(String errorinfo) {
        String activities = CommonUtil.getActivityName(context);

        String appkey = AppInfo.getAppKey(context);
        String os_version = DeviceInfo.getOsVersion();
        JSONObject errorInfo = new JSONObject();
        try {
            errorInfo.put("session_id", CommonUtil.getSessionid(context));
            errorInfo.put("stacktrace", errorinfo);
            errorInfo.put("time", DeviceInfo.getDeviceTime());
            errorInfo.put("activity", activities);
            errorInfo.put("version", AppInfo.getAppVersion(context));
            errorInfo.put("appkey", appkey);
            errorInfo.put("error_type", 0);//自定义错误
            errorInfo.put("os_version", os_version);
            errorInfo.put("deviceid", DeviceInfo.getDeviceId());
            errorInfo.put("devicename", DeviceInfo.getDeviceName());
            errorInfo.put("useridentifier",
                    CommonUtil.getUserIdentifier(context));
            errorInfo.put("lib_version", UmsConstants.LIB_VERSION);

        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, ErrorManager.class, e.toString());
            return null;
        }
        return errorInfo;
    }

    public void postErrorInfo(String error) {
        final JSONObject errorJSON;
        try {
            errorJSON = prepareErrorJSON(error);
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
            return;
        }

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("data", new JSONArray().put(errorJSON));
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }

        if (CommonUtil.getReportPolicyMode(context) == UmsAgent.SendPolicy.POST_NOW
                && CommonUtil.isNetworkAvailable(context)) {

            NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL
                    + UmsConstants.ERROR_URL), postdata.toString(), new OnDataCallBack() {
                @Override
                public void onSuccess(MyMessage response) {

                }

                @Override
                public void onFailure(String errorEntity) {
                    CobubLog.e(UmsConstants.LOG_TAG, ErrorManager.class, "Message=" + errorEntity);
                    CommonUtil.saveInfoToFile("errorInfo", errorJSON, context);
                }
            });
        } else {
            CommonUtil.saveInfoToFile("errorInfo", errorJSON, context);
        }

    }
}
