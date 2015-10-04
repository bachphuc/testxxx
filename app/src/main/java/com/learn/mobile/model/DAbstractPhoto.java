package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractPhoto extends DMobileModelBase{
    
    
    public String _primaryKey = "photo_id";
    

    @SerializedName("photo_id")
    public int photoId ;
    
    @SerializedName("album_id")
    public int albumId = 0;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("group_id")
    public int groupId = 0;
    
    @SerializedName("type_id")
    public int typeId = 0;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("destination")
    public String destination ;
    
    @SerializedName("mature")
    public int mature = 0;
    
    @SerializedName("allow_comment")
    public int allowComment = 0;
    
    @SerializedName("allow_rate")
    public int allowRate = 0;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("total_view")
    public int totalView = 0;
    
    @SerializedName("total_download")
    public int totalDownload = 0;
    
    @SerializedName("total_rating")
    public int totalRating = 0;
    
    @SerializedName("total_vote")
    public int totalVote = 0;
    
    @SerializedName("total_battle")
    public int totalBattle = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("is_featured")
    public int isFeatured = 0;
    
    @SerializedName("is_cover")
    public int isCover = 0;
    
    @SerializedName("allow_download")
    public int allowDownload = 0;
    
    @SerializedName("is_sponsor")
    public int isSponsor = 0;
    
    @SerializedName("ordering")
    public int ordering = 0;
    
    @SerializedName("is_profile_photo")
    public int isProfilePhoto = 0;
    
    @SerializedName("user")
    public User user ;
    
    public User getUser(){
        return user;
    }
}