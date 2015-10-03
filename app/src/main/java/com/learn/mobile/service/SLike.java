package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.DMobileModelBase;

/**
 * Created by 09520_000 on 10/3/2015.
 */
public class SLike extends SBase {
    public void like(String typeId, int itemId, DResponse.Complete complete) {
        execute("like.like", typeId, itemId, complete);
    }

    public void removeLike(String typeId, int itemId, DResponse.Complete complete) {
        execute("like.removeLike", typeId, itemId, complete);
    }

    private void execute(String api, String typeId, int itemId, final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi(api);
        dRequest.addParam("type_id", typeId);
        dRequest.addParam("item_id", itemId);

        dRequest.get(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                String respondString = (String) o;
                if (status) {
                    SingleObjectResponse response = DbHelper.parseSingleObjectResponse(respondString, DMobileModelBase.class);
                    if (response.isSuccessfully()) {
                        complete.onComplete(true, response.data);
                    } else {
                        DMobi.showToast(response.getErrors());
                        complete.onComplete(false, null);
                    }
                } else {
                    DMobi.showToast(respondString, true);
                }
            }

        });
    }
}

