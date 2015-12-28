package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractProjectTask extends DMobileModelBase{
    
    
    public String _primaryKey = "task_id";
    

    @SerializedName("task_id")
    public int taskId ;
    
    @SerializedName("project_id")
    public int projectId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("estimate_time")
    public int estimateTime = 0;
    
    @SerializedName("total_percent")
    public int totalPercent = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    

}