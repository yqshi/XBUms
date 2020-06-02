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

class UploadActivityLog extends Thread {
    public Context context;

    public UploadActivityLog(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run() {
        String cachfileactivity = context.getCacheDir().getAbsolutePath()
                + "/cobub.cache" + "activityInfo";
        postactivityinfo(cachfileactivity);
    }

    private void postactivityinfo(String cachfileactivity) {
        // 首先判断是否能发送，如果不能发送就没必要读文件了
        if (!CommonUtil.isNetworkAvailable(context)) {
            return;
        }
        // 可以发送，读文件
        String data = CommonUtil.readDataFromFile(cachfileactivity);

        try {
            String[] dataarr = data.split(UmsConstants.fileSep);
            final JSONArray jsonarr = new JSONArray();
            for (int i = 1; i < dataarr.length; i++) {
                try {
                    JSONObject obj = new JSONObject(dataarr[i])
                            .getJSONObject("activityInfo");
                    int jsonarrlength = jsonarr.length();
                    jsonarr.put(jsonarrlength, obj);
                } catch (Exception e) {
                    CobubLog.e(UmsConstants.LOG_TAG, UploadActivityLog.class, "Message=" + e.getMessage());
                    continue;
                }
            }
            if (jsonarr.length() == 0) {
                return;
            }
            String postdata = jsonarr.toString();

            // 发送之前再度判断
            if (CommonUtil.isNetworkAvailable(context)) {
                CobubLog.i(UmsConstants.LOG_TAG, UploadActivityLog.class, "post activity info");
                NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL), postdata, new OnDataCallBack() {
                    @Override
                    public void onSuccess(MyMessage response) {

                    }

                    @Override
                    public void onFailure(String errorEntity) {
                        CobubLog.e(UmsConstants.LOG_TAG, UploadActivityLog.class, errorEntity);
                        for (int i = 0; i < jsonarr.length(); i++) {
                            try {
                                CommonUtil.saveInfoToFile("activityInfo",
                                        jsonarr.getJSONObject(i), context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


            } else {
                for (int i = 0; i < jsonarr.length(); i++) {
                    CommonUtil.saveInfoToFile("activityInfo",
                            jsonarr.getJSONObject(i), context);
                }

            }

        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }

    }
}
