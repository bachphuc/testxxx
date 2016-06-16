package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.*;

public class DAbstractDchatRoom extends DMobileModelBase{
    
    
    public String _primaryKey = "room_id";
    

    @SerializedName("room_id")
    public int roomId ;
    
    @SerializedName("room_name")
    public String roomName ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("last_message")
    public String lastMessage ;
    

}