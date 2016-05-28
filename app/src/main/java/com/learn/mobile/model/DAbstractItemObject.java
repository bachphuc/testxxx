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
            case "Notification":
                type = new TypeToken<Notification>() {
                }.getType();
                Notification objNotification = gson.fromJson(jsonData, type);
                return objNotification;
            case "Project":
                type = new TypeToken<Project>() {
                }.getType();
                Project objProject = gson.fromJson(jsonData, type);
                return objProject;
            case "ProjectTask":
                type = new TypeToken<ProjectTask>() {
                }.getType();
                ProjectTask objProjectTask = gson.fromJson(jsonData, type);
                return objProjectTask;
            case "ProjectAttachment":
                type = new TypeToken<ProjectAttachment>() {
                }.getType();
                ProjectAttachment objProjectAttachment = gson.fromJson(jsonData, type);
                return objProjectAttachment;
            case "Funny":
                type = new TypeToken<Funny>() {
                }.getType();
                Funny objFunny = gson.fromJson(jsonData, type);
                return objFunny;
            case "Search":
                type = new TypeToken<Search>() {
                }.getType();
                Search objSearch = gson.fromJson(jsonData, type);
                return objSearch;
            case "Dchat":
                type = new TypeToken<Dchat>() {
                }.getType();
                Dchat objDchat = gson.fromJson(jsonData, type);
                return objDchat;
            case "DchatMessage":
                type = new TypeToken<DchatMessage>() {
                }.getType();
                DchatMessage objDchatMessage = gson.fromJson(jsonData, type);
                return objDchatMessage;
            case "DchatRoom":
                type = new TypeToken<DchatRoom>() {
                }.getType();
                DchatRoom objDchatRoom = gson.fromJson(jsonData, type);
                return objDchatRoom;
            case "DchatMember":
                type = new TypeToken<DchatMember>() {
                }.getType();
                DchatMember objDchatMember = gson.fromJson(jsonData, type);
                return objDchatMember;
            case "DchatPhoto":
                type = new TypeToken<DchatPhoto>() {
                }.getType();
                DchatPhoto objDchatPhoto = gson.fromJson(jsonData, type);
                return objDchatPhoto;
            case "DchatRequest":
                type = new TypeToken<DchatRequest>() {
                }.getType();
                DchatRequest objDchatRequest = gson.fromJson(jsonData, type);
                return objDchatRequest;
            default:
                return new DMobileModelBase();
        }
    }

    public Type getType() {
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
            case "Notification":
                type = new TypeToken<Notification>() {
                }.getType();
                return type;
            case "Project":
                type = new TypeToken<Project>() {
                }.getType();
                return type;
            case "ProjectTask":
                type = new TypeToken<ProjectTask>() {
                }.getType();
                return type;
            case "ProjectAttachment":
                type = new TypeToken<ProjectAttachment>() {
                }.getType();
                return type;
            case "Funny":
                type = new TypeToken<Funny>() {
                }.getType();
                return type;
            case "Search":
                type = new TypeToken<Search>() {
                }.getType();
                return type;
            case "Dchat":
                type = new TypeToken<Dchat>() {
                }.getType();
                return type;
            case "DchatMessage":
                type = new TypeToken<DchatMessage>() {
                }.getType();
                return type;
            case "DchatRoom":
                type = new TypeToken<DchatRoom>() {
                }.getType();
                return type;
            case "DchatMember":
                type = new TypeToken<DchatMember>() {
                }.getType();
                return type;
            case "DchatPhoto":
                type = new TypeToken<DchatPhoto>() {
                }.getType();
                return type;
            case "DchatRequest":
                type = new TypeToken<DchatRequest>() {
                }.getType();
                return type;
            default:
                return null;
        }
    }

    public Type getListType() {
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
            case "Notification":
                type = new TypeToken<ListObjectResponse<Notification>>() {
                }.getType();
                return type;
            case "Project":
                type = new TypeToken<ListObjectResponse<Project>>() {
                }.getType();
                return type;
            case "ProjectTask":
                type = new TypeToken<ListObjectResponse<ProjectTask>>() {
                }.getType();
                return type;
            case "ProjectAttachment":
                type = new TypeToken<ListObjectResponse<ProjectAttachment>>() {
                }.getType();
                return type;
            case "Funny":
                type = new TypeToken<ListObjectResponse<Funny>>() {
                }.getType();
                return type;
            case "Search":
                type = new TypeToken<ListObjectResponse<Search>>() {
                }.getType();
                return type;
            case "Dchat":
                type = new TypeToken<ListObjectResponse<Dchat>>() {
                }.getType();
                return type;
            case "DchatMessage":
                type = new TypeToken<ListObjectResponse<DchatMessage>>() {
                }.getType();
                return type;
            case "DchatRoom":
                type = new TypeToken<ListObjectResponse<DchatRoom>>() {
                }.getType();
                return type;
            case "DchatMember":
                type = new TypeToken<ListObjectResponse<DchatMember>>() {
                }.getType();
                return type;
            case "DchatPhoto":
                type = new TypeToken<ListObjectResponse<DchatPhoto>>() {
                }.getType();
                return type;
            case "DchatRequest":
                type = new TypeToken<ListObjectResponse<DchatRequest>>() {
                }.getType();
                return type;
            default:
                return null;
        }
    }

    public Type getSingleType() {
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
            case "Notification":
                type = new TypeToken<SingleObjectResponse<Notification>>() {
                }.getType();
                return type;
            case "Project":
                type = new TypeToken<SingleObjectResponse<Project>>() {
                }.getType();
                return type;
            case "ProjectTask":
                type = new TypeToken<SingleObjectResponse<ProjectTask>>() {
                }.getType();
                return type;
            case "ProjectAttachment":
                type = new TypeToken<SingleObjectResponse<ProjectAttachment>>() {
                }.getType();
                return type;
            case "Funny":
                type = new TypeToken<SingleObjectResponse<Funny>>() {
                }.getType();
                return type;
            case "Search":
                type = new TypeToken<SingleObjectResponse<Search>>() {
                }.getType();
                return type;
            case "Dchat":
                type = new TypeToken<SingleObjectResponse<Dchat>>() {
                }.getType();
                return type;
            case "DchatMessage":
                type = new TypeToken<SingleObjectResponse<DchatMessage>>() {
                }.getType();
                return type;
            case "DchatRoom":
                type = new TypeToken<SingleObjectResponse<DchatRoom>>() {
                }.getType();
                return type;
            case "DchatMember":
                type = new TypeToken<SingleObjectResponse<DchatMember>>() {
                }.getType();
                return type;
            case "DchatPhoto":
                type = new TypeToken<SingleObjectResponse<DchatPhoto>>() {
                }.getType();
                return type;
            case "DchatRequest":
                type = new TypeToken<SingleObjectResponse<DchatRequest>>() {
                }.getType();
                return type;
            default:
                return null;
        }
    }
}
