package com.learn.mobile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.Link;

import java.lang.reflect.Type;

/**
 * Created by 09520_000 on 9/20/2015.
 */
public class SLink extends SBase {
    public SLink() {
        itemClass = Link.class;
    }

    public void preview(String url, final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("link.preview");
        dRequest.addParam("link_url", url);

        dRequest.get(new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                SingleObjectResponse response = DbHelper.parseSingleObjectResponse(respondString, Link.class);
                if (response.isSuccessfully()) {
                    complete.onComplete(true, response.data);
                } else {
                    DMobi.showToast(response.getErrors());
                    complete.onComplete(false, null);
                }
            }
        }, new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object var1) {
                if (complete != null) {
                    complete.onComplete(false, null);
                    DMobi.showToast("Network error!");
                }
            }
        });
    }
}
