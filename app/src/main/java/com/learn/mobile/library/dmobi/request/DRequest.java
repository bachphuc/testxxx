package com.learn.mobile.library.dmobi.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DRequest {
    private static final String TAG = DRequest.class.getSimpleName();

    public DRequest() {
        requestParams = new HashMap<>();
        postData = new HashMap<>();
    }

    public DRequest(Context ct) {
        this.context = ct;
        requestParams = new HashMap<>();
        postData = new HashMap<>();
    }

    private HashMap<String, Object> requestParams;
    private Map<String, String> postData;
    private String api = "";
    private Context context = null;

    private DResponse.ErrorListener errorListener = null;
    private DResponse.Listener listener = null;
    private DResponse.Complete complete = null;
    private DResponse.PreExecute preExecute = null;
    private DResponse.UpdateProcess updateProcess = null;

    String filePath;

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public void addParam(String key, Object value) {
        requestParams.put(key, value);
    }

    public void addPost(String key, String value) {
        value = DUtils.getString(value);
        postData.put(key, value);
    }

    public void addPosts(HashMap<String, Object> params) {
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                addPost(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public void addParams(HashMap<String, Object> params) {
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                addParam(entry.getKey(), entry.getValue());
            }
        }
    }

    public String getRequestUrl() {
        addParam("android", 1);
        if (requestParams.size() == 0) {
            return "";
        }
        String str = DConfig.getApiUrl() + "?api=" + this.api + "&token=" + DConfig.getToken() + "&";
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            str += entry.getKey() + "=" + entry.getValue().toString() + "&";
        }
        str = DUtils.trimAll(str, ',');
        DMobi.log("Url Request", str);
        return str;
    }

    public void resetParam() {
        requestParams.clear();
    }

    public void setApi(String str) {
        this.api = str;
    }

    public void setListener(DResponse.Listener listener) {
        this.listener = listener;
    }

    public void setErrorListener(DResponse.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setComplete(DResponse.Complete complete) {
        this.complete = complete;
    }

    public void setPreExecute(DResponse.PreExecute preExecute) {
        this.preExecute = preExecute;
    }

    public void setUpdateProcess(DResponse.UpdateProcess updateProcess) {
        this.updateProcess = updateProcess;
    }

    public void execute(final int method) {
        final DResponse.Listener ls = this.listener;
        final DResponse.ErrorListener er = this.errorListener;
        final DResponse.Complete cm = this.complete;
        Context ct = (this.context != null ? this.context : DConfig.getContext());
        RequestQueue requestQueue = Volley.newRequestQueue(ct);
        String url = this.getRequestUrl();
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (ls != null) {
                    ls.onResponse(s);
                }
                if (cm != null) {
                    cm.onComplete(true, s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (er != null) {
                    er.onErrorResponse(volleyError);
                }
                if (cm != null) {
                    cm.onComplete(false, volleyError.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (getMethod() == Method.POST && postData.size() > 0) {
                    return postData;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (getMethod() == Method.POST) {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
                return super.getHeaders();
            }
        };
        if (preExecute != null) {
            preExecute.onPreExecute();
        }
        requestQueue.add(stringRequest);
        this.resetParam();
    }

    public void get() {
        this.execute(Request.Method.GET);
    }

    public void post() {
        this.execute(Request.Method.POST);
    }

    public void post(DResponse.Listener listener, DResponse.ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
        this.execute(Request.Method.POST);
    }

    public void get(DResponse.Listener listener, DResponse.ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
        this.get();
    }

    public void get(DResponse.Complete complete) {
        this.complete = complete;
        this.get();
    }


    public void upload() {
        UploadFileToServer uploadFileToServer = new UploadFileToServer();
        uploadFileToServer.setCallBack(preExecute, updateProcess, listener, errorListener, complete);
        uploadFileToServer.setFilePath(filePath);
        uploadFileToServer.setUrl(this.getRequestUrl());
        if (postData.size() > 0) {
            uploadFileToServer.setParams(postData);
        }
        uploadFileToServer.execute();
    }

    public void upload(String filePath) {
        this.setFilePath(filePath);
        this.upload();
    }

    public void setContext(Context c) {
        this.context = c;
    }
}
