package com.learn.turtorial1.service;

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
import com.learn.turtorial1.model.RequestListObjectResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SFeed extends SBase {
    private int page = 0;
    private int maxFeedId = 0;
    private List<Feed> feeds;
    public static final String LOADMORE_ACTION = "loadmore";
    public static final String LOADNEW_ACTION = "loadnew";

    public SFeed(){
        feeds = new ArrayList<Feed>();
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Feed> getData(){
        return this.feeds;
    }

    public void updateMaxFeedId() {
        if(feeds.size() == 0){
            maxFeedId = 0;
        }
        Feed feed = feeds.get(0);
        maxFeedId = feed.feedId;
    }

    public void getFeeds(final Dresponse.Complete complete, final String action) {
        DRequest dRequest = DMobi.getRequest();
        dRequest.setApi("feed.gets");
        dRequest.setParam("android", 1);
        dRequest.setParam("action", action);
        if(action == LOADMORE_ACTION){
            dRequest.setParam("page", this.page);
        }
        else{
            dRequest.setParam("max_feed_id", this.maxFeedId);
        }

        final SFeed that = this;
        dRequest.get(new Dresponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<RequestListObjectResponse<Feed>>() {
                }.getType();

                RequestListObjectResponse<Feed> respond = gson.fromJson(respondString, type);

                if (respond != null && complete != null) {
                    if(action == LOADMORE_ACTION){
                        feeds.addAll(0, respond.data);
                    }
                    else{
                        feeds.addAll(respond.data);
                        that.page++;
                    }
                    that.updateMaxFeedId();
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

    public void loadMoreFeeds(final Dresponse.Complete complete) {
        this.getFeeds(complete, LOADMORE_ACTION);
    }

    public void loadNewFeeds(final Dresponse.Complete complete) {
        this.getFeeds(complete, LOADNEW_ACTION);
    }
}
