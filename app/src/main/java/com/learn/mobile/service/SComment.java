package com.learn.mobile.service;

import com.learn.mobile.model.Comment;

/**
 * Created by 09520_000 on 10/4/2015.
 */
public class SComment extends SBase{
    public SComment(){
        itemClass = Comment.class;
        getDataByPage = true;
    }
}
