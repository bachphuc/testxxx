package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.BaseObjectResponse;
import com.learn.mobile.library.dmobi.request.response.BasicObjectResponse;
import com.learn.mobile.model.Feed;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SFeed extends SBase {
    private static final String TAG = SFeed.class.getSimpleName();
    public SFeed(){
        itemClass = Feed.class;
    }

    public void setProfileUser(int userId){
        clearGetRequestParams();
        setGetRequestParams(DRequest.createRequestParam("user_id", userId));
    }

    public void delete(int feed_id){
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("feed.delete");
        dRequest.addParam("feed_id", feed_id);
        dRequest.get(new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                BasicObjectResponse baseObjectResponse = DbHelper.parseResponse(respondString);
                if (baseObjectResponse != null) {
                    if (baseObjectResponse.isSuccessfully()) {
                        String message = baseObjectResponse.getMessage();
                        if (message == null || message == "") {
                            message = "Action completed.";
                        }
                        DMobi.showToast(DMobi.translate(message));
                    } else {
                        DMobi.showToast(baseObjectResponse.getErrors());
                    }
                } else {
                    DMobi.showToast(DMobi.translate("Request fail."));
                }
            }
        }, new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object var1) {
                DMobi.showToast("Can not connect to server.");
            }
        });
    }

    public void delete(Feed feed){
        if(feed == null){
            DMobi.log(TAG, DMobi.translate("Can not delete this feed because it's null."));
            return;
        }
        this.delete(feed.getId());
    }
}
