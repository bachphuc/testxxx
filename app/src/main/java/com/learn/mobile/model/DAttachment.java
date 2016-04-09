package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.library.dmobi.DMobi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BachPhuc on 3/24/2016.
 */
public class DAttachment extends DMobileModelBase {
    @SerializedName("attachments")
    public List<DMobileModelBase> attachments;
    private boolean bSaveParent = false;

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

    @Override
    public void saveParent(DMobileModelBase item) {
        if (bSaveParent) {
            return;
        }
        if (attachments == null) {
            return;
        }
        bSaveParent = true;
        for (int i = 0; i < attachments.size(); i++) {
            DMobileModelBase attachment = attachments.get(i);
            attachment.saveParent(item);
        }
    }
}
