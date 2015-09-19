package com.learn.mobile.library.dmobi.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.request.response.BaseObjectResponse;
import com.learn.mobile.library.dmobi.request.response.BasicObjectResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DAbstractItemObject;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

import java.lang.reflect.Type;

/**
 * Created by 09520_000 on 9/2/2015.
 */
public class DbHelper {
    public static String toJsonString(Object o) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(o);
        return jsonData;
    }

    public static Type getItemType(Class aClass) {
        DAbstractItemObject dAbstractItemObject = new DAbstractItemObject();
        dAbstractItemObject.itemType = aClass.getSimpleName();
        return dAbstractItemObject.getType();
    }

    public static DMobileModelBase parseObject(String jsonData, Class sClass) {
        DAbstractItemObject dAbstractItemObject = new DAbstractItemObject();
        dAbstractItemObject.itemType = sClass.getSimpleName();
        dAbstractItemObject.jsonData = jsonData;
        return dAbstractItemObject.getItem();
    }

    public static ListObjectResponse<DMobileModelBase> parseListObjectResponse(String jsonData, Class sClass) {
        DAbstractItemObject dAbstractItemObject = new DAbstractItemObject();
        dAbstractItemObject.itemType = sClass.getSimpleName();

        Gson gson = new GsonBuilder().create();
        Type type = dAbstractItemObject.getListType();

        ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) gson.fromJson(jsonData, type);
        return response;
    }

    public static BasicObjectResponse parseResponse(String jsonData) {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<BasicObjectResponse>() {
        }.getType();
        BasicObjectResponse baseObjectResponse = (BasicObjectResponse) gson.fromJson(jsonData, type);
        return baseObjectResponse;
    }
}
