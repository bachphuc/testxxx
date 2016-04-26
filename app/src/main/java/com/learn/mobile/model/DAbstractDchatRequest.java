package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractDchatRequest extends DMobileModelBase{
    
    
    public String _primaryKey = "request_id";
    

    @SerializedName("request_id")
    public int requestId ;
    
    @SerializedName("room_id")
    public int roomId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("request_user_id")
    public int requestUserId ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("status")
    public int status = 0;
    
    @SerializedName("request_user")
    public User requestUser ;
    
    @SerializedName("room")
    public DchatRoom room ;
    
    public User getRequestUser(){
        return requestUser;
    }
    public DchatRoom getRoom(){
        return room;
    }
}