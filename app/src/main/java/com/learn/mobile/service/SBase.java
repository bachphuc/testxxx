package com.learn.mobile.service;

import com.android.volley.VolleyError;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.Dresponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DmobileModelBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SBase {
    public int page = 0;
    public int maxId = 0;
    public int minId = 0;
    protected List<DmobileModelBase> data = new ArrayList<DmobileModelBase>();
    public static final String LOADMORE_ACTION = "loadmore";
    public static final String LOADNEW_ACTION = "loadnew";

    protected Class itemClass;

    public void setData(List<DmobileModelBase> list) {
        data = list;
    }

    public List<DmobileModelBase> getData() {
        return data;
    }

    public void updateMaxAndMinId() {
        if (data.size() == 0) {
            maxId = 0;
            minId = 0;
        }
        DmobileModelBase item = data.get(0);
        maxId = item.getId();
        item = data.get(data.size() - 1);
        minId = item.getId();
    }

    public void gets(final Dresponse.Complete complete, final String action) {
        DRequest dRequest = DMobi.getRequest();

        dRequest.setApi(itemClass.getSimpleName().toLowerCase() + ".gets");
        dRequest.addParam("action", action);
        if (action == LOADMORE_ACTION) {
            dRequest.addParam("page", this.page);
        }
        dRequest.addParam("max_id", this.maxId);
        dRequest.addParam("min_id", this.minId);

        final SBase that = this;
        dRequest.get(new Dresponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                ListObjectResponse<DmobileModelBase> response = DbHelper.parseListObjectResponse(respondString, itemClass);
                if (response != null) {
                    if (response.isSuccessfully()) {
                        if(response.hasData()){
                            if (action == LOADMORE_ACTION) {
                                data.addAll(0, response.data);
                            } else {
                                data.addAll(response.data);
                                that.page++;
                            }
                            that.updateMaxAndMinId();
                            if (complete != null) {
                                complete.onComplete(response.data);
                            }
                        }
                        else{
                            complete.onComplete(null);
                        }
                    }
                    else{
                        DMobi.showToast(response.getErrors());
                        if (complete != null) {
                            complete.onComplete(null);
                        }
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

    public void getNews(final Dresponse.Complete complete){
        this.gets(complete, LOADNEW_ACTION);
    }

    public void getMores(final Dresponse.Complete complete) {
        this.gets(complete, LOADMORE_ACTION);
    }
}
