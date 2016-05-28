package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.PagesCategory;
import com.learn.mobile.model.User;

public class DAbstractPages extends DMobileModelBase {
    
    
    public String _primaryKey = "page_id";
    

    @SerializedName("page_id")
    public int pageId ;
    
    @SerializedName("app_id")
    public int appId = 0;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("type_id")
    public int typeId ;
    
    @SerializedName("category_id")
    public int categoryId ;
    
    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("reg_method")
    public int regMethod = 0;
    
    @SerializedName("landing_page")
    public String landingPage ;
    
    @SerializedName("time_stamp")
    public int timeStamp ;
    
    @SerializedName("image_path")
    public String imagePath ;
    
    @SerializedName("total_dislike")
    public int totalDislike = 0;
    
    @SerializedName("privacy")
    public int privacy = 0;
    
    @SerializedName("designer_style_id")
    public int designerStyleId = 0;
    
    @SerializedName("cover_photo_id")
    public int coverPhotoId ;
    
    @SerializedName("cover_photo_position")
    public String coverPhotoPosition ;
    
    @SerializedName("location_latitude")
    public float locationLatitude ;
    
    @SerializedName("location_longitude")
    public float locationLongitude ;
    
    @SerializedName("location_name")
    public String locationName ;
    
    @SerializedName("use_timeline")
    public int useTimeline = 0;
    
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