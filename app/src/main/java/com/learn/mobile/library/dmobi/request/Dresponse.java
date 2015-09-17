package com.learn.mobile.library.dmobi.request;

import com.android.volley.VolleyError;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DResponse {
    public static final int NO_FILE_UPLOAD = 1;
    public static final int REQUEST_FAIL = 0;
    public interface ErrorListener {
        void onErrorResponse(Object var1);
    }

    public interface Listener {
        void onResponse(String respondString);
    }

    public interface Complete{
        void onComplete(Boolean status, Object o);
    }

    public interface UpdateProcess{
        void onUpdateProcess(int percent);
    }

    public interface PreExecute{
        void onPreExecute();
    }
}
