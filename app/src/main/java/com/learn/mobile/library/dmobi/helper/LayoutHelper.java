package com.learn.mobile.library.dmobi.helper;

import com.learn.mobile.library.dmobi.DMobi;

import java.util.HashMap;

/**
 * Created by 09520_000 on 9/26/2015.
 */
public class LayoutHelper {
    private static HashMap<String, Object> layoutMaps = new HashMap<String, Object>();
    private static HashMap<String, Object> layoutTypes = new HashMap<String, Object>();

    public final static String FEED_LAYOUT = "FEED_LAYOUT";
    public final static String LIST_LAYOUT = "LIST_LAYOUT";

    public static int registerLayout(Class c, String suffix, int layout){
        if(!layoutMaps.containsKey(c.getSimpleName() + suffix)){
            int id = DMobi.getIdentityId();
            layoutTypes.put(id + "", layout);
            layoutMaps.put(c.getSimpleName(), id);
            return id;
        }
        int id = (int)layoutMaps.get(c.getSimpleName() + suffix);
        return id;
    }

    public static int getLayout(int id){
        if(layoutTypes.containsKey(id + "")){
            return (int) layoutTypes.get(id + "");
        }
        return 0;
    }
}
