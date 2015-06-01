package com.learn.turtorial1.model;

import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class DmobileModelBase {
    public String title;
    public String description;
    public ImageObject images;

    public int feedLayout = 0;

    public DmobileModelBase() {
        feedLayout = R.layout.feed_basic_layout;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ImageObject getImages() {
        return images;
    }

    public int getFeedLayout() {
        return feedLayout;
    }

    public void processFeedViewHolder(FeedViewHolder feedViewHolder){
    }
}
