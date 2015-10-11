package com.learn.mobile.model;

import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

public class Photo extends DAbstractPhoto {
    public Photo() {
        registerLayout(LayoutHelper.FEED_LAYOUT, R.layout.feed_photo_layout);
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.photo_item_layout);
    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);
        final DFeedImageView imageView;
        if (images != null) {
            imageView = (DFeedImageView) itemBaseViewHolder.findView(R.id.main_image);
            if (imageView != null) {
                float ratio = (float)images.large.height / (float)images.large.width;
                imageView.setScale(ratio);
                ImageHelper.display(imageView, images.large.url);
            }
        }

    }

    @Override
    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(itemBaseViewHolder, position);
        ImageView imageView = (ImageView)itemBaseViewHolder.findView(R.id.img_photo);
        if(imageView != null){
            ImageHelper.display(imageView, images.big.url);
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(getTitle());
    }
}