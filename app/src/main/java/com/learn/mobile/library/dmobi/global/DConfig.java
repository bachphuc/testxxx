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

    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String APP_NAME = "dmobi";
    public static final String USER_DATA = "USER_DATA";
    public static final String SITE_URL = "SITE_URL";
    public static String DEFAULT_HOST = "http://dmobi.pe.hu";
    public static final String BUNDLE_ID = "com.learn.mobile";
    public static final String preference_file_key = BUNDLE_ID + ".PREFERENCE_FILE_KEY";
    public static final int DEBUG_MODE = 1;
    public static final String DATABASE_OFFLINE = APP_NAME + ".db";
    public static final String MODEL_NAME = "model";
    public static final String SERVICE_NAME = "service";
    public static final int MAX_ATTACHMENTS_SHOW = 4;

    public static final int MAX_SUGGESTION_LIMIT = 10;

    public static final boolean HIDE_TAB_BAR = true;

    // Can use GlideImageAdapter OR PicassoImageAdapter
    public static final String IMAGE_ADAPTER = "GlideImageAdapter";

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
            baseUrl = getSetting(SITE_URL, DEFAULT_HOST);
        }
        return baseUrl;
    }

    public static String getApiUrl() {
        if (apiUrl != null) {
            return apiUrl;
        }
        apiUrl = getBaseUrl() + "/module/dmobile/api.php";
        return apiUrl;
    }

    public static String getToken() {
        if (token != null) {
            return token;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        String sToken = sharedPref.getString(DConfig.TOKEN_KEY, null);
        if (sToken != null) {
            token = sToken;
        }
        return token;
    }

    public static void setToken(String sToken) {
        token = sToken;
        setSetting(DConfig.TOKEN_KEY, sToken);
    }

    public static void clearToken() {
        token = null;
        removeSetting(DConfig.TOKEN_KEY);
    }

    public static void setSetting(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveUserData(String jsonData) {
        setSetting(USER_DATA, jsonData);
    }

    public static void clearUserData() {
        removeSetting(USER_DATA);
    }

    public static String getUserData() {
        return getSetting(USER_DATA);
    }

    public static void setSetting(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeSetting(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getSetting(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static String getSetting(String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }
}
