package com.learn.mobile.service;

import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.BasicObjectResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class SUser extends SBase {
    public static final String SIGNUP_TAG = "SIGNUP_TAG";
    User user;

    HashMap<String, Object> registerData = new HashMap<String, Object>();
    String avatarPath;

    public SUser() {
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
                try {
                    SingleObjectResponse<LoginObjectResponseData> response = gson.fromJson(respondString, type);
                    if (response.isSuccessfully()) {
                        loginSuccess(response, complete);
                    } else {
                        responseError(response, complete);
                    }
                } catch (JsonParseException e) {
                    DMobi.log("Login", respondString);
                    networkError(complete);
                }

            }
        }, new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object var1) {
                networkError(complete);
            }
        });
    }

    public void clearSignupData() {
        registerData.clear();
    }

    public void setRegisterData(HashMap<String, Object> registerData) {
        this.registerData = registerData;
    }

    public void setAvatarPath(String path) {
        avatarPath = path;
    }

    public void register(final DResponse.Complete complete) {
        DRequest dRequest = DMobi.createRequest();
        dRequest.addPosts(registerData);

        dRequest.setApi("user.signup");

        DResponse.Listener listener = new DResponse.Listener() {
            @Override
            public void onResponse(String respondString) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<SingleObjectResponse<LoginObjectResponseData>>() {
                }.getType();
                try {
                    SingleObjectResponse<LoginObjectResponseData> response = gson.fromJson(respondString, type);
                    if (response.isSuccessfully()) {
                        loginSuccess(response, complete);
                    } else {
                        responseError(response, complete);
                    }
                } catch (JsonParseException e) {
                    DMobi.log("Register", respondString);
                    networkError(complete);
                }

            }
        };

        DResponse.ErrorListener errorListener = new DResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Object var1) {
                networkError(complete);
            }
        };
        dRequest.setListener(listener);
        dRequest.setErrorListener(errorListener);

        if (avatarPath == null) {
            dRequest.post();
        } else {
            dRequest.setFilePath(avatarPath);
            dRequest.upload();
        }
    }

    private void loginSuccess(SingleObjectResponse<LoginObjectResponseData> response, DResponse.Complete complete) {
        DConfig.setToken(response.data.token);

        user = response.data.user;
        DConfig.saveUserData(getUserJsonData());
        complete.onComplete(true, response.data.user);
    }

    class LoginObjectResponseData {
        public String token;
        public User user;
    }

    public void updateUser(User user) {
        this.user = user;
        DConfig.saveUserData(getUserJsonData());
        DMobi.fireEvent(Event.EVENT_UPDATE_PROFILE, user);
    }

    public void updateAvatar(String filePath, final DResponse.Complete complete) {
        if (filePath == null) {
            return;
        }
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("user.updateAvatar");
        dRequest.setFilePath(filePath);
        dRequest.setComplete(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    String response = (String) o;
                    DMobi.log("Update Avatar", response);
                    SingleObjectResponse<DMobileModelBase> responseObject = DbHelper.parseSingleObjectResponse(response, User.class);
                    if (responseObject.isSuccessfully()) {
                        User u = (User) responseObject.data;
                        updateUser(u);
                        complete.onComplete(true, o);
                    } else {
                        responseError(responseObject, complete);
                    }
                } else {
                    networkError(complete);
                }
            }
        });
        dRequest.upload();
    }
}
