package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DAttachment;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

public class DAbstractFeed extends DMobileModelBase {
    
    
    public String _primaryKey = "feed_id";
    

    @SerializedName("feed_id")
    public int feedId ;
    
    @SerializedName("app_id")
    public int appId = 0;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("type_id")
    public String typeId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("feed_reference")
    public int feedReference = 0;
    
    @SerializedName("parent_feed_id")
    public int parentFeedId = 0;
    
    @SerializedName("parent_module_id")
    public String parentModuleId ;
    
    @SerializedName("time_update")
    public int timeUpdate = 0;
    
    @SerializedName("is_delete")
    public int isDelete = 0;
    
    @SerializedName("expire_time")
    public int expireTime = 0;
    
    @SerializedName("time")
    public String time ;
    
    @SerializedName("user")
    public User user ;
    
    @SerializedName("item")
    public DMobileModelBase item ;
    
    @SerializedName("content")
    public String content ;
    
    @SerializedName("attachments")
    public DAttachment attachments ;
    
    @SerializedName("header")
    public String header ;
    

}