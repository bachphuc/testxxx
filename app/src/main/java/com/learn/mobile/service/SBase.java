package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.BaseObjectResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SBase {
    public int page = 0;
    public int maxId = 0;
    public int minId = 0;

    protected User user;

    protected List<DMobileModelBase> data = new ArrayList<DMobileModelBase>();
    protected List<DRequest.RequestParam> requestParams = new ArrayList<DRequest.RequestParam>();

    public static final String LOADMORE_ACTION = "loadmore";
    public static final String LOADNEW_ACTION = "loadnew";
    public static final String ENTRY_ID = "id";

    protected boolean getDataByPage = false;

    protected Class itemClass;

    protected int itemHeaderCount = 0;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void append(DMobileModelBase item) {
        data.add(item);
        updateMaxAndMinId();
    }

    public void appends(List<DMobileModelBase> items) {
        data.addAll(items);
        updateMaxAndMinId();
    }

    public void addHeader(DMobileModelBase item) {
        data.add(0, item);
        itemHeaderCount++;
    }

    public void prepend(DMobileModelBase item) {
        if (data.size() == 0) {
            data.add(item);
        } else {
            if (data.size() >= itemHeaderCount) {
                data.add(itemHeaderCount, item);
            } else {
                data.add(item);
            }
        }
        updateMaxAndMinId();
    }

    public void prepends(List<DMobileModelBase> items) {
        if (data.size() == 0) {
            data.addAll(items);
        } else {
            if (data.size() >= itemHeaderCount) {
                data.addAll(itemHeaderCount, items);
            } else {
                data.addAll(items);
            }
        }
        updateMaxAndMinId();
    }

    public void clear() {
        data.clear();
        page = 0;
        maxId = 0;
        minId = 0;
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
        if (user != null) {
            dRequest.addParam("user_id", user.getId());
        }
        if (requestParams.size() > 0) {
            dRequest.addParams(requestParams);
        }
        if (action == LOADMORE_ACTION) {
            if (getDataByPage) {
                dRequest.addParam("page", this.page);
            }
        }
        updateMaxAndMinId();
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

    public void networkError(DResponse.Complete complete) {
        if (complete != null) {
            complete.onComplete(false, null);
        }
        DMobi.showToast("Network error!");
    }

    public void responseError(BaseObjectResponse response, DResponse.Complete complete) {
        DMobi.showToast(response.getErrors());
        if (complete != null) {
            complete.onComplete(false, null);
        }
    }

    public void get(final DResponse.Complete complete, int itemId) {
        DRequest dRequest = DMobi.createRequest();

        dRequest.setApi(itemClass.getSimpleName().toLowerCase() + ".get");
        dRequest.addParam(ENTRY_ID, itemId);
        final SBase that = this;
        dRequest.get(new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                DMobi.log("REQUEST", respondString);
                SingleObjectResponse<DMobileModelBase> response = DbHelper.parseSingleObjectResponse(respondString, itemClass);
                if (response != null) {
                    if (response.isSuccessfully()) {
                        if (response.hasData()) {
                            complete.onComplete(true, response.data);
                        } else {
                            if (complete != null) {
                                complete.onComplete(false, null);
                            }
                        }
                    } else {
                        DMobi.showToast(response.getErrors());
                        if (complete != null) {
                            complete.onComplete(false, null);
                        }
                    }
                } else {
                    if (complete != null) {
                        complete.onComplete(false, null);
                    }
                }
            }
        }, new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object error) {
                if (complete != null) {
                    complete.onComplete(false, null);
                    DMobi.showToast("Network error!");
                }
            }
        });
    }
}
