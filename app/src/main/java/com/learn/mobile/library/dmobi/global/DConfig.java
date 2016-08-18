package com.learn.mobile.library.dmobi.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DConfig {
    private static Context context = null;
    private static String baseUrl;
    private static String apiUrl;
    private static String token = null;

    public static void setContext(Context ctx) {
        if (context != null) {
            return;
        }
        context = ctx;
    }

    public static Context getContext() {
        return context;
    }

    public static void resetHostConfig() {
        baseUrl = null;
        apiUrl = null;
    }

    public static String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = Constant.DEFAULT_HOST;
        }
        return baseUrl;
    }

    public static String getApiUrl() {
        if (apiUrl != null) {
            return apiUrl;
        }
        apiUrl = getBaseUrl() + Constant.API_URL;
        return apiUrl;
    }

    public static String getToken() {
        if (token != null) {
            return token;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        String sToken = sharedPref.getString(Constant.TOKEN_KEY, null);
        if (sToken != null) {
            token = sToken;
        }
        return token;
    }

    public static void setToken(String sToken) {
        token = sToken;
        setSetting(Constant.TOKEN_KEY, sToken);
    }

    public static void clearToken() {
        token = null;
        removeSetting(Constant.TOKEN_KEY);
    }

    public static void setSetting(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveUserData(String jsonData) {
        setSetting(Constant.USER_DATA, jsonData);
    }

    public static void clearUserData() {
        removeSetting(Constant.USER_DATA);
    }

    public static String getUserData() {
        return getSetting(Constant.USER_DATA);
    }

    public static void setSetting(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeSetting(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getSetting(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static String getSetting(String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constant.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static String getDeviceToken() {
        return DConfig.getSetting(Constant.DEVICE_TOKEN);
    }
}
