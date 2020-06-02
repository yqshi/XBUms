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

import android.util.Log;

import com.yqshi.xbums.constants.UmsConstants;


/**
 * @author Cobub Logger class is responsible for Log records.
 */
class CobubLog {

    public static void v(String tag, Class<?> classobj, String msg) {

        if (!UmsConstants.DebugEnabled)
            return;

        if (UmsConstants.DebugLevel == UmsAgent.LogLevel.Debug
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Info
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Warn
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Error)
            return;

        Log.v(tag, classobj.getCanonicalName() + ": " + msg);
    }

    public static void d(String tag, Class<?> classobj, String msg) {

        if (!UmsConstants.DebugEnabled)
            return;

        if (UmsConstants.DebugLevel == UmsAgent.LogLevel.Info
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Warn
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Error)
            return;

        Log.d(tag, classobj.getCanonicalName() + ": " + msg);
    }

    public static void i(String tag, Class<?> classobj, String msg) {

        if (!UmsConstants.DebugEnabled)
            return;

        if (UmsConstants.DebugLevel == UmsAgent.LogLevel.Warn
                || UmsConstants.DebugLevel == UmsAgent.LogLevel.Error)
            return;

        Log.i(tag, classobj.getCanonicalName() + ": " + msg);
    }

    public static void w(String tag, Class<?> classobj, String msg) {

        if (!UmsConstants.DebugEnabled)
            return;

        if (UmsConstants.DebugLevel == UmsAgent.LogLevel.Error)
            return;

        Log.w(tag, classobj.getCanonicalName() + ": " + msg);
    }

    public static void e(String tag, Class<?> classobj, String msg) {
        if (!UmsConstants.DebugEnabled)
            return;
        Log.e(tag, classobj.getCanonicalName() + ": " + msg);
    }

    public static void e(String tag, Exception e) {
        if (!UmsConstants.DebugEnabled)
            return;
        Log.e(tag, e.toString());
        e.printStackTrace();
    }
}
