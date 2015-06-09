package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractPages extends DmobileModelBase{
    
    
    public String _primaryKey = "page_id";
    

    @SerializedName("page_id")
    public int pageId ;
    
    @SerializedName("app_id")
    public int appId = 0;
    
    @SerializedName("view_id")
    public Boolean viewId = false;
    
    @SerializedName("type_id")
    public int typeId ;
    
    @SerializedName("category_id")
    public int categoryId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("reg_method")
    public Boolean regMethod = false;
    
    @SerializedName("landing_page")
    public String landingPage ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("image_path")
    public String imagePath ;
    
    @SerializedName("total_like")
    public int totalLike = 0;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("total_comment")
    public int totalComment = 0;
    
    @SerializedName("privacy")
    public Boolean privacy = false;
    
    @SerializedName("designer_style_id")
    public int designerStyleId = 0;
    
    @SerializedName("cover_photo_id")
    public int coverPhotoId ;
    
    @SerializedName("cover_photo_position")
    public String coverPhotoPosition ;
    
    @SerializedName("location_latitude")
    public int locationLatitude ;
    
    @SerializedName("location_longitude")
    public int locationLongitude ;
    
    @SerializedName("location_name")
    public String locationName ;
    
    @SerializedName("use_timeline")
    public Boolean useTimeline = false;
    
    @SerializedName("text")
    public String text ;
    
    @SerializedName("category")
    public PagesCategory category ;
    
    @SerializedName("user")
    public User user ;
    
    public PagesCategory getCategory(){
        return category;
    }
    public User getUser(){
        return user;
    }
}