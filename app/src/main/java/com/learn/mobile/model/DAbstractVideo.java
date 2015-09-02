package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractVideo extends DmobileModelBase{
    
    
    public String _primaryKey = "video_id";
    

    @SerializedName("video_id")
    public int videoId ;
    
    @SerializedName("in_process")
    public int inProcess = 0;
    
    @SerializedName("is_stream")
    public int isStream = 0;
    
    @SerializedName("is_featured")
    public int isFeatured = 0;
    
    @SerializedName("is_spotlight")
    public int isSpotlight = 0;
    
    @SerializedName("is_sponsor")
    public int isSponsor = 0;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("item_id")
    public int itemId = 0;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("user_id")
    public int userId = 0;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("destination")
    public String destination ;
    
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
    public int isViewed = 0;
    
    @SerializedName("custom_v_id")
    public int customVId = 0;
    
    @SerializedName("image")
    public String image ;
    

}