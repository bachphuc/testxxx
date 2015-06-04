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

public class Photo extends DAbstractPhoto {

    transient DisplayImageOptions displayImageOp;

    transient FeedAdapter.AnimateImageListener animateFirstListener;

    public Photo() {
        feedLayout = R.layout.feed_photo_layout;
        displayImageOp = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        animateFirstListener = new FeedAdapter.AnimateImageListener();
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        ImageView imageView;
        if (images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.main_image);

            ImageLoader.getInstance().displayImage(images.full, imageView, displayImageOp, animateFirstListener);
        }
    }
}