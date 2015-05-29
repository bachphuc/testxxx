package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class Photo extends DmobileModelBase{
    
    
    public String _primaryKey = "photo_id";
    

    @SerializedName("photo_id")
    public int photoId ;
    
    @SerializedName("album_id")
    public int albumId = 0;
    
    @SerializedName("view_id")
    public Boolean viewId = false;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("group_id")
    public int groupId = 0;
    
    @SerializedName("type_id")
    public Boolean typeId = false;
    
    @SerializedName("privacy")
    public Boolean privacy = false;
    
    @SerializedName("privacy_comment")
    public Boolean privacyComment = false;
    
    @SerializedName("title")
    public String title ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("parent_user_id")
    public int parentUserId = 0;
    
    @SerializedName("destination")
    public String destination ;
    
    @SerializedName("server_id")
    public Boolean serverId ;
    
    @SerializedName("mature")
    public Boolean mature = false;
    
    @SerializedName("allow_comment")
    public Boolean allowComment = false;
    
    @SerializedName("allow_rate")
    public Boolean allowRate = false;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("total_view")
    public int totalView = 0;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
    @SerializedName("total_download")
    public int totalDownload = 0;
    
    @SerializedName("total_rating")
    public int totalRating = 0;
    
    @SerializedName("total_vote")
    public int totalVote = 0;
    
    @SerializedName("total_battle")
    public int totalBattle = 0;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("is_featured")
    public Boolean isFeatured = false;
    
    @SerializedName("is_cover")
    public Boolean isCover = false;
    
    @SerializedName("allow_download")
    public Boolean allowDownload = false;
    
    @SerializedName("is_sponsor")
    public Boolean isSponsor = false;
    
    @SerializedName("ordering")
    public int ordering = 0;
    
    @SerializedName("is_profile_photo")
    public Boolean isProfilePhoto = false;
    
    @SerializedName("user")
    public User user ;
    
    public User getUser(){
        return user;
    }
}