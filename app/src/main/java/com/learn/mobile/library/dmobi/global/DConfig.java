package com.learn.mobile.library.dmobi.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DConfig {
    private static Context context = null;
    private static String baseUrl = "http://dmobi.pe.hu";
    private static String apiUrl = "http://dmobi.pe.hu/module/dmobile/api.php";
    private static String token = null;

    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String USER_DATA = "USER_DATA";
    public static final String BUNDLE_ID = "com.learn.mobile";
    public static final String preference_file_key = "com.learn.mobile.PREFERENCE_FILE_KEY";
    public static final int DEBUG_MODE = 1;

    public static void setContext(Context ctx){
        if(context != null){
            return;
        }
        context = ctx;
    }

    public static Context getContext(){
        return context;
    }

    public static String getBaseUrl(){
        return baseUrl;
    }

    public static String getApiUrl(){
        return apiUrl;
    }

    public static String getToken(){
        if(token != null){
            return token;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        String sToken = sharedPref.getString(DConfig.TOKEN_KEY, null);
        if(sToken != null){
            token = sToken;
        }
        return token;
    }

    public static void setToken(String sToken){
        token = sToken;
        setSetting(DConfig.TOKEN_KEY, sToken);
    }

    public static void clearToken(){
        token = null;
        removeSetting(DConfig.TOKEN_KEY);
    }

    public static void setSetting(String key, int value){
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveUserData(String jsonData){
        setSetting(USER_DATA, jsonData);
    }

    public static void clearUserData(){
        removeSetting(USER_DATA);
    }

    public static String getUserData(){
        return getSetting(USER_DATA);
    }

    public static void setSetting(String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void removeSetting(String key){
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getSetting(String key){
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static String getSetting(String key, String defaultValue){
        SharedPreferences sharedPref = context.getSharedPreferences(DConfig.preference_file_key, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }
}
