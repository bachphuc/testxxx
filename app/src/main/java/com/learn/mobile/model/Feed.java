package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.learn.mobile.customview.dialog.TutsPlusBottomSheetDialogFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.Constant;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.DAbstract.DAbstractFeed;

import java.util.List;

public class Feed extends DAbstractFeed implements View.OnClickListener {
    private static final String TAG = Feed.class.getSimpleName();

    public Feed() {

    }

    public DMobileModelBase getAttachment() {
        if (item != null) {
            return item;
        }

        return item;
    }

    public DAttachment getAttachments() {
        return attachments;
    }

    public List<DMobileModelBase> getAttachmentItems() {
        if (attachments == null) {
            return null;
        }
        return attachments.attachments;
    }

    public int getFeedLayoutType() {
        DMobileModelBase dmobileModelBase = getAttachment();
        if (dmobileModelBase != null) {
            String type = "";
            if (attachments != null && attachments.getAttachmentCount() > 1) {
                type = "" + (attachments.getAttachmentCount() > Constant.MAX_ATTACHMENTS_SHOW ? Constant.MAX_ATTACHMENTS_SHOW : attachments.getAttachmentCount());
            }
            return dmobileModelBase.getLayoutType(LayoutHelper.FEED_LAYOUT, type);
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

        // todo set text view header
        if (!DUtils.isEmpty(header)) {
            textView = (TextView) itemBaseViewHolder.findView(R.id.tv_sub_header);
            if (textView != null) {
                textView.setText(header);
            }
        }

        // TODO: 5/1/2016 display description
        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_description);
        LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.feed_content);
        if (DUtils.isEmpty(content)) {
            textView.setVisibility(View.GONE);
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            textView.setVisibility(View.VISIBLE);
            if (linearLayout != null) {
                linearLayout.setVisibility(View.VISIBLE);
            }
            textView.setText(content);
        }

        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_feed_dropdown_menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/1/2016 use menu bottom sheet
                TutsPlusBottomSheetDialogFragment bottomSheetDialogFragment = new TutsPlusBottomSheetDialogFragment();
                bottomSheetDialogFragment.setMenuResId(R.menu.menu_feed_action);
                bottomSheetDialogFragment.setMenuTitle(DMobi.translate("Choose your action"));
                bottomSheetDialogFragment.setOnMenuBottomSheetListener(new TutsPlusBottomSheetDialogFragment.OnMenuBottomSheetInteractionListener() {
                    @Override
                    public void onMenuBottomSheetInteraction(int id, String title) {
                        switch (id) {
                            case R.id.mn_delete_feed:
                                adapter.delete(itemBaseViewHolder.getAdapterPosition());
                                break;
                        }
                    }
                });

                showBottomMenu(v.getContext(), bottomSheetDialogFragment);
            }
        });

        if (user.images != null) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_avatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.getAvatar().url);
                imageView.setOnClickListener(this);
            }
        }
        ImageView mainImageView = (ImageView) itemBaseViewHolder.findView(R.id.img_feed_default_image);
        if (mainImageView != null) {
            mainImageView.setVisibility(View.GONE);
        }
        // todo process attachment
        if (item != null) {
            // todo save parent for attachment item
            // item.saveParent(this);
            if (getAttachments() != null && getAttachments().getAttachmentCount() > 1) {
                DAttachment dAttachment = getAttachments();
                // todo save parent for all attachment items
                dAttachment.saveParent(this);
                item.processFeedAttachmentsViewHolder(itemBaseViewHolder, position, dAttachment);
            } else {
                item.processFeedViewHolder(itemBaseViewHolder, position);
            }
        } else if (images != null) {
            // todo show image if feed has image
            if (mainImageView != null) {
                DMobi.log(TAG, "set main image for feed " + images.getFull().url);
                mainImageView.setVisibility(View.VISIBLE);
                ImageHelper.display(mainImageView, images.getFull());
            }
        }

        // TODO Check if liked
        if (this.canDisplayLike()) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_like);
            imageView.setOnClickListener(this);
            updateLikeView(imageView);
        }

        // TODO process comment button
        if (this.canDisplayComment()) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_comment);
            if (imageView != null) imageView.setOnClickListener(this);
            textView = (TextView) itemBaseViewHolder.findView(R.id.tb_total_comment);
            if (textView != null) {
                textView.setText(getTotalComment() + " " + DMobi.translate(getTotalComment() > 1 ? "comments" : "comment"));
            }
        }

        // todo process shape button
        if (this.canDisplayShare()) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.bt_share);
            if (imageView != null) {
                imageView.setOnClickListener(this);
            }
        }
    }

    public void showBottomMenu(Context context, TutsPlusBottomSheetDialogFragment bottomSheetDialogFragment) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            bottomSheetDialogFragment.show(appCompatActivity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
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
        return item != null && item.isLike();
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
            View parent;
            parent = (View) v.getParent();
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
            case R.id.img_avatar:
                context = v.getContext();
                intent = new Intent(context, UserProfileActivity.class);
                User currentUser = (User) DMobi.getData(UserProfileActivity.USER_PROFILE);
                boolean canOpen = (currentUser == null || (currentUser.getId() != user.getId() ? true : false));
                if (canOpen) {
                    DMobi.pushData(UserProfileActivity.USER_PROFILE, user);
                    context.startActivity(intent);
                }
                break;
            // todo process like action
            case R.id.bt_like:
                if (!this.canLike()) {
                    return;
                }
                DMobileModelBase item = getAttachment();
                item.like(new DResponse.Complete() {
                    @Override
                    public void onComplete(Boolean status, Object o) {

                    }
                });

                updateLikeView(v);
                break;
            // todo process comment action
            case R.id.bt_comment:
                if (!this.canComment()) {
                    return;
                }
                context = v.getContext();
                if (context instanceof FeedDetailActivity) {
                    return;
                }
                intent = new Intent(context, FeedDetailActivity.class);
                DMobi.pushData(FeedDetailActivity.FEED_DETAIL, this);
                context.startActivity(intent);
                break;
            // todo process shape action
            case R.id.bt_share:
                if (!this.canShare()) {
                    return;
                }
                share(v.getContext());
                break;
        }
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        processFeedViewHolder(itemBaseViewHolder, position);
    }

    @Override
    public boolean canComment() {
        DMobi.log(TAG, this.getItemType());
        if (this.typeId.equals("user_photo")) {
            return false;
        }
        return super.canComment();
    }

    @Override
    public boolean canLike() {
        if (this.typeId.equals("user_photo")) {
            return false;
        }
        return super.canLike();
    }
}