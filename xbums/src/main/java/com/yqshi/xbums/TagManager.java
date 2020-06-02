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

class TagManager {

    private Context context;
    private String tags;

    public TagManager(Context context, String tags) {
        this.context = context;
        this.tags = tags;
    }

    private JSONObject prepareTagJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("tag", tags);
            object.put("deviceid", DeviceInfo.getDeviceId());
            object.put("appkey", AppInfo.getAppKey(context));
            object.put("useridentifier", CommonUtil.getUserIdentifier(context));
            object.put("lib_version", UmsConstants.LIB_VERSION);
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
        return object;
    }

    public void PostTag() {
        final JSONObject tagJSON;
        try {
            tagJSON = prepareTagJSON();
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
            return;
        }

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("data", new JSONArray().put(tagJSON));
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }

        if (CommonUtil.getReportPolicyMode(context) == UmsAgent.SendPolicy.POST_NOW
                && CommonUtil.isNetworkAvailable(context)) {

            NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL), postdata.toString(), new OnDataCallBack() {
                @Override
                public void onSuccess(MyMessage response) {

                }

                @Override
                public void onFailure(String errorEntity) {
                    CobubLog.e(UmsConstants.LOG_TAG, TagManager.class, "Message=" + errorEntity);

                    CommonUtil.saveInfoToFile("tags", tagJSON, context);
                }
            });
        } else {
            CommonUtil.saveInfoToFile("tags", tagJSON, context);
        }
    }
}
