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

import org.json.JSONException;
import org.json.JSONObject;

class ConfigManager {
    private Context context;

    public ConfigManager(Context context) {
        this.context = context;
    }

    JSONObject prepareConfigJSON() throws JSONException {
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("appKey", AppInfo.getAppKey(context));

        return jsonConfig;
    }

    public void updateOnlineConfig() {
        JSONObject jsonConfig;
        try {
            jsonConfig = prepareConfigJSON();
        } catch (Exception e) {
            return;
        }

        if (CommonUtil.isNetworkAvailable(context)) {
            NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL
                    + UmsConstants.CONFIG_URL), jsonConfig.toString(), new OnDataCallBack() {
                @Override
                public void onSuccess(MyMessage response) {
                    try {
//                        JSONObject result_obj = new JSONObject(message.getMsg()).getJSONObject("reply");
//                        SharedPrefUtil prefUtil = new SharedPrefUtil(context);
//                        int isLocation = result_obj.getInt("autoGetLocation");// 是否获取location
//                        if (isLocation == 0)
//                            UmsAgent.setAutoLocation(false);
//                        else
//                            UmsAgent.setAutoLocation(true);
//                        prefUtil.setValue("locationStatus", isLocation != 0);
//
//                        int isOnlyWifi = result_obj.getInt("updateOnlyWifi");// 只在wifi状态下更新
//                        if (isOnlyWifi == 0)
//                            UmsAgent.setUpdateOnlyWifi(false);
//                        else
//                            UmsAgent.setUpdateOnlyWifi(true);
//                        prefUtil.setValue("updateOnlyWifiStatus", isOnlyWifi != 0);
//
//                        int reportPolicy = result_obj.getInt("reportPolicy");// 数据发送模式
//                        CommonUtil.saveDefaultReportPolicy(context, reportPolicy);// 保存在本地
//
//                        int session = result_obj.getInt("sessionMillis");// 获取到的单位为 秒 默认30秒
//                        UmsAgent.setSessionContinueMillis(context, session * 1000);
//
//                        // interval_time
//                        int interval_time = result_obj.getInt("intervalTime");// 获取到的数据单位为
//                        // 分钟 默认1分钟
//                        UmsAgent.setPostIntervalMillis(context, interval_time);
//
//                        // file size
//                        int filesize = result_obj.getInt("fileSize") * 1024 * 1024;// 缓存文件大小 服务端为M ，此处转为字节保存之
//                        prefUtil.setValue("file_size", filesize);

                    } catch (Exception e1) {
                        CobubLog.e(UmsConstants.LOG_TAG, e1);
                    }
                }

                @Override
                public void onFailure(String errorEntity) {

                }
            });

        }
    }
}
