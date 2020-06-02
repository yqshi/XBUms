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


class ClientdataManager {
    private Context context;
    private final String PLATFORM = "android";

    public ClientdataManager(Context context) {
        this.context = context;
        DeviceInfo.init(context);
    }

    public JSONObject prepareClientdataJSON() throws JSONException {
        JSONObject jsonClientdata = new JSONObject();
        try {
            jsonClientdata.put("tj_deviceid", DeviceInfo.getDeviceId());  //设备ID 拿得到就有否则就为uuid
            jsonClientdata.put("tj_sys", "Android " + DeviceInfo.getOsVersion()); //系统版本号 eg：9.0.0
            jsonClientdata.put("tj_plat", PLATFORM);     //客户端平台
            jsonClientdata.put("tj_nlang", DeviceInfo.getLanguage()); //当前手机设置的语言
            //jsonClientdata.put("tj_appkey", AppInfo.getAppKey(context)); //
            jsonClientdata.put("tj_resol", DeviceInfo.getResolution()); // 分辨率
            jsonClientdata.put("tj_ispc", "0"); //是否PC
            jsonClientdata.put("tj_phonetype", DeviceInfo.getPhoneType());//PHONE_TYPE_NONE //0 PHONE_TYPE_GSM //1 PHONE_TYPE_CDMA //2 PHONE_TYPE_SIP //3
            jsonClientdata.put("tj_imsi", DeviceInfo.getIMSI()); //获取SIM卡唯一标识 没有则为空
            jsonClientdata.put("tj_mccmnc", DeviceInfo.getMCCMNC()); //返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)
            jsonClientdata.put("tj_mccmnc_name", DeviceInfo.getMCCMNCName()); //返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)
           // jsonClientdata.put("tj_cellid", DeviceInfo.getCellInfoofCID()); //基站ID
            //jsonClientdata.put("tj_lac", DeviceInfo.getCellInfoofLAC()); //获取gsm网络编号


            jsonClientdata.put("tj_network", DeviceInfo.getNetworkTypeWIFI2G3G());//网络类型 eg:wifi/4g/3g
            // jsonClientdata.put("tj_time", DeviceInfo.getDeviceTime()); //格式化的时间
            jsonClientdata.put("tj_appv", AppInfo.getAppVersion(context));//app版本号
//        jsonClientdata.put("useridentifier",
//                CommonUtil.getUserIdentifier(context));
            jsonClientdata.put("tj_modulename", DeviceInfo.getDeviceProduct());//生产厂商
            jsonClientdata.put("tj_model", DeviceInfo.getDeviceName());//厂家+品牌名称 eg:HUAWEI+EDI-AL10
            jsonClientdata.put("tj_wifimac", DeviceInfo.getWifiMac()); //wifi 的mac地址
            jsonClientdata.put("tj_havebt", DeviceInfo.getBluetoothAvailable());// 蓝牙是否打开 true 打开 false 没有
            jsonClientdata.put("tj_havewifi", DeviceInfo.getWiFiAvailable());//wifi 是否链接 true 链接 false 没有
            jsonClientdata.put("tj_havegps", DeviceInfo.getGPSAvailable());// 是否开启GPS
            jsonClientdata.put("tj_havegravity", DeviceInfo.getGravityAvailable());//重力感应
            //jsonClientdata.put("session_id", CommonUtil.getSessionid(context));
            // jsonClientdata.put("salt", CommonUtil.getSALT(context));
            jsonClientdata.put("tj_sdk_version", UmsConstants.LIB_VERSION);//sdk版本

            if (CommonUtil.isSupportlocation(context)) {
                jsonClientdata.put("tj_lat", DeviceInfo.getLatitude());//经度
                jsonClientdata.put("tj_lng", DeviceInfo.getLongitude());//纬度
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonClientdata;
    }

    public void judgeSession(final Context context) {
        CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "judgeSession on clientdata");
        try {
            if (CommonUtil.isNewSession(context)) {
                String session_id = CommonUtil.generateSession(context);
                CobubLog.i(UmsConstants.LOG_TAG, UsinglogManager.class, "New Sessionid is " + session_id);
            }
        } catch (Exception e) {
            CobubLog.e(UmsConstants.LOG_TAG, e);
        }
    }

    public void postClientData() {
        final JSONObject clientData;
        judgeSession(context);
        try {
            clientData = prepareClientdataJSON();
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
                    CobubLog.e(UmsConstants.LOG_TAG, ClientdataManager.class, "Error Code=" + errorEntity);
                    CommonUtil.saveInfoToFile("clientData", clientData, context);
                }
            });
        } else {
            CommonUtil.saveInfoToFile("clientData", clientData, context);
        }
    }
}
