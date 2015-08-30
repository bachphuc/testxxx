package com.learn.turtorial1.library.dmobi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.learn.turtorial1.R;
import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.learn.turtorial1.library.dmobi.request.DRequest;
import com.learn.turtorial1.model.DmobileModelBase;
import com.learn.turtorial1.service.SBase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DMobi {
    private Context context = null;
    public static void getLib(String lib){

    }

    static {
        bases = new Hashtable<String, SBase>();
    }

    private static Hashtable<String,SBase> bases;

    public static SBase getService(String service, String key){
        String serviceKey = service + "_" + key;
        SBase sBase = bases.get(serviceKey);
        if(sBase != null){
            return sBase;
        }
        String sClass = DConfig.BUNDLE_ID + ".service." + service;
        Class c= null;
        try {
            c = Class.forName(sClass);
            try {
                sBase = (SBase) c.newInstance();
                bases.put(serviceKey, sBase);
                return sBase;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static SBase getService(String service){
        return DMobi.getService(service, "");
    }

    public static SBase getService(Class aClass) {
        return DMobi.getService(aClass.getSimpleName());
    }

    public static SBase getService(Class aClass, String key) {
        return DMobi.getService(aClass.getSimpleName(), key);
    }

    public static SBase getInstance(String service){
        String sClass = DConfig.BUNDLE_ID + ".service." + service;
        Class c= null;
        SBase sBase;
        try {
            c = Class.forName(sClass);
            try {
                sBase = (SBase) c.newInstance();
                return sBase;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static DRequest getRequest(){
        return new DRequest();
    }

    public static DRequest getRequest(Context ct){
        return new DRequest(ct);
    }

    public static boolean isUser(){
       String token = DConfig.getToken();
        if(token != null){
            return true;
        }
        return false;
    }
}
