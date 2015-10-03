package com.learn.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractMusicSong extends DMobileModelBase{
    
    
    public String _primaryKey = "song_id";
    

    @SerializedName("song_id")
    public int songId ;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("privacy_comment")
    public int privacyComment = 0;
    
    @SerializedName("is_featured")
    public int isFeatured = 0;
    
    @SerializedName("is_sponsor")
    public int isSponsor = 0;
    
    @SerializedName("album_id")
    public int albumId ;
    
    @SerializedName("genre_id")
    public int genreId = 0;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("song_path")
    public String songPath ;
    
    @SerializedName("explicit")
    public int explicit ;
    
    @SerializedName("duration")
    public String duration ;
    
    @SerializedName("ordering")
    public int ordering = 0;
    
    @SerializedName("total_play")
    public int totalPlay = 0;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
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