package com.learn.mobile.model.DAbstract;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.model.*;

public class DAbstractPagesCategory extends DMobileModelBase{
    
    
    public String _primaryKey = "category_id";
    

    @SerializedName("category_id")
    public int categoryId ;
    
    @SerializedName("type_id")
    public int typeId ;
    
    @SerializedName("name")
    public String name ;
    
    @SerializedName("page_type")
    public int pageType = 0;
    
    @SerializedName("is_active")
    public int isActive = 1;
    
    @SerializedName("ordering")
    public int ordering = 0;
    

}