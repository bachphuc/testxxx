package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractProject extends DMobileModelBase{
    
    
    public String _primaryKey = "project_id";
    

    @SerializedName("project_id")
    public int projectId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("status")
    public int status = 0;
    
    @SerializedName("complete_percent")
    public int completePercent = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    

}