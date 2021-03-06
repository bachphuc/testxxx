package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractBlog extends DMobileModelBase{
    
    
    public String _primaryKey = "blog_id";
    

    @SerializedName("blog_id")
    public int blogId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("time_update")
    public int timeUpdate = 0;
    
    @SerializedName("is_approved")
    public int isApproved ;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("post_status")
    public int postStatus ;
    
    @SerializedName("total_attachment")
    public int totalAttachment = 0;
    
    @SerializedName("total_view")
    public int totalView = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("module_id")
    public String moduleId = "blog";
    
    @SerializedName("user")
    public User user ;
    
    public User getUser(){
        return user;
    }
}