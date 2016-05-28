package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;

public class DAbstractProject extends DMobileModelBase {
    
    
    public String _primaryKey = "project_id";
    

    @SerializedName("project_id")
    public int projectId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("status")
    public String status = "PENDING";
    
    @SerializedName("complete_percent")
    public int completePercent = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("priority")
    public int priority = 0;
    
    @SerializedName("estimate_time")
    public int estimateTime = 0;
    
    @SerializedName("image_path")
    public String imagePath ;
    
    @SerializedName("budget")
    public int budget = 0;
    
    @SerializedName("estimate_time_display")
    public String estimateTimeDisplay ;
    

}