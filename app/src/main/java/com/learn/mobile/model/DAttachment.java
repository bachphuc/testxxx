package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BachPhuc on 3/24/2016.
 */
public class DAttachment extends DMobileModelBase {
    @SerializedName("attachment_obj")
    public List<DAbstractItemObject> attachmentObj;

    public List<DMobileModelBase> attachments;

    public DAttachment() {

    }

    public void convertToAttachment() {
        if (attachments != null) {
            return;
        }

        if (attachmentObj != null) {
            attachments = new ArrayList<>();
            DMobileModelBase item;
            for (int i = 0; i < attachmentObj.size(); i++) {
                item = attachmentObj.get(i).getItem();
                attachments.add(item);
            }
        }
    }
}
