package com.learn.turtorial1.library.dmobi.request;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learn.turtorial1.library.dmobi.Utils.Utils;
import com.learn.turtorial1.library.dmobi.global.DConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DRequest {
    public DRequest(){
        requestParams = new ArrayList<RequestParam>();
    }

    public DRequest(Context ct){
        this.context = ct;
        requestParams = new ArrayList<RequestParam>();
    }

    private List<RequestParam> requestParams;
    private String api = "";
    private Context context = null;

    private Dresponse.ErrorListener errorListener = null;
    private Dresponse.Listener listener = null;
    private Dresponse.Complete complete = null;

    public void setParam(String key, Object value){
        RequestParam re = new RequestParam(key, value);
        requestParams.add(re);
    }

    public String getRequestUrl(){
        if(requestParams.size() == 0){
            return "";
        }
        String str = DConfig.getApiUrl() + "?api= " + this.api + "&token=" + DConfig.getToken() + "&";
        for(int i = 0; i < requestParams.size(); i++){
            RequestParam rq = requestParams.get(i);
            str+= rq.key + "=" + rq.value.toString() + "&";
        }
        str = Utils.trimAll(str, ',');
        return str;
    }

    public void resetParam(){
        requestParams.clear();
    }

    public void setApi(String str){
        this.api = str;
    }

    public void setListener(Dresponse.Listener listener){
        this.listener = listener;
    }

    public void setErrorListener(Dresponse.ErrorListener errorListener){
        this.errorListener = errorListener;
    }

    public void setComplete(Dresponse.Complete complete){
        this.complete = complete;
    }

    public void get(){
        final Dresponse.Listener ls = this.listener;
        final Dresponse.ErrorListener er = this.errorListener;
        final Dresponse.Complete cm = this.complete;
        Context ct = (this.context != null ? this.context : DConfig.getContext());
        RequestQueue reqestQueue = Volley.newRequestQueue(ct);
        String url = this.getRequestUrl();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(ls != null){
                    ls.onResponse(s);
                }
                if(cm != null){
                    cm.onComplete(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(er != null){
                    er.onErrorResponse(volleyError);
                }
                if(cm != null){
                    cm.onComplete(volleyError);
                }
            }
        });

        reqestQueue.add(stringRequest);
        this.resetParam();
    }

    public void get(Dresponse.Listener listener , Dresponse.ErrorListener errorListener){
        this.listener = listener;
        this.errorListener = errorListener;
        this.get();
    }

    public void upload(){

    }

    class RequestParam{
        public RequestParam(){

        }

        public RequestParam(String key, Object object){
            this.key = key;
            this.value = object;
        }
        public String key = null;
        public Object value = null;
    }
}
