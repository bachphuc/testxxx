package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BachPhuc on 3/24/2016.
 */
public class DAttachment extends DMobileModelBase {
    @SerializedName("attachments")
    public List<DMobileModelBase> attachments;

    public DAttachment() {

    }

    public int getAttachmentCount() {
        if (attachments == null) {
            return 0;
        }
        return attachments.size();
    }

    public DMobileModelBase getItem(int position) {
        if (attachments == null) {
            return null;
        }
        if (position > getAttachmentCount() - 1) {
            return null;
        }
        return attachments.get(position);
    }
}
