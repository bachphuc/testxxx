package com.learn.mobile.library.dmobi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SBase;
import com.learn.mobile.service.SUser;

import java.util.Hashtable;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DMobi {
    private static final String TAG = DMobi.class.getSimpleName();
    private Context context = null;

    private static Hashtable<String, SBase> bases;
    private static Hashtable<String, Event> eventHashTable;
    private static Hashtable<String, Object> globalDatas;
    private static int identityId = 0;

    static {
        bases = new Hashtable<String, SBase>();
        eventHashTable = new Hashtable<String, Event>();
        globalDatas = new Hashtable<String, Object>();
    }

    public static void getLib(String lib) {

    }

    public static int getIdentityId() {
        identityId++;
        return identityId;
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

    public static SBase getInstance(Class service) {
        String sClass = DConfig.BUNDLE_ID + ".service." + service.getSimpleName();
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

    public static DRequest createRequest() {
        return new DRequest();
    }

    public static DRequest createRequest(Context ct) {
        return new DRequest(ct);
    }

    // TODO check if user has already login
    public static boolean isUser() {
        String token = DConfig.getToken();
        if (token != null) {
            return true;
        }
        return false;
    }

    public static void showToast(String message) {
        message = DMobi.translate(message);
        Toast toast = Toast.makeText(DConfig.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(String message, boolean isError) {
        if (isError && DConfig.DEBUG_MODE == 1) {
            showToast(message);
        } else {
            DMobi.log(TAG, message);
        }
    }

    // TODO show toast with message and time
    public static void showToast(String message, int time) {
        message = DMobi.translate(message);
        Toast toast = Toast.makeText(DConfig.getContext(), message, time);
        toast.show();
    }

    // TODO Show login
    public static ProgressDialog showLoading(Context context, String title, String description) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(description);
        return progressDialog;
    }

    // TODO show alert
    public static void alert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alert(Context context, String message) {
        message = DMobi.translate(message);
        alert(context, DMobi.translate("Alert"), message);
    }

    // TODO display log to debug when EBUG_MODE enable
    public static void log(String key, Object o) {
        if (DConfig.DEBUG_MODE != 0) {
            Log.i(key, o.toString());
        }
    }

    // TODO register a global event
    public static Event registerEvent(String key) {
        Event event = eventHashTable.get(key);
        if (event != null) {
            return event;
        }
        event = new Event();
        eventHashTable.put(key, event);
        return event;
    }

    public static Event registerEvent(String key, Event.Action action) {
        Event event = eventHashTable.get(key);
        if (event != null) {
            if(action != null){
                event.addAction(action);
            }
            return event;
        }
        event = new Event(action, key);
        eventHashTable.put(key, event);
        return event;
    }

    public boolean isRegisterEvent(String key) {
        Event event = eventHashTable.get(key);
        return (event != null ? true : false);
    }

    public static Event getEvent(String key) {
        Event event = eventHashTable.get(key);
        return event;
    }

    public static boolean resetEvent(String key) {
        Event event = eventHashTable.get(key);
        if (event != null) {
            event.clearAction();
            return true;
        }
        return false;
    }

    public static boolean destroyEvent(String key) {
        Event event = eventHashTable.get(key);
        if (event == null) {
            return false;
        }
        event = null;
        eventHashTable.remove(key);
        return true;
    }

    public static void fireEvent(String key, Object o) {
        Event event = eventHashTable.get(key);
        if (event != null) {
            event.fireAction(o);
        }
    }

    public static void pushData(String key, Object o) {
        globalDatas.put(key, o);
    }

    public static Object getData(String key) {
        return globalDatas.get(key);
    }

    public static void clearData() {
        globalDatas.clear();
    }

    public static User getUser() {
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        return sUser.getUser();
    }

    public static String translate(String s) {
        return s;
    }
}
