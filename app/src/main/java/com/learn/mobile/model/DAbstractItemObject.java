package com.learn.mobile.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.library.dmobi.request.response.SingleObjectResponse;

import java.lang.reflect.Type;

/**
 * Created by phuclb on 5/28/2015.
 */

public class DAbstractItemObject {
    public String itemType;
    public String jsonData;

    public DMobileModelBase getItem() {
        Gson gson = new GsonBuilder().create();
        Type type;
        if(itemType == null){
            return null;
        }
        switch (itemType) {
		case "User":
			type = new TypeToken<User>() {
			}.getType();
			User objUser = gson.fromJson(jsonData, type);
			return objUser;
		case "Feed":
			type = new TypeToken<Feed>() {
			}.getType();
			Feed objFeed = gson.fromJson(jsonData, type);
			return objFeed;
		case "Photo":
			type = new TypeToken<Photo>() {
			}.getType();
			Photo objPhoto = gson.fromJson(jsonData, type);
			return objPhoto;
		case "Pages":
			type = new TypeToken<Pages>() {
			}.getType();
			Pages objPages = gson.fromJson(jsonData, type);
			return objPages;
		case "PagesCategory":
			type = new TypeToken<PagesCategory>() {
			}.getType();
			PagesCategory objPagesCategory = gson.fromJson(jsonData, type);
			return objPagesCategory;
		case "Blog":
			type = new TypeToken<Blog>() {
			}.getType();
			Blog objBlog = gson.fromJson(jsonData, type);
			return objBlog;
		case "UserStatus":
			type = new TypeToken<UserStatus>() {
			}.getType();
			UserStatus objUserStatus = gson.fromJson(jsonData, type);
			return objUserStatus;
		case "Link":
			type = new TypeToken<Link>() {
			}.getType();
			Link objLink = gson.fromJson(jsonData, type);
			return objLink;
		case "MusicSong":
			type = new TypeToken<MusicSong>() {
			}.getType();
			MusicSong objMusicSong = gson.fromJson(jsonData, type);
			return objMusicSong;
		case "Video":
			type = new TypeToken<Video>() {
			}.getType();
			Video objVideo = gson.fromJson(jsonData, type);
			return objVideo;
		case "Comment":
			type = new TypeToken<Comment>() {
			}.getType();
			Comment objComment = gson.fromJson(jsonData, type);
			return objComment;
            default:
                return new DMobileModelBase();
        }
    }
    
    public Type getType(){
        Type type;
        switch (itemType) {
        case "User":
            type = new TypeToken<User>() {
            }.getType();
            return type;
        case "Feed":
            type = new TypeToken<Feed>() {
            }.getType();
            return type;
        case "Photo":
            type = new TypeToken<Photo>() {
            }.getType();
            return type;
        case "Pages":
            type = new TypeToken<Pages>() {
            }.getType();
            return type;
        case "PagesCategory":
            type = new TypeToken<PagesCategory>() {
            }.getType();
            return type;
        case "Blog":
            type = new TypeToken<Blog>() {
            }.getType();
            return type;
        case "UserStatus":
            type = new TypeToken<UserStatus>() {
            }.getType();
            return type;
        case "Link":
            type = new TypeToken<Link>() {
            }.getType();
            return type;
        case "MusicSong":
            type = new TypeToken<MusicSong>() {
            }.getType();
            return type;
        case "Video":
            type = new TypeToken<Video>() {
            }.getType();
            return type;
        case "Comment":
            type = new TypeToken<Comment>() {
            }.getType();
            return type;
            default:
                return null;
        }
    }
    
    public Type getListType(){
        Type type;
        switch (itemType) {
        case "User":
            type = new TypeToken<ListObjectResponse<User>>() {
            }.getType();
            return type;
        case "Feed":
            type = new TypeToken<ListObjectResponse<Feed>>() {
            }.getType();
            return type;
        case "Photo":
            type = new TypeToken<ListObjectResponse<Photo>>() {
            }.getType();
            return type;
        case "Pages":
            type = new TypeToken<ListObjectResponse<Pages>>() {
            }.getType();
            return type;
        case "PagesCategory":
            type = new TypeToken<ListObjectResponse<PagesCategory>>() {
            }.getType();
            return type;
        case "Blog":
            type = new TypeToken<ListObjectResponse<Blog>>() {
            }.getType();
            return type;
        case "UserStatus":
            type = new TypeToken<ListObjectResponse<UserStatus>>() {
            }.getType();
            return type;
        case "Link":
            type = new TypeToken<ListObjectResponse<Link>>() {
            }.getType();
            return type;
        case "MusicSong":
            type = new TypeToken<ListObjectResponse<MusicSong>>() {
            }.getType();
            return type;
        case "Video":
            type = new TypeToken<ListObjectResponse<Video>>() {
            }.getType();
            return type;
        case "Comment":
            type = new TypeToken<ListObjectResponse<Comment>>() {
            }.getType();
            return type;
            default:
                return null;
        }
    }
    
    public Type getSingleType(){
        Type type;
        switch (itemType) {
        case "User":
            type = new TypeToken<SingleObjectResponse<User>>() {
            }.getType();
            return type;
        case "Feed":
            type = new TypeToken<SingleObjectResponse<Feed>>() {
            }.getType();
            return type;
        case "Photo":
            type = new TypeToken<SingleObjectResponse<Photo>>() {
            }.getType();
            return type;
        case "Pages":
            type = new TypeToken<SingleObjectResponse<Pages>>() {
            }.getType();
            return type;
        case "PagesCategory":
            type = new TypeToken<SingleObjectResponse<PagesCategory>>() {
            }.getType();
            return type;
        case "Blog":
            type = new TypeToken<SingleObjectResponse<Blog>>() {
            }.getType();
            return type;
        case "UserStatus":
            type = new TypeToken<SingleObjectResponse<UserStatus>>() {
            }.getType();
            return type;
        case "Link":
            type = new TypeToken<SingleObjectResponse<Link>>() {
            }.getType();
            return type;
        case "MusicSong":
            type = new TypeToken<SingleObjectResponse<MusicSong>>() {
            }.getType();
            return type;
        case "Video":
            type = new TypeToken<SingleObjectResponse<Video>>() {
            }.getType();
            return type;
        case "Comment":
            type = new TypeToken<SingleObjectResponse<Comment>>() {
            }.getType();
            return type;
            default:
                return null;
        }
    }
}
