package com.learn.turtorial1.library.dmobi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.learn.turtorial1.library.dmobi.event.Event;
import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.learn.turtorial1.library.dmobi.request.DRequest;
import com.learn.turtorial1.service.SBase;

import java.util.Hashtable;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DMobi {
    private Context context = null;

    private static Hashtable<String, SBase> bases;
    private static Hashtable<String, Event> eventHashtable;

    static {
        bases = new Hashtable<String, SBase>();
        eventHashtable = new Hashtable<String, Event>();
    }

    public static void getLib(String lib) {

    }

    public static SBase getService(String service, String key) {
        String serviceKey = service + "_" + key;
        SBase sBase = bases.get(serviceKey);
        if (sBase != null) {
            return sBase;
        }
        String sClass = DConfig.BUNDLE_ID + ".service." + service;
        Class c = null;
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

    public static SBase getService(String service) {
        return DMobi.getService(service, "");
    }

    public static SBase getService(Class aClass) {
        return DMobi.getService(aClass.getSimpleName());
    }

    public static SBase getService(Class aClass, String key) {
        return DMobi.getService(aClass.getSimpleName(), key);
    }

    public static SBase getInstance(String service) {
        String sClass = DConfig.BUNDLE_ID + ".service." + service;
        Class c = null;
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

    public static DRequest getRequest() {
        return new DRequest();
    }

    public static DRequest getRequest(Context ct) {
        return new DRequest(ct);
    }

    // check if user has already login
    public static boolean isUser() {
        String token = DConfig.getToken();
        if (token != null) {
            return true;
        }
        return false;
    }

    public static void showToast(String message) {
        Toast toast = Toast.makeText(DConfig.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // show toast with message and time
    public static void showToast(String message, int time) {
        Toast toast = Toast.makeText(DConfig.getContext(), message, time);
        toast.show();
    }

    // display log to debug when EBUG_MODE enable
    public static void log(String key, Object o) {
        if (DConfig.DBUG_MODE != 0) {
            Log.i(key, o.toString());
        }
    }

    // register a global event
    public static Event registerEvent(String key){
        Event event = eventHashtable.get(key);
        if(event != null){
            return event;
        }
        event = new Event();
        eventHashtable.put(key, event);
        return event;
    }

    public static Event registerEvent(String key, Event.Action action) {
        Event event = eventHashtable.get(key);
        if(event != null){
            return event;
        }
        event = new Event(action);
        eventHashtable.put(key, event);
        return event;
    }

    public boolean isRegisterEvent(String key){
        Event event = eventHashtable.get(key);
        return (event != null ? true : false);
    }

    public static Event getEvent(String key){
        Event event = eventHashtable.get(key);
        return event;
    }

    public static boolean destroyEvent(String key){
        Event event = eventHashtable.get(key);
        if(event == null){
            return false;
        }
        event = null;
        eventHashtable.remove(key);
        return true;
    }

    public static void fireEvent(String key, Object o){
        Event event = eventHashtable.get(key);
        if(event != null){
            event.fireAction(o);
        }
    }
}
