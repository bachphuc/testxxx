package com.learn.mobile.library.dmobi.request;

import com.android.volley.VolleyError;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class Dresponse {
    public interface ErrorListener {
        void onErrorResponse(VolleyError var1);
    }

    public interface Listener {
        void onResponse(String respondString);
    }

    public interface Complete{
        void onComplete(Object o);
    }
}
