package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.Comment;
import com.learn.mobile.model.DMobileModelBase;

/**
 * Created by 09520_000 on 10/4/2015.
 */
public class SComment extends SBase {
    public SComment() {
        itemClass = Comment.class;
        getDataByPage = true;
    }

    public void comment(DMobileModelBase item, String content, final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("comment.add");
        dRequest.addParam("item_type", item.getItemType());
        dRequest.addParam("item_id", item.getId());
        dRequest.addPost("text", content);
        dRequest.setComplete(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    String responseText = (String) o;
                    SingleObjectResponse<DMobileModelBase> response = DbHelper.parseSingleObjectResponse(responseText, Comment.class);
                    if (response.isSuccessfully()) {
                        data.add(response.data);
                        if (complete != null) {
                            complete.onComplete(status, response.data);
                        }
                    } else {
                        DMobi.showToast(response.getErrors());
                        complete.onComplete(status, o);
                    }

                } else {
                    if (complete != null) {
                        complete.onComplete(status, o);
                    }
                    String error = (String) o;
                    DMobi.showToast(error);
                }
            }
        });
        dRequest.post();
    }
}
