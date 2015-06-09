package com.learn.turtorial1.model;

import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.squareup.picasso.Picasso;

public class Photo extends DAbstractPhoto {
    public Photo() {
        feedLayout = R.layout.feed_photo_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        final ImageView imageView;
        if (images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.main_image);

            ViewTreeObserver vto = imageView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int newHeight = imageView.getMeasuredWidth() * images.large.height / images.large.width;
                    imageView.getLayoutParams().height = newHeight;
                    return true;
                }
            });

            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(images.large.url).into(imageView);
            }
        }
    }
}