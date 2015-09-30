package com.learn.mobile.service;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.User;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SUser extends SBase {

    User user;

    public SUser(){
        itemClass = User.class;
    }

    public void setUser(User oUser) {
        user = oUser;
    }

    public User getUser() {
        if (!DMobi.isUser()) {
            return null;
        }
        if (user != null) {
            return user;
        }

        String jsonData = DConfig.getUserData();
        if (jsonData != null) {
            user = (User) DbHelper.parseObject(jsonData, User.class);
        }
        return user;
    }

    public void clearUser() {
        user = null;
    }

    public String getUserJsonData() {
        if (user == null) {
            return "";
        }
        String jsonData = DbHelper.toJsonString(user);
        return jsonData;
    }

    public void logout() {
        user = null;
        DConfig.clearToken();
        DConfig.clearUserData();
    }

    public void login(String email, String password, final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("user.login");
        dRequest.addParam("login", email);
        dRequest.addParam("password", password);

        final SUser that = this;
        dRequest.get(new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<SingleObjectResponse<LoginObjectResponseData>>() {
                }.getType();

                SingleObjectResponse<LoginObjectResponseData> response = gson.fromJson(respondString, type);
                if (response.isSuccessfully()) {
                    DConfig.setToken(response.data.token);

                    user = response.data.user;
                    DConfig.saveUserData(getUserJsonData());
                    complete.onComplete(true, response.data.user);
                } else {
                    DMobi.showToast(response.getErrors());
                    complete.onComplete(false, null);
                }
            }
        }, new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object var1) {
                if (complete != null) {
                    complete.onComplete(false, null);
                    DMobi.showToast("Network error!");
                }
            }
        });
    }

    class LoginObjectResponseData {
        public String token;
        public User user;
    }
}
