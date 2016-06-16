package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.*;

public class DAbstractUserStatus extends DMobileModelBase{
    
    
    public String _primaryKey = "status_id";
    

    @SerializedName("status_id")
    public int statusId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("content")
    public String content ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("location_latlng")
    public String locationLatlng ;
    
    @SerializedName("location_name")
    public String locationName ;
    

}