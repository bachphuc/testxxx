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
}
