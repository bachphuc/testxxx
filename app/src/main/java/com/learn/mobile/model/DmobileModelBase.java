package com.learn.mobile.model;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class DMobileModelBase {
    public String title;
    public String description;
    public int id;
    public String item_type;
    public ImageObject images;

    public int feedLayout = 0;
    public int layout = 0;

    public DMobileModelBase() {
        feedLayout = R.layout.feed_basic_layout;
    }

    public String getTitle() {
        return (title != null ? title : "");
    }

    public String getDescription() {
        return (description != null ? description : "");
    }

    public int getId() {
        return id;
    }

    public String getItemType() {
        return item_type;
    }

    public ImageObject getImages() {
        return images;
    }

    public int getFeedLayout() {
        return feedLayout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getLayout() {
        return layout;
    }

    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
    }

    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
    }
}
