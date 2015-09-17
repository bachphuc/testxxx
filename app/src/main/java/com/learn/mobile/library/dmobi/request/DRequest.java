package com.learn.mobile.library.dmobi.request;

import android.content.Context;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.Utils.Utils;
import com.learn.mobile.library.dmobi.global.DConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DRequest {
    public DRequest() {
        requestParams = new ArrayList<RequestParam>();
    }

    public DRequest(Context ct) {
        this.context = ct;
        requestParams = new ArrayList<RequestParam>();
    }

    private List<RequestParam> requestParams;
    private String api = "";
    private Context context = null;

    private DResponse.ErrorListener errorListener = null;
    private DResponse.Listener listener = null;
    private DResponse.Complete complete = null;
    private DResponse.PreExecute preExecute = null;
    private DResponse.UpdateProcess updateProcess = null;

    Uri fileUri;

    public void setFileUri(Uri uri) {
        this.fileUri = uri;
    }

    public void addParam(String key, Object value) {
        RequestParam re = new RequestParam(key, value);
        requestParams.add(re);
    }

    public void addParams(List<RequestParam> params) {
        requestParams.addAll(params);
    }

    public static RequestParam createRequestParam(String key, Object value) {
        return new RequestParam(key, value);
    }

    public String getRequestUrl() {
        addParam("android", 1);
        if (requestParams.size() == 0) {
            return "";
        }
        String str = DConfig.getApiUrl() + "?api=" + this.api + "&token=" + DConfig.getToken() + "&";
        for (int i = 0; i < requestParams.size(); i++) {
            RequestParam rq = requestParams.get(i);
            str += rq.key + "=" + rq.value.toString() + "&";
        }
        str = Utils.trimAll(str, ',');
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

    public void get() {
        final DResponse.Listener ls = this.listener;
        final DResponse.ErrorListener er = this.errorListener;
        final DResponse.Complete cm = this.complete;
        Context ct = (this.context != null ? this.context : DConfig.getContext());
        RequestQueue reqestQueue = Volley.newRequestQueue(ct);
        String url = this.getRequestUrl();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
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
                    cm.onComplete(false, volleyError);
                }
            }
        });

        reqestQueue.add(stringRequest);
        this.resetParam();
    }

    public void get(DResponse.Listener listener, DResponse.ErrorListener errorListener) {
        this.listener = listener;
        this.errorListener = errorListener;
        this.get();
    }

    public static class RequestParam {
        public RequestParam() {

        }

        public RequestParam(String key, Object object) {
            this.key = key;
            this.value = object;
        }

        public String key = null;
        public Object value = null;
    }

    public void upload() {
        UploadFileToServer uploadFileToServer = new UploadFileToServer();
        uploadFileToServer.setCallBack(preExecute, updateProcess, listener, errorListener, complete);
        uploadFileToServer.setFileUri(fileUri);
        uploadFileToServer.setUrl(this.getRequestUrl());
        uploadFileToServer.execute();
    }

    public void upload(Uri fileUri) {
        this.setFileUri(fileUri);
        this.upload();
    }
}
