package com.learn.mobile.service;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.model.Notification;

/**
 * Created by BachPhuc on 5/28/2016.
 */
public class SNotification extends SBase {
    public SNotification() {
        itemClass = Notification.class;
    }

    public void markAsRead(int id) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("notification.markAsRead");
        dRequest.addParam(ENTRY_ID, id);
        dRequest.get();
    }

    public void markAsRead(Notification notification) {
        markAsRead(notification.getId());
    }
}
