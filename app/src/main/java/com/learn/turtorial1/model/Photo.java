package com.learn.turtorial1.model;

import android.util.Log;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.learn.turtorial1.FeedAdapter;
import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

public class Photo extends DAbstractPhoto {

    public Photo() {
        feedLayout = R.layout.feed_photo_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        ImageView imageView;
        if (images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.main_image);

            Picasso.with(imageView.getContext()).load(images.full).into(imageView);
        }
    }
}