package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.*;

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
    
    @SerializedName("estimate_time_display")
    public String estimateTimeDisplay ;
    

}