package com.learn.turtorial1.model;

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
        Gson gson = new GsonBuilder().create();
        Type type;
        switch (itemType) {
		case "User":
			type = new TypeToken<User>() {
			}.getType();
			break;
		case "Feed":
			type = new TypeToken<Feed>() {
			}.getType();
			break;
		case "Photo":
			type = new TypeToken<Photo>() {
			}.getType();
			break;
		case "Pages":
			type = new TypeToken<Pages>() {
			}.getType();
			break;
		case "PagesCategory":
			type = new TypeToken<PagesCategory>() {
			}.getType();
			break;
		case "Blog":
			type = new TypeToken<Blog>() {
			}.getType();
			break;
		case "UserStatus":
			type = new TypeToken<UserStatus>() {
			}.getType();
			break;
		case "Link":
			type = new TypeToken<Link>() {
			}.getType();
			break;
		case "MusicSong":
			type = new TypeToken<MusicSong>() {
			}.getType();
			break;
		case "Video":
			type = new TypeToken<Video>() {
			}.getType();
			break;
            default:
                return new DmobileModelBase();
        }

        Blog obj = gson.fromJson(jsonData, type);

        return obj;
    }
}
