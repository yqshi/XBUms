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


class AccessManager {
    private Context context;

    public AccessManager(Context context) {
        this.context = context;
    }

    public JSONObject prepareAccessDataJSON() throws JSONException {
        ClientdataManager clientdataManager = new ClientdataManager(context);
        JSONObject jsonAccessdata = new JSONObject();
        try {
            SharedPrefUtil sp = new SharedPrefUtil(context);
            String pageName = sp.getValue("CurrentPage", CommonUtil.getActivityName(context));

            jsonAccessdata = clientdataManager.prepareClientdataJSON();
            jsonAccessdata.put("tj_plt", AppInfo.getAppKey(context));
            jsonAccessdata.put("tj_atype", "access");
            jsonAccessdata.put("tj_app", AppInfo.getAppName(context));
            jsonAccessdata.put("tj_adt", CommonUtil.getFormatTime(System.currentTimeMillis()));
            jsonAccessdata.put("tj_wlp", pageName);
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, AccessManager.class, e.toString());
        }


        return jsonAccessdata;
    }

    public void postAccessData() {
        final JSONObject clientData;
        try {
            clientData = prepareAccessDataJSON();
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
            return;
        }
        String postdata = new JSONArray().put(clientData).toString();

        if (CommonUtil.isNetworkAvailable(context)) {
            NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL), postdata, new OnDataCallBack() {
                @Override
                public void onSuccess(MyMessage response) {

                }

                @Override
                public void onFailure(String errorEntity) {
                    CobubLog.e(UmsConstants.LOG_TAG, AccessManager.class, "Error Code=" + errorEntity);
                    CommonUtil.saveInfoToFile("accessData", clientData, context);
                }
            });
        } else {
            CommonUtil.saveInfoToFile("accessData", clientData, context);
        }
    }
}
