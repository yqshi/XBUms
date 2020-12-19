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
package com.yqshi.xbums.constants;


import com.yqshi.xbums.UmsAgent;

public class UmsConstants {
    public static String BASE_URL = "";

    public final static String CLIENTDATA_URL = "";
    public final static String ERROR_URL = "/errorlog";
    public final static String EVENT_URL = "";
    public final static String TAG_URL = "";
    public final static String USINGLOG_URL = "";
    public final static String UPDATE_URL = "/appupdate";
    public final static String CONFIG_URL = "/pushpolicyquery";
    public final static String PARAMETER_URL = "/getAllparameters";
    public final static String LOG_TAG = "UMSAgent";

    // Set the SDK Logs output. If DebugEnabled == true, the log will be
    // output depends on DebugLevel. If DebugEnabled == false, there is 
    // no any outputs.
    public static boolean DebugEnabled = false;
    // Default Log Level is Debug, no log information will be output in Logcat
    public static UmsAgent.LogLevel DebugLevel = UmsAgent.LogLevel.Debug;

    // Default settings for continue Session duration. If user quit the app and 
    // then re-entry the app in 1.5 seconds, it will be seemed as the same session.
    public static long kContinueSessionMillis = 1500L; // Default is 1.5s.

    public static boolean mProvideGPSData = false; // Default is false, not use GPS data.

    public static boolean mUpdateOnlyWifi = true; // Default is true, only wifi update

    // Report policy: 1 means sent the data to server immediately
    // 0 means the data will be cached and sent to server when next app's start up.
    public static UmsAgent.SendPolicy mReportPolicy = UmsAgent.SendPolicy.POST_NOW; //Default is 1, real-time


    public static long defaultFileSize = 1024 * 1024;//1M

    public static String fileSep = "@_@";
    //other settings
    public static String SDK_VERSION = "${sdk.version}";
    //security level:0=http;1=https;2=https+dn
    public static String SDK_SECURITY_LEVEL = "0";// "${sdk.security.level}";
    //ssl pos name
    public static String SDK_POS_NAME = "${sdk.pos.name}";
    //csr alias
    public static String SDK_CSR_ALIAS = "${sdk.csr.alias}";
    //for cpos dn check
    public static String SDK_HTTPS_DN = "${sdk.https.dn}";

    public static String LIB_VERSION = "1.0.14";


}


