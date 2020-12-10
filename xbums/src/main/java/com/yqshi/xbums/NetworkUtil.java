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

import com.yqshi.xbums.constants.UmsConstants;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

class NetworkUtil {
    public static int SO_TIMEOUT = 30; // 30s
    public static String CONTENT_MEDIA_TYPE = "application/json; charset=utf-8";

    private static void initSSL() {
        CobubLog.d(UmsConstants.LOG_TAG, NetworkUtil.class, "InitSSL start it:" + UmsConstants.SDK_POS_NAME);
        System.setProperty("javax.net.ssl.keyStoreProvider",
                UmsConstants.SDK_POS_NAME);
        System.setProperty("javax.net.ssl.certAlias",
                UmsConstants.SDK_CSR_ALIAS);
        CobubLog.d(UmsConstants.LOG_TAG, NetworkUtil.class, "InitSSL end it:" + UmsConstants.SDK_CSR_ALIAS);
    }

    public static void Post(String url, String data, final OnDataCallBack onDataCallBack) {
        CobubLog.d(UmsConstants.LOG_TAG, NetworkUtil.class, "URL====> " + url);
        CobubLog.d(UmsConstants.LOG_TAG, NetworkUtil.class, "data====>" + data);

        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(SO_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(SO_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(SO_TIMEOUT, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse(CONTENT_MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(data, mediaType))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onDataCallBack != null) {
                    onDataCallBack.onFailure(e.getMessage());
                }

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

            }

        });


    }


    private static boolean isJson(String strForValidating) {
        try {
            JSONObject jsonObject = new JSONObject(strForValidating);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
