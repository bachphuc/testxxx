package com.learn.turtorial1.model;

import android.widget.ImageView;
import android.widget.TextView;

import com.learn.turtorial1.FeedViewHolder;
import com.learn.turtorial1.R;
import com.squareup.picasso.Picasso;

public class Link extends DAbstractLink {
    public Link() {
        feedLayout = R.layout.feed_link_layout;
    }

    @Override
    public void processFeedViewHolder(FeedViewHolder feedViewHolder) {
        super.processFeedViewHolder(feedViewHolder);
        ImageView imageView;
        if (image != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.main_image);
            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(image).into(imageView);
            }
        }

        TextView textView = (TextView) feedViewHolder.findView(R.id.link_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        textView = (TextView) feedViewHolder.findView(R.id.link_description);
        if (textView != null) {
            textView.setText(getDescription());
        }
    }

}