package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.ImageObject;

public class DAbstractUser extends DMobileModelBase {
    
    
    public String _primaryKey = "user_id";
    

    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("profile_page_id")
    public int profilePageId = 0;
    
    @SerializedName("user_group_id")
    public int userGroupId ;
    
    @SerializedName("status_id")
    public int statusId = 0;
    
    @SerializedName("view_id")
    public int viewId = 0;
    
    @SerializedName("user_name")
    public String userName ;
    
    @SerializedName("full_name")
    public String fullName ;
    
    @SerializedName("password")
    public String password ;
    
    @SerializedName("password_salt")
    public String passwordSalt ;
    
    @SerializedName("email")
    public String email ;
    
    @SerializedName("gender")
    public int gender = 0;
    
    @SerializedName("birthday")
    public String birthday ;
    
    @SerializedName("birthday_search")
    public int birthdaySearch = 0;
    
    @SerializedName("country_iso")
    public String countryIso ;
    
    @SerializedName("language_id")
    public String languageId ;
    
    @SerializedName("style_id")
    public int styleId = 0;
    
    @SerializedName("time_zone")
    public String timeZone ;
    
    @SerializedName("dst_check")
    public int dstCheck = 0;
    
    @SerializedName("joined")
    public int joined ;
    
    @SerializedName("last_login")
    public int lastLogin = 0;
    
    @SerializedName("last_activity")
    public int lastActivity = 0;
    
    @SerializedName("user_image")
    public String userImage ;
    
    @SerializedName("hide_tip")
    public int hideTip = 0;
    
    @SerializedName("status")
    public String status ;
    
    @SerializedName("footer_bar")
    public int footerBar = 0;
    
    @SerializedName("invite_user_id")
    public int inviteUserId = 0;
    
    @SerializedName("im_beep")
    public int imBeep = 0;
    
    @SerializedName("im_hide")
    public int imHide = 0;
    
    @SerializedName("is_invisible")
    public int isInvisible = 0;
    
    @SerializedName("total_spam")
    public int totalSpam = 0;
    
    @SerializedName("last_ip_address")
    public String lastIpAddress ;
    
    @SerializedName("feed_sort")
    public int feedSort = 0;
    
    @SerializedName("cover_photo_id")
    public String coverPhotoId ;
    
    @SerializedName("cover_photo")
    public ImageObject coverPhoto ;
    

}