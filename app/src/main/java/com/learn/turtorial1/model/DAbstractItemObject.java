package com.learn.turtorial1.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by 09520_000 on 5/28/2015.
 */

public class DAbstractItemObject {
    protected String itemType;
    protected String jsonData;

    public DmobileModelBase getItem() {
		if(itemType == null || jsonData == null){
			return null;
		}
        Gson gson = new GsonBuilder().create();
        Type type;

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
                return new DmobileModelBase();
        }
    }
}
