package com.learn.turtorial1.model;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Feed extends DAbstractFeed {
    private boolean bItemReady = false;

    public DmobileModelBase getItem() {
        if (item != null && bItemReady == true) {
            return item;
        }
        if (itemData != null) {
            item = itemData.getItem();
        }
        bItemReady = true;

        return item;
    }

    @Override
    public int getFeedLayout() {
        DmobileModelBase dmobileModelBase = getItem();
        if (dmobileModelBase != null) {
            return dmobileModelBase.getFeedLayout();
        }
        return R.layout.feed_basic_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);

        TextView textView = (TextView) feedViewHolder.findView(R.id.tvTitle);
        textView.setText(user.getTitle());
        textView = (TextView) feedViewHolder.findView(R.id.tvDescription);
        textView.setText(content);

        ImageView imageView;
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(64)).build();

        if (user.images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.imageViewAvatar);
            ImageLoader.getInstance().displayImage(user.images.full, imageView, displayImageOptions);
        }

        DmobileModelBase dmobileModelBase = getItem();
        if (dmobileModelBase != null) {
            dmobileModelBase.processFeedViewHolder(feedViewHolder);
        }
    }
}