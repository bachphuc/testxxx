package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractLink extends DMobileModelBase{
    
    
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
    public int isCustom = 0;
    
    @SerializedName("link")
    public String link ;
    
    @SerializedName("image")
    public String image ;
    
    @SerializedName("status_info")
    public String statusInfo ;
    
    @SerializedName("privacy")
    public int privacy ;
    
    @SerializedName("privacy_comment")
    public int privacyComment ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("has_embed")
    public int hasEmbed = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("site")
    public String site ;
    
    @SerializedName("site_url")
    public String siteUrl ;
    

}