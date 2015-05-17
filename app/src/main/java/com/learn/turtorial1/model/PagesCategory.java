package com.learn.turtorial1.model;

import com.google.gson.annotations.SerializedName;

public class PagesCategory extends DmobileModelBase{
    
    
    public String _primaryKey = "category_id";
    

    @SerializedName("category_id")
    public int categoryId ;
    
    @SerializedName("type_id")
    public int typeId ;
    
    @SerializedName("name")
    public String name ;
    
    @SerializedName("page_type")
    public Boolean pageType = false;
    
    @SerializedName("is_active")
    public Boolean isActive = true;
    
    @SerializedName("ordering")
    public int ordering = 0;
    

}