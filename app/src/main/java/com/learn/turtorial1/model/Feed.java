package com.learn.turtorial1.model;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

import net.grobas.view.PolygonImageView;

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

        LinearLayout linearLayout = (LinearLayout)feedViewHolder.findView(R.id.feed_content);
        if(content.length() == 0){
            linearLayout.setVisibility(View.GONE);
        }

        ImageView imageView;
        if (user.images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.imageViewAvatar);
            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(user.images.avatar.url).into(imageView);
                PolygonImageView polygonImageView = (PolygonImageView)imageView;
                polygonImageView.setVertices(0);
            }
        }

        DmobileModelBase dmobileModelBase = getItem();
        if (dmobileModelBase != null) {
            dmobileModelBase.processFeedViewHolder(feedViewHolder);
        }
    }
}