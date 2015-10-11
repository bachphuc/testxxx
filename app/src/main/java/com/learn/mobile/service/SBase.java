package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SBase {
    public int page = 0;
    public int maxId = 0;
    public int minId = 0;

    protected List<DMobileModelBase> data = new ArrayList<DMobileModelBase>();
    protected List<DRequest.RequestParam> requestParams = new ArrayList<DRequest.RequestParam>();

    public static final String LOADMORE_ACTION = "loadmore";
    public static final String LOADNEW_ACTION = "loadnew";

    protected boolean getDataByPage = false;

    protected Class itemClass;

    protected int itemHeaderCount = 0;

    public void append(DMobileModelBase item){
        data.add(item);
        updateMaxAndMinId();
    }

    public void appends(List<DMobileModelBase> items){
        data.addAll(items);
        updateMaxAndMinId();
    }

    public void addHeader(DMobileModelBase item){
        data.add(0, item);
        itemHeaderCount++;
    }

    public void prepend(DMobileModelBase item){
        if(data.size() == 0){
            data.add(item);
        }
        else{
            if(data.size() >= itemHeaderCount){
                data.add(itemHeaderCount, item);
            }
            else{
                data.add(item);
            }
        }
        updateMaxAndMinId();
    }

    public void prepends(List<DMobileModelBase> items){
        if(data.size() == 0){
            data.addAll(items);
        }
        else{
            if(data.size() >= itemHeaderCount){
                data.addAll(itemHeaderCount, items);
            }
            else{
                data.addAll(items);
            }
        }
        updateMaxAndMinId();
    }

    public void setData(List<DMobileModelBase> list) {
        data = list;
    }

    public void setItemClass(Class c) {
        itemClass = c;
    }

    public List<DMobileModelBase> getData() {
        return data;
    }

    public void setRequestParams(DRequest.RequestParam params) {
        requestParams.add(params);
    }

    public void clearRequestParams() {
        requestParams.clear();
    }

    public void updateMaxAndMinId() {
        if (data.size() == 0 || data.size() <= itemHeaderCount) {
            maxId = 0;
            minId = 0;
            return;
        }
        DMobileModelBase item;
        item = data.get(itemHeaderCount);
        maxId = item.getId();
        item = data.get(data.size() - 1);
        minId = item.getId();
    }

    public void setGetDataByPage(boolean getDataByPage) {
        this.getDataByPage = getDataByPage;
    }

    public void gets(final DResponse.Complete complete, final String action) {
        DRequest dRequest = DMobi.createRequest();

        dRequest.setApi(itemClass.getSimpleName().toLowerCase() + ".gets");
        dRequest.addParam("action", action);
        if (requestParams.size() > 0) {
            dRequest.addParams(requestParams);
        }
        if (action == LOADMORE_ACTION) {
            if (getDataByPage) {
                dRequest.addParam("page", this.page);
            }
        }
        dRequest.addParam("max_id", this.maxId);
        dRequest.addParam("min_id", this.minId);

        final SBase that = this;
        dRequest.get(new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                DMobi.log("REQUEST", respondString);
                ListObjectResponse<DMobileModelBase> response = DbHelper.parseListObjectResponse(respondString, itemClass);
                if (response != null) {
                    if (response.isSuccessfully()) {
                        if (response.hasData()) {
                            if (action == LOADNEW_ACTION) {
                                prepends(response.data);
                            } else {
                                appends(response.data);
                                that.page++;
                            }
                        }
                    } else {
                        DMobi.showToast(response.getErrors());
                    }
                    if (complete != null) {
                        complete.onComplete(true, response);
                    }
                } else {
                    if (complete != null) {
                        complete.onComplete(false, null);
                    }
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

    public void getNews(final DResponse.Complete complete) {
        this.gets(complete, LOADNEW_ACTION);
    }

    public void getMores(final DResponse.Complete complete) {
        this.gets(complete, LOADMORE_ACTION);
    }
}
