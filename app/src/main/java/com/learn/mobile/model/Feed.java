package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.FeedDetailActivity;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.adapter.FeedAdapter;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.library.dmobi.request.DResponse;

public class Feed extends DAbstractFeed implements View.OnClickListener {
    private static final String TAG = Feed.class.getSimpleName();
    private boolean bItemReady = false;

    public Feed() {

    }

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

    public int getFeedLayoutType() {
        DMobileModelBase dmobileModelBase = getAttachment();
        if (dmobileModelBase != null) {
            return (int) dmobileModelBase.getLayoutType(LayoutHelper.FEED_LAYOUT);
        }
        return LayoutHelper.FEED_DEFAULT_LAYOUT;
    }

    @Override
    public int getLayoutType(String suffix) {
        return getFeedLayoutType();
    }

    // TODO Process card view in feed recycler view
    @Override
    public void processFeedViewHolder(final ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);

        final FeedAdapter adapter = (FeedAdapter) itemBaseViewHolder.getAdapter();

        DMobileModelBase item = getAttachment();
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(user.getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tvDescription);

        if (DUtils.isEmpty(content)) {
            LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.feed_content);
            linearLayout.setVisibility(View.GONE);
        } else {
            textView.setText(content);
        }

        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_feed_dropdown_menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_feed_action, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mn_delete_feed:
                                adapter.delete(itemBaseViewHolder.getAdapterPosition());
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        if (user.images != null) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_avatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.getAvatar().url);
                imageView.setOnClickListener(this);
            }
        }

        // TODO Check if liked
        imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_like);
        imageView.setOnClickListener(this);
        updateLikeView(imageView);

        if (item != null) {
            item.processFeedViewHolder(itemBaseViewHolder, position);
        }

        // TODO process comment button
        imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_comment);
        if (imageView != null) {
            imageView.setOnClickListener(this);
        }
        textView = (TextView) itemBaseViewHolder.findView(R.id.tb_total_comment);
        if (textView != null) {
            textView.setText(getTotalComment() + " " + DMobi.translate(getTotalComment() > 1 ? "comments" : "comment"));
        }

        imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_share);
        if (imageView != null) {
            imageView.setOnClickListener(this);
        }
    }

    @Override
    public int getTotalComment() {
        DMobileModelBase item = getAttachment();
        if (item == null) {
            return 0;
        }
        return item.getTotalComment();
    }

    @Override
    public boolean isLike() {
        DMobileModelBase item = getAttachment();
        if (item == null) {
            return false;
        }
        return item.isLike();
    }

    @Override
    public int getTotalLike() {
        DMobileModelBase item = getAttachment();
        if (item == null) {
            return 0;
        }
        return item.getTotalLike();
    }

    @Override
    public void setIsLike(boolean b) {
        super.setIsLike(b);
        DMobileModelBase item = getAttachment();
        if (item == null) {
            return;
        }
        item.setIsLike(b);
    }

    @Override
    public void setTotalLike(int totalLike) {
        super.setTotalLike(totalLike);
        DMobileModelBase item = getAttachment();
        if (item == null) {
            return;
        }
        item.setTotalLike(totalLike);
    }

    public void updateLikeView(View v) {
        ImageView imageView = (ImageView) v;
        if (imageView != null) {
            View parent = (View) v.getParent();
            TextView textView = (TextView) parent.findViewById(R.id.tv_total_like);
            if (textView != null) {
                textView.setText(getTotalLike() + " " + DMobi.translate(getTotalLike() > 1 ? "likes" : "like"));
            }
            if (isLike()) {
                imageView.setSelected(true);
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.primary_icon_color), PorterDuff.Mode.SRC_IN);
            } else {
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.feed_icon_action_color), PorterDuff.Mode.MULTIPLY);
                imageView.setSelected(false);
            }
        }
    }

    @Override
    public void onClick(final View v) {
        Intent intent;
        Context context;
        switch (v.getId()) {
            case R.id.bt_like:
                DMobileModelBase item = getAttachment();
                item.like(new DResponse.Complete() {
                    @Override
                    public void onComplete(Boolean status, Object o) {
                        if (status) {

                        } else {

                        }
                    }
                });

                updateLikeView(v);

                break;
            case R.id.img_avatar:
                context = v.getContext();
                intent = new Intent(context, UserProfileActivity.class);
                User currentUser = (User) DMobi.getData(UserProfileActivity.USER_PROFILE);
                boolean canOpen = (currentUser == null ? true : (currentUser.getId() != user.getId() ? true : false));
                if (canOpen) {
                    DMobi.pushData(UserProfileActivity.USER_PROFILE, user);
                    context.startActivity(intent);
                }
                break;
            case R.id.bt_comment:
                context = v.getContext();
                if (context instanceof FeedDetailActivity) {
                    return;
                }
                intent = new Intent(context, FeedDetailActivity.class);
                DMobi.pushData(FeedDetailActivity.FEED_DETAIL, this);
                context.startActivity(intent);
                break;
            case R.id.bt_share:
                share(v.getContext());
                break;
        }
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        processFeedViewHolder(itemBaseViewHolder, position);
    }
}