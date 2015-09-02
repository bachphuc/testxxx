package com.learn.mobile.library.dmobi.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DAbstractItemObject;
import com.learn.mobile.model.DmobileModelBase;

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

    public static DmobileModelBase parseObject(String jsonData, Class sClass) {
        DAbstractItemObject dAbstractItemObject = new DAbstractItemObject();
        dAbstractItemObject.itemType = sClass.getSimpleName();
        dAbstractItemObject.jsonData = jsonData;
        return dAbstractItemObject.getItem();
    }

    public static ListObjectResponse<DmobileModelBase> parseListObjectResponse(String jsonData, Class sClass) {
        DAbstractItemObject dAbstractItemObject = new DAbstractItemObject();
        dAbstractItemObject.itemType = sClass.getSimpleName();

        Gson gson = new GsonBuilder().create();
        Type type = dAbstractItemObject.getListType();

        ListObjectResponse<DmobileModelBase> response = (ListObjectResponse<DmobileModelBase>) gson.fromJson(jsonData, type);
        return response;
    }
}
