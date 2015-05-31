package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class DAbstractUser extends DmobileModelBase{
    
    
    public String _primaryKey = "user_id";
    

    @SerializedName("user_id")
    public int userId ;
    
    @SerializedName("profile_page_id")
    public int profilePageId = 0;
    
    @SerializedName("server_id")
    public Boolean serverId = false;
    
    @SerializedName("user_group_id")
    public int userGroupId ;
    
    @SerializedName("status_id")
    public Boolean statusId = false;
    
    @SerializedName("view_id")
    public Boolean viewId = false;
    
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
    public Boolean gender = false;
    
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
    public Boolean dstCheck = false;
    
    @SerializedName("joined")
    public int joined ;
    
    @SerializedName("last_login")
    public int lastLogin = 0;
    
    @SerializedName("last_activity")
    public int lastActivity = 0;
    
    @SerializedName("user_image")
    public String userImage ;
    
    @SerializedName("hide_tip")
    public Boolean hideTip = false;
    
    @SerializedName("status")
    public String status ;
    
    @SerializedName("footer_bar")
    public Boolean footerBar = false;
    
    @SerializedName("invite_user_id")
    public int inviteUserId = 0;
    
    @SerializedName("im_beep")
    public Boolean imBeep = false;
    
    @SerializedName("im_hide")
    public Boolean imHide = false;
    
    @SerializedName("is_invisible")
    public Boolean isInvisible = false;
    
    @SerializedName("total_spam")
    public int totalSpam = 0;
    
    @SerializedName("last_ip_address")
    public String lastIpAddress ;
    
    @SerializedName("feed_sort")
    public Boolean feedSort = false;
    

}