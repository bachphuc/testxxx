package com.learn.turtorial1.library.dmobi;

import android.content.Context;

import com.learn.turtorial1.library.dmobi.request.DRequest;
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

    public static SBase getService(String str){
        SBase sBase = bases.get(str);
        if(sBase != null){
            return sBase;
        }

        Class c= null;
        try {
            c = Class.forName(str);
            try {
                sBase = (SBase) c.newInstance();
                bases.put(str, sBase);
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
}
