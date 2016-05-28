package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

public class DAbstractFunny extends DMobileModelBase {
    
    
    public String _primaryKey = "funny_id";
    

    @SerializedName("funny_id")
    public int funnyId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("search_title")
    public String searchTitle ;
    
    @SerializedName("link")
    public String link ;
    
    @SerializedName("image")
    public String image ;
    
    @SerializedName("alias")
    public String alias ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("height")
    public int height = 0;
    
    @SerializedName("width")
    public int width = 0;
    
    @SerializedName("user")
    public User user ;
    
    public User getUser(){
        return user;
    }
}