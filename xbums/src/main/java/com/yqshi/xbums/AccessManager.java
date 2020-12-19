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

import java.lang.ref.WeakReference;


class AccessManager {
    private WeakReference<Context> contextWR;

    public AccessManager(Context context) {
        contextWR = new WeakReference<>(context);
        context = null;
    }

    public JSONObject prepareAccessDataJSON() throws JSONException {
        ClientdataManager clientdataManager = new ClientdataManager(contextWR.get());
        JSONObject jsonAccessdata = new JSONObject();
        try {
            SharedPrefUtil sp = new SharedPrefUtil(contextWR.get());
            String pageName = sp.getValue("CurrentPage", CommonUtil.getActivityName(contextWR.get()));

            jsonAccessdata = clientdataManager.prepareClientdataJSON();
            jsonAccessdata.put("tj_plt", AppInfo.getAppKey(contextWR.get()));
            jsonAccessdata.put("tj_atype", "access");
            jsonAccessdata.put("tj_app", AppInfo.getAppName(contextWR.get()));
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

        if (contextWR.get() == null) {
            return;
        }

        if (CommonUtil.isNetworkAvailable(contextWR.get())) {
            NetworkUtil.Post(CommonUtil.assemParamsUrl(contextWR.get(), UmsConstants.BASE_URL), postdata, null);
        } else {
            CommonUtil.saveInfoToFile("accessData", clientData, contextWR.get());
        }
    }
}
