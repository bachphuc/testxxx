package com.learn.turtorial1.service;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.turtorial1.library.dmobi.DMobi;
import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.learn.turtorial1.library.dmobi.request.DRequest;
import com.learn.turtorial1.library.dmobi.request.Dresponse;
import com.learn.turtorial1.model.Feed;
import com.learn.turtorial1.model.RequestResultObject;

import java.lang.reflect.Type;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SFeed extends SBase {
    private int page = 0;

    public void setPage(int page) {
        this.page = page;
    }

    public void getFeeds(final Dresponse.Complete complete) {
        DRequest dRequest = DMobi.getRequest();
        dRequest.setApi("feed.gets");
        dRequest.setParam("android", 1);
        dRequest.setParam("action", "loadmore");
        dRequest.setParam("page", this.page);
        final SFeed that = this;
        dRequest.get(new Dresponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<RequestResultObject<Feed>>() {
                }.getType();

                RequestResultObject<Feed> respond = gson.fromJson(respondString, type);

                if (respond != null && complete != null) {
                    that.page++;
                    complete.onComplete(respond.data);
                }
            }
        }, new Dresponse.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError var1) {
                if (complete != null) {
                    complete.onComplete(null);
                    Toast toast = Toast.makeText(DConfig.getContext(), "Network error!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
