package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractMusicSong extends DmobileModelBase{
    
    
    public String _primaryKey = "song_id";
    

    @SerializedName("song_id")
    public int songId ;
    
    @SerializedName("view_id")
    public Boolean viewId = false;
    
    @SerializedName("privacy")
    public Boolean privacy = false;
    
    @SerializedName("privacy_comment")
    public Boolean privacyComment = false;
    
    @SerializedName("is_featured")
    public Boolean isFeatured = false;
    
    @SerializedName("is_sponsor")
    public Boolean isSponsor = false;
    
    @SerializedName("album_id")
    public int albumId ;
    
    @SerializedName("genre_id")
    public int genreId = 0;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("title")
    public String title ;
    
    @SerializedName("description")
    public String description ;
    
    @SerializedName("song_path")
    public String songPath ;
    
    @SerializedName("server_id")
    public Boolean serverId = false;
    
    @SerializedName("explicit")
    public Boolean explicit ;
    
    @SerializedName("duration")
    public String duration ;
    
    @SerializedName("ordering")
    public Boolean ordering = false;
    
    @SerializedName("total_play")
    public int totalPlay = 0;
    
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
    public int timeStamp ;
    
    @SerializedName("module_id")
    public String moduleId ;
    
    @SerializedName("item_id")
    public int itemId = 0;
    

}