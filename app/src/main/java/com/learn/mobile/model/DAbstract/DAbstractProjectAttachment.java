package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.*;

public class DAbstractProjectAttachment extends DMobileModelBase{
    
    
    public String _primaryKey = "attachment_id";
    

    @SerializedName("attachment_id")
    public int attachmentId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("parent_type")
    public String parentType ;
    
    @SerializedName("parent_id")
    public int parentId ;
    
    @SerializedName("type")
    public String type = "file";
    
    @SerializedName("path")
    public String path ;
    
    @SerializedName("extension")
    public String extension ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    

}