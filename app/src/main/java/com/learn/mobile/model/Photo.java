package com.learn.mobile.model;

import android.widget.ImageView;

import com.learn.mobile.FeedViewHolder;
import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

public class Photo extends DAbstractPhoto {
    public Photo() {
        feedLayout = R.layout.feed_photo_layout;
        layout = R.layout.photo_item_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        final DFeedImageView imageView;
        if (images != null) {
            imageView = (DFeedImageView) feedViewHolder.findView(R.id.main_image);

            float ratio = (float)images.large.height / (float)images.large.width;
            imageView.setScale(ratio);

            if (imageView != null) {
                ImageHelper.display(imageView, images.large.url);
            }
        }

    }

    @Override
    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
        super.processViewHolder(itemBaseViewHolder);
        ImageView imageView = (ImageView)itemBaseViewHolder.findView(R.id.img_photo);
        ImageHelper.display(imageView, images.normal.url);
    }
}