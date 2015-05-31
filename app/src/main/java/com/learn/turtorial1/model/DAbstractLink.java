package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractLink extends DmobileModelBase{
    
    
    public String _primaryKey = "link_id";
    

    @SerializedName("link_id")
    public int linkId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("item_id")
    public int itemId = 0;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("is_custom")
    public Boolean isCustom = false;
    
    @SerializedName("link")
    public String link ;
    
    @SerializedName("image")
    public String image ;
    
    @SerializedName("title")
    public String title ;
    
    @SerializedName("description")
    public String description ;
    
    @SerializedName("status_info")
    public String statusInfo ;
    
    @SerializedName("privacy")
    public Boolean privacy ;
    
    @SerializedName("privacy_comment")
    public Boolean privacyComment ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("has_embed")
    public Boolean hasEmbed = false;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("site")
    public String site ;
    
    @SerializedName("site_url")
    public String siteUrl ;
    

}