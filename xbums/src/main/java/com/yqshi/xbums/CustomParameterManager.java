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

public class CustomParameterManager {
    Context context;

    public CustomParameterManager(Context context) {
        super();
        this.context = context;
    }

    public void getParameters() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("appKey", AppInfo.getAppKey(context));
            if (CommonUtil.isNetworkAvailable(context)
                    && CommonUtil.isNetworkTypeWifi(context)) {
                NetworkUtil.Post(CommonUtil.assemParamsUrl(context, UmsConstants.BASE_URL
                        + UmsConstants.PARAMETER_URL), obj.toString(), new OnDataCallBack() {
                    @Override
                    public void onSuccess(MyMessage message) {
                        try {
                            CobubLog.d(UmsConstants.LOG_TAG, CustomParameterManager.class, message.getMsg());
                            JSONObject result_obj = new JSONObject(message.getMsg())
                                    .getJSONObject("reply");
                            if (result_obj.has("parameters")) {
                                JSONArray arr = result_obj.getJSONArray("parameters");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject item = arr.getJSONObject(i);
                                    SharedPrefUtil spu = new SharedPrefUtil(context);
                                    spu.setValue(item.getString("key"),
                                            item.getString("value"));
                                }
                            }
                        } catch (JSONException e1) {
                            CobubLog.e(UmsConstants.LOG_TAG, e1);
                        }
                    }

                    @Override
                    public void onFailure(String errorEntity) {

                    }
                });

            }

        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }

}
