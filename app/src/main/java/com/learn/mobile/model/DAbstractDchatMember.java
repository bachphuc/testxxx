package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractDchatMember extends DMobileModelBase{
    
    
    public String _primaryKey = "member_id";
    

    @SerializedName("member_id")
    public int memberId ;
    
    @SerializedName("room_id")
    public int roomId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("last_message")
    public String lastMessage ;
    

}