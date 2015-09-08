package com.learn.mobile.service;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.Dresponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.Feed;

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

    public SFeed() {
        feeds = new ArrayList<Feed>();
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Feed> getFeedData() {
        return this.feeds;
    }

    public void updateMaxFeedId() {
        if (feeds.size() == 0) {
            maxFeedId = 0;
        }
        Feed feed = feeds.get(0);
        maxFeedId = feed.feedId;
    }

    public void getFeeds(final Dresponse.Complete complete, final String action) {
        DRequest dRequest = DMobi.getRequest();
        dRequest.setApi("feed.gets");
        dRequest.addParam("action", action);
        if (action == LOADMORE_ACTION) {
            dRequest.addParam("page", this.page);
        } else {
            dRequest.addParam("max_feed_id", this.maxFeedId);
        }

        final SFeed that = this;
        dRequest.get(new Dresponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<ListObjectResponse<Feed>>() {
                }.getType();

                ListObjectResponse<Feed> response = gson.fromJson(respondString, type);
                if (response != null) {
                    if (response.isSuccessfully()) {
                        if(response.hasData()){
                            if (action == LOADNEW_ACTION) {
                                feeds.addAll(0, response.data);
                            } else {
                                feeds.addAll(response.data);
                                that.page++;
                            }
                            that.updateMaxFeedId();
                        }
                    }
                    else{
                        DMobi.showToast(response.getErrors());
                    }
                    if (complete != null) {
                        complete.onComplete(response);
                    }
                }
                else {
                    if (complete != null) {
                        complete.onComplete(null);
                    }
                }
            }
        }, new Dresponse.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError var1) {
                if (complete != null) {
                    complete.onComplete(null);
                    DMobi.showToast("Network error!");
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
