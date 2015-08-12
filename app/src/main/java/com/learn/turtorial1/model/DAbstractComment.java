package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractComment extends DmobileModelBase{
    
    
    public String _primaryKey = "comment_id";
    

    @SerializedName("comment_id")
    public int commentId ;
    
    @SerializedName("parent_id")
    public int parentId = 0;
    
    @SerializedName("type_id")
    public String typeId ;
    
    @SerializedName("item_id")
    public int itemId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("owner_user_id")
    public int ownerUserId = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("update_time")
    public int updateTime = 0;
    
    @SerializedName("update_user")
    public String updateUser ;
    
    @SerializedName("rating")
    public String rating ;
    
    @SerializedName("ip_address")
    public String ipAddress ;
    
    @SerializedName("author")
    public String author ;
    
    @SerializedName("author_email")
    public String authorEmail ;
    
    @SerializedName("author_url")
    public String authorUrl ;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("child_total")
    public int childTotal = 0;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("is_like")
    public String isLike ;
    
    @SerializedName("text")
    public String text ;
    
    @SerializedName("time")
    public String time ;
    
    @SerializedName("user")
    public String user ;
    

}