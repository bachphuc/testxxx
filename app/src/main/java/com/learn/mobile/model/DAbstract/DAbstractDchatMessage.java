package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.DchatRoom;
import com.learn.mobile.model.User;

public class DAbstractDchatMessage extends DMobileModelBase {
    
    
    public String _primaryKey = "message_id";
    

    @SerializedName("message_id")
    public int messageId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("room_id")
    public int roomId ;
    
    @SerializedName("message")
    public String message ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("attachment_type")
    public String attachmentType ;
    
    @SerializedName("attachment_id")
    public int attachmentId ;
    
    @SerializedName("user")
    public User user ;
    
    @SerializedName("room")
    public DchatRoom room ;
    
    @SerializedName("attachment")
    public String attachment ;
    
    @SerializedName("bMine")
    public String bMine ;
    
    public User getUser(){
        return user;
    }
    public DchatRoom getRoom(){
        return room;
    }
}