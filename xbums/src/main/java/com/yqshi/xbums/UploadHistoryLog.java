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
import org.json.JSONObject;

class UploadHistoryLog extends Thread {
    public Context context;

    public UploadHistoryLog(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run() {
        String baseDir = context.getCacheDir().getAbsolutePath()
                + "/cobub.cache";
        String cachfileclientdata = baseDir + "clientData";
        String cachfileerror = baseDir + "errorInfo";
        String cachfileeventInfo = baseDir + "eventInfo";
        String cachfiletags = baseDir + "tags";

        //postdata(cachfiletags, "tags", UmsConstants.TAG_URL);
        //postdata(cachfileclientdata, "clientData", UmsConstants.CLIENTDATA_URL);
        //postdata(cachfileerror, "errorInfo", UmsConstants.ERROR_URL);
        postdata(cachfileeventInfo, "eventInfo", UmsConstants.EVENT_URL);
    }

    private void postdata(String cachfile, final String type, String url) {
        //首先判断是否能发送，如果不能发送就没必要读文件了
        if (!CommonUtil.isNetworkAvailable(context)) {
            return;
        }
        CobubLog.i(UmsConstants.LOG_TAG, UploadHistoryLog.class, " upload type=" + type);
        //可以发送，读文件
        JSONObject postdata = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            arr = CommonUtil.getJSONdata(cachfile, type);
            if (arr.length() == 0) {
                return;
            }
            postdata.put("data", arr);
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
        if (postdata != null) {
            //增强判断，以免网络突然中断
            if (CommonUtil.isNetworkAvailable(context)) {
                final JSONArray finalArr = arr;
                NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL
                        + url), postdata.toString(), new OnDataCallBack() {
                    @Override
                    public void onSuccess(MyMessage response) {

                    }

                    @Override
                    public void onFailure(String errorEntity) {
                        CobubLog.e(UmsConstants.LOG_TAG, UploadHistoryLog.class, " Message=" + errorEntity);
                        CommonUtil.saveInfoToFile(type, finalArr, context);
                    }
                });

            } else {
                CommonUtil.saveInfoToFile(type, arr, context);
            }
        }
    }

}
