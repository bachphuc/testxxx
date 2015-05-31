package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractVideo extends DmobileModelBase{
    
    
    public String _primaryKey = "video_id";
    

    @SerializedName("video_id")
    public int videoId ;
    
    @SerializedName("in_process")
    public Boolean inProcess = false;
    
    @SerializedName("is_stream")
    public Boolean isStream = false;
    
    @SerializedName("is_featured")
    public Boolean isFeatured = false;
    
    @SerializedName("is_spotlight")
    public Boolean isSpotlight = false;
    
    @SerializedName("is_sponsor")
    public Boolean isSponsor = false;
    
    @SerializedName("view_id")
    public Boolean viewId = false;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("item_id")
    public int itemId = 0;
    
    @SerializedName("privacy")
    public Boolean privacy = false;
    
    @SerializedName("privacy_comment")
    public Boolean privacyComment = false;
    
    @SerializedName("title")
    public String title ;
    
    @SerializedName("user_id")
    public int userId = 0;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("destination")
    public String destination ;
    
    @SerializedName("server_id")
    public Boolean serverId = false;
    
    @SerializedName("file_ext")
    public String fileExt ;
    
    @SerializedName("duration")
    public String duration ;
    
    @SerializedName("resolution_x")
    public String resolutionX ;
    
    @SerializedName("resolution_y")
    public String resolutionY ;
    
    @SerializedName("image_path")
    public String imagePath ;
    
    @SerializedName("image_server_id")
    public Boolean imageServerId = false;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("total_score")
    public int totalScore = 0;
    
    @SerializedName("total_rating")
    public int totalRating = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp = 0;
    
    @SerializedName("total_view")
    public int totalView = 0;
    
    @SerializedName("is_viewed")
    public Boolean isViewed = false;
    
    @SerializedName("custom_v_id")
    public int customVId = 0;
    
    @SerializedName("image")
    public String image ;
    

}