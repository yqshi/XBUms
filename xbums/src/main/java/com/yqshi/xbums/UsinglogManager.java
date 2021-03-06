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
import android.text.TextUtils;

import com.yqshi.xbums.constants.UmsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class UsinglogManager {
    private String session_id = "";
    /**
     * startTime,统计每个页面开始时间的map集合
     */
    private final HashMap<String, Long> startTimeMap = new HashMap<>();

    public UsinglogManager(Context context) {
    }

    JSONObject prepareUsinglogJSON(Context context, String start_millis, String end_millis,
                                   String duration, String activities) throws JSONException {
        JSONObject jsonUsinglog = new JSONObject();
        try {
            ClientdataManager clientdataManager = new ClientdataManager(context);
            jsonUsinglog = clientdataManager.prepareClientdataJSON();
            if (session_id.equals("")) {
                session_id = CommonUtil.getSessionid(context);
            }
            jsonUsinglog.put("tj_plt", AppInfo.getAppKey(context));
            jsonUsinglog.put("tj_app", AppInfo.getAppName(context));

        } catch (Exception e) {
            CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "usinglog context is null");
        }
        double dura = Double.parseDouble(duration) / 1000;
        jsonUsinglog.put("tj_atype", "duration");
        jsonUsinglog.put("tj_adt", start_millis);
        jsonUsinglog.put("tj_st", start_millis);
        jsonUsinglog.put("tj_et", end_millis);
        jsonUsinglog.put("tj_dur", String.valueOf(dura));
        jsonUsinglog.put("tj_wlp", activities);
        return jsonUsinglog;
    }

    public void judgeSession(final Context context) {
        CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "Call onResume()");
        try {
            if (CommonUtil.isNewSession(context)) {
                session_id = CommonUtil.generateSession(context);
                CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "New Sessionid is " + session_id);
            }
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }

    /**
     * activity onResume
     *
     * @param context
     */
    public void onResume(final Context context) {
        judgeSession(context);
        String pageName = CommonUtil.getActivityName(context);
        if (!TextUtils.isEmpty(pageName)) {
            CommonUtil.savePageName(context, pageName);
            synchronized (this.startTimeMap) {
                this.startTimeMap.put(pageName, System.currentTimeMillis());
            }
        }

        AccessManager accessManager = new AccessManager(context);
        accessManager.postAccessData();
    }

    /**
     * fragment onResume
     *
     * @param context
     * @param pageName
     */
    public void onFragmentResume(final Context context, String pageName) {
        judgeSession(context);
        if (!TextUtils.isEmpty(pageName)) {
            CommonUtil.savePageName(context, pageName);
            synchronized (this.startTimeMap) {
                this.startTimeMap.put(pageName, System.currentTimeMillis());
            }
        }
        AccessManager accessManager = new AccessManager(context);
        accessManager.postAccessData();
    }

    public void onPause(final Context context) {
        CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "Call onPause()");

        String pageName = CommonUtil.getActivityName(context);
        CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "pageName--->" + pageName);
        Long start;
        synchronized (this.startTimeMap) {
            start = (Long) this.startTimeMap.remove(pageName);
        }
        if (start == null) {
            return;
        }

        String start_millis = CommonUtil.getFormatTime(start);

        long end = System.currentTimeMillis();
        String end_millis = CommonUtil.getFormatTime(end);

        String duration = end - start + "";
        CommonUtil.saveSessionTime(context);

        JSONObject info;
        try {
            info = prepareUsinglogJSON(context, start_millis, end_millis, duration,
                    pageName);
            CommonUtil.saveInfoToFile("activityInfo", info, context);
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }


    public void onFragmentPause(final Context context, String pageName) {
        CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "Call onPause()");

        Long start;
        synchronized (this.startTimeMap) {
            start = (Long) this.startTimeMap.remove(pageName);
        }
        if (start == null) {
            return;
        }

        String start_millis = CommonUtil.getFormatTime(start);

        long end = System.currentTimeMillis();
        String end_millis = CommonUtil.getFormatTime(end);

        String duration = end - start + "";
        CommonUtil.saveSessionTime(context);

        JSONObject info;
        try {
            info = prepareUsinglogJSON(context, start_millis, end_millis, duration,
                    pageName);
            CommonUtil.saveInfoToFile("activityInfo", info, context);
        } catch (JSONException e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }


    public void onWebPage(String pageName, final Context context) {
        SharedPrefUtil sp = new SharedPrefUtil(context);
        String lastView = sp.getValue("CurrentWenPage", "");
        if (lastView.equals("")) {
            sp.setValue("CurrentWenPage", pageName);
            sp.setValue("session_save_time", System.currentTimeMillis());
        } else {
            long start = sp.getValue("session_save_time",
                    System.currentTimeMillis());
            String start_millis = CommonUtil.getFormatTime(start);

            long end = System.currentTimeMillis();
            String end_millis = CommonUtil.getFormatTime(end);

            String duration = end - start + "";

            sp.setValue("CurrentWenPage", pageName);
            sp.setValue("session_save_time", end);

            JSONObject obj;
            try {
                obj = prepareUsinglogJSON(context, start_millis, end_millis, duration,
                        lastView);
                CommonUtil.saveInfoToFile("activityInfo", obj, context);
            } catch (JSONException e) {
                CobubLog.e(UmsConstants.LOG_TAG, e);
            }
        }
    }
}
