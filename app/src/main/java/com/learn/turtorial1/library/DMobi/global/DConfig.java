package com.learn.turtorial1.library.dmobi.global;

import android.content.Context;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DConfig {
    private static Context context = null;
    private static String baseUrl = "http://dmobi.pe.hu";
    private static String apiUrl = "http://dmobi.pe.hu/module/dmobile/api.php";

    public static void setContext(Context ctx){
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
        return "b3cff55d83b4367ade5413";
    }
}
