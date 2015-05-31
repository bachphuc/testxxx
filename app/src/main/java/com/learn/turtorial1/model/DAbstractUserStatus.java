package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractUserStatus extends DmobileModelBase{
    
    
    public String _primaryKey = "status_id";
    

    @SerializedName("status_id")
    public int statusId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("privacy")
    public Boolean privacy = false;
    
    @SerializedName("privacy_comment")
    public Boolean privacyComment = false;
    
    @SerializedName("content")
    public String content ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("location_latlng")
    public String locationLatlng ;
    
    @SerializedName("location_name")
    public String locationName ;
    

}