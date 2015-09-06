package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.FeedViewHolder;
import com.learn.mobile.R;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.squareup.picasso.Picasso;

import net.grobas.view.PolygonImageView;

public class Feed extends DAbstractFeed {
    private boolean bItemReady = false;

    public DmobileModelBase getAttachment() {
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
        DmobileModelBase dmobileModelBase = getAttachment();
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

        LinearLayout linearLayout = (LinearLayout) feedViewHolder.findView(R.id.feed_content);
        if (content.length() == 0) {
            linearLayout.setVisibility(View.GONE);
        }

        ImageView imageView;
        final User currentUser = user;
        if (user.images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.imageViewAvatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.avatar.url);
                PolygonImageView polygonImageView = (PolygonImageView) imageView;
                polygonImageView.setVertices(0);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra(UserProfileActivity.PROFILE_TITLE, currentUser.getTitle());
                        intent.putExtra(UserProfileActivity.PROFILE_AVATAR, currentUser.images.medium.url);

                        context.startActivity(intent);
                    }
                });
            }
        }

        DmobileModelBase dmobileModelBase = getAttachment();
        if (dmobileModelBase != null) {
            dmobileModelBase.processFeedViewHolder(feedViewHolder);
        }
    }
}