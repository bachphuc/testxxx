package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.PostActivity;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

public class Feed extends DAbstractFeed {
    private boolean bItemReady = false;

    public DMobileModelBase getAttachment() {
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
        DMobileModelBase dmobileModelBase = getAttachment();
        if (dmobileModelBase != null) {
            return dmobileModelBase.getFeedLayout();
        }
        return R.layout.feed_basic_layout;
    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
        super.processFeedViewHolder(itemBaseViewHolder);

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tvTitle);
        textView.setText(user.getTitle());
        textView = (TextView) itemBaseViewHolder.findView(R.id.tvDescription);
        if (content != null) {
            textView.setText(content);

            LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.feed_content);
            if (content.length() == 0) {
                linearLayout.setVisibility(View.GONE);
            }
        }

        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_feed_dropdown_menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.feed_action_menu, popup.getMenu());
                popup.show();
            }
        });

        final User currentUser = user;
        if (user.images != null) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.imageViewAvatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.avatar.url);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        DMobi.pushData(UserProfileActivity.USER_PROFILE, currentUser);

                        context.startActivity(intent);
                    }
                });
            }
        }

        DMobileModelBase dmobileModelBase = getAttachment();
        if (dmobileModelBase != null) {
            dmobileModelBase.processFeedViewHolder(itemBaseViewHolder);
        }
    }
}