package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.model.Feed;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SFeed extends SBase {
    public SFeed(){
        itemClass = Feed.class;
    }

    public void setProfileUser(int userId){
        clearGetRequestParams();
        setGetRequestParams(DRequest.createRequestParam("user_id", userId));
    }
}
