package com.learn.turtorial1.service;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.turtorial1.library.dmobi.DMobi;
import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.learn.turtorial1.library.dmobi.request.DRequest;
import com.learn.turtorial1.library.dmobi.request.Dresponse;
import com.learn.turtorial1.library.dmobi.request.response.SingleObjectResponse;
import com.learn.turtorial1.model.User;

import java.lang.reflect.Type;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SUser extends SBase {

    User user;

    public void setUser(User oUser){
        user = oUser;
    }

    public User getUser(){
        return user;
    }

    public void clearUser(){
        user = null;
    }

    public void logout(){
        user = null;
        DConfig.clearToken();
    }

    public void login(String email, String password, final Dresponse.Complete complete) {
        DRequest dRequest = DMobi.getRequest();
        dRequest.setApi("user.login");
        dRequest.addParam("login", email);
        dRequest.addParam("password", password);

        final SUser that = this;
        dRequest.get(new Dresponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<SingleObjectResponse<LoginObjectResponseData>>() {
                }.getType();

                SingleObjectResponse<LoginObjectResponseData> response = gson.fromJson(respondString, type);
                if(response.isSuccessfully()){
                    DConfig.setToken(response.data.token);
                    user = response.data.user;
                    complete.onComplete(response.data.user);
                }
                else{
                    DMobi.showToast(response.getErrors());
                    complete.onComplete(null);
                }
            }
        }, new Dresponse.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError var1) {
                if (complete != null) {
                    complete.onComplete(null);
                    DMobi.showToast("Network error!");
                }
            }
        });
    }

    class LoginObjectResponseData{
        public String token;
        public User user;
    }
}
