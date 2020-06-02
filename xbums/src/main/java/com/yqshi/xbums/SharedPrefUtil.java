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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class SharedPrefUtil {
    private SharedPreferences sp = null;
    private Editor edit = null;
    
    public SharedPrefUtil(Context context) {
        this.sp = context.getSharedPreferences("CobubRazor_SharedPref",
                Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    public void setValue(String key, long value) {
        edit.putLong(key, value);
        edit.commit();
    }

    public void removeKey(String key) {
        edit.remove(key);
        edit.commit();
    }

    public void setValue(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }

    public void setValue(String key, Boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }

    public long getValue(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public String getValue(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public Boolean getValue(String key, Boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }
   
    
}
