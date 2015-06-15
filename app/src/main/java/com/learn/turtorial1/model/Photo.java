package com.learn.turtorial1.model;

import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.learn.turtorial1.customview.DFeedImageView;
import com.squareup.picasso.Picasso;

import net.grobas.view.PolygonImageView;

public class Photo extends DAbstractPhoto {
    public Photo() {
        feedLayout = R.layout.feed_photo_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        final DFeedImageView imageView;
        if (images != null) {
            imageView = (DFeedImageView) feedViewHolder.findView(R.id.main_image);

            float ratio = (float)images.large.height / (float)images.large.width;
            Log.i("Photo ratio", "" + ratio);
            imageView.setScale(ratio);

            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(images.large.url).into(imageView);
            }
        }

    }
}