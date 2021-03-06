package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractDchatPhoto extends DMobileModelBase{
    
    
    public String _primaryKey = "photo_id";
    

    @SerializedName("photo_id")
    public int photoId ;
    
    @SerializedName("message_id")
    public int messageId ;
    
    @SerializedName("destination")
    public String destination ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("photo")
    public String photo ;
    

}