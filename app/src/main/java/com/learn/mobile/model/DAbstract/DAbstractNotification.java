package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;

public class DAbstractNotification extends DMobileModelBase {
    
    
    public String _primaryKey = "notification_id";
    

    @SerializedName("notification_id")
    public int notificationId ;
    
    @SerializedName("type_id")
    public String typeId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("owner_user_id")
    public int ownerUserId = 0;
    
    @SerializedName("is_seen")
    public int isSeen = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("time")
    public String time ;
    
    @SerializedName("user")
    public String user ;
    
    @SerializedName("content")
    public String content ;
    
    @SerializedName("display_time")
    public String displayTime ;
    

}