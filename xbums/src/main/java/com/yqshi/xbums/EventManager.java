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

import java.util.Iterator;

class EventManager {
    private Context context;
    private String eventid;
    private String label;
    private String acc;
    private String json = "";

    public EventManager(Context context, String eventid, String label,
                        String acc) {
        this.context = context;
        this.eventid = eventid;
        this.label = label;
        this.acc = acc;
    }

    public EventManager(Context context, String eventid, String label,
                        String acc, String json) {
        this.context = context;
        this.eventid = eventid;
        this.label = label;
        this.acc = acc;
        this.json = json;
    }

    private JSONObject prepareEventJSON() {
        ClientdataManager clientdataManager = new ClientdataManager(context);
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject = clientdataManager.prepareClientdataJSON();
            localJSONObject.put("tj_plt", AppInfo.getAppKey(context));
            localJSONObject.put("tj_atype", "event");
            localJSONObject.put("tj_app", AppInfo.getAppName(context));
            localJSONObject.put("evt_i", eventid);
            localJSONObject.put("evt_n", label);
            localJSONObject.put("evt_a", acc); //1 点击次数

            //key值可能会与上面的重复，加一个前缀V_
            if (json != null && json.length() > 0) {
                //如果不是json串，丢弃这部分内容并告警
                try {
                    JSONObject obj = new JSONObject(json);
                    Iterator<String> iterator = obj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        localJSONObject.put("V_" + key, obj.get(key));
                    }
                } catch (JSONException e) {
                    CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, e.toString());
                }
            }
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, e.toString());
        }
        return localJSONObject;
    }

    public void postEventInfo() {
        final JSONObject localJSONObject;
        try {
            localJSONObject = prepareEventJSON();
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, e.toString());
            return;
        }

        String postdata = new JSONArray().put(localJSONObject).toString();

        if (CommonUtil.getReportPolicyMode(context) == UmsAgent.SendPolicy.POST_NOW
                && CommonUtil.isNetworkAvailable(context)) {
            NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL), postdata, new OnDataCallBack() {
                @Override
                public void onSuccess(MyMessage response) {

                }

                @Override
                public void onFailure(String errorEntity) {
                    CobubLog.e(UmsConstants.LOG_TAG, EventManager.class, "Message="
                            + errorEntity);
                    CommonUtil
                            .saveInfoToFile("eventInfo", localJSONObject, context);
                }
            });
        } else {
            CommonUtil.saveInfoToFile("eventInfo", localJSONObject, context);
        }
    }
}
