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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yqshi.xbums.constants.UmsConstants;


class AppInfo {
    private static String versionName = "";

    static String getAppKey(Context context) {
        SharedPrefUtil sp = new SharedPrefUtil(context);
        return sp.getValue("app_key", "");
    }

    static String getAppName(Context context) {
        try {

            PackageManager packageManager = context.getPackageManager();

            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);

            int labelRes = packageInfo.applicationInfo.labelRes;

            return context.getResources().getString(labelRes);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "";
    }

    static String getAppVersion(Context context) {
        if (versionName.equals("")) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    versionName = pi.versionName;
                }
            } catch (Exception e) {
                AppInfo.class.getCanonicalName();
                CobubLog.e(UmsConstants.LOG_TAG, AppInfo.class, e.toString());
            }
        }
        return versionName;
    }

}
