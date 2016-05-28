package com.learn.mobile.model;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.DActivityBasic;
import com.learn.mobile.activity.PhotoDetailActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.model.DAbstract.DAbstractPhoto;

import java.util.List;
import java.util.Random;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class Photo extends DAbstractPhoto implements View.OnClickListener {
    protected int feedLayout;

    public Photo() {

    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);
        final DFeedImageView imageView;
        if (images != null) {
            imageView = (DFeedImageView) itemBaseViewHolder.findView(R.id.img_main_image);
            if (imageView != null) {
                processClickFeedAttachment(imageView);
                float ratio = (float) images.getExtraLarge().height / (float) images.getExtraLarge().width;
                imageView.setScale(ratio);
                ImageHelper.display(imageView, images.getExtraLarge().url);
            }
        }

    }

    public void processClickFeedAttachment(View view) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(this);
    }

    protected void displayPhotoAttachmentItem(ItemBaseViewHolder itemBaseViewHolder, DAttachment dAttachment, int position, int layout) {
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(layout);
        Photo attachmentItem = (Photo) dAttachment.getItem(position);
        attachmentItem.processClickFeedAttachment(imageView);
        ImageHelper.display(imageView, attachmentItem.getImages().getLarge().url);
    }

    @Override
    public void processFeedAttachmentsViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position, DAttachment dAttachment) {
        super.processFeedAttachmentsViewHolder(itemBaseViewHolder, position, dAttachment);
        if (dAttachment == null) {
            return;
        }
        if (dAttachment.getAttachmentCount() == 0) {
            return;
        }

        int count = dAttachment.getAttachmentCount();

        if (count >= 2) {
            displayPhotoAttachmentItem(itemBaseViewHolder, dAttachment, 0, R.id.img_photo_1);
            displayPhotoAttachmentItem(itemBaseViewHolder, dAttachment, 1, R.id.img_photo_2);
        }

        if (count >= 3) {
            displayPhotoAttachmentItem(itemBaseViewHolder, dAttachment, 2, R.id.img_photo_3);
        }

        if (count >= 4) {
            displayPhotoAttachmentItem(itemBaseViewHolder, dAttachment, 3, R.id.img_photo_4);
        }

        if (count >= 5) {
            displayPhotoAttachmentItem(itemBaseViewHolder, dAttachment, 4, R.id.img_photo_5);
        }
    }

    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, final int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, adapter.getData());
                    Intent intent = new Intent(context, PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.PHOTO_POSITION, position);
                    intent.putExtra(RecyclerViewBaseAdapter.RECYCLER_VIEW_NOTIFY_DATA_CHANGE, adapter.getNotifyDataSetChangedEventKey());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && false) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation((Activity) context, v, "robot");
                        context.startActivity(intent, options.toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Context context = v.getContext();
                    if (context instanceof DActivityBasic) {
                        DMobi.log("Photo Model", "setOnLongClickListener");
                        DActivityBasic dActivityBasic = (DActivityBasic) context;
                        dActivityBasic.showImagePreview(getImages().getLarge().url);
                        return true;
                    }
                    return false;
                }
            });

            if (images != null && images.getLarge() != null) {
                ImageHelper.getGlobalAdapter().loadFlickrThumb(images, imageView);
            }
        }
    }

    @Override
    public void showItemDetail(Context context) {
        DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, this);
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_main_image:
                showItemDetail(v.getContext());
                break;
            case R.id.img_photo:
            default:
                Context context = v.getContext();
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                Object parentObj = this.getData(PARENT_KEY);
                if (parentObj != null && parentObj instanceof Feed) {
                    Feed parentFeed = (Feed) parentObj;
                    if (parentFeed.getAttachments() != null && parentFeed.getAttachments().getAttachmentCount() > 1) {
                        List<DMobileModelBase> listData = parentFeed.getAttachmentItems();
                        DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, listData);
                        int index = listData.indexOf(this);
                        intent.putExtra(PhotoDetailActivity.PHOTO_POSITION, index);
                    } else {
                        DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, this);
                    }
                } else {
                    DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, this);
                }

                context.startActivity(intent);
                break;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.img_photo:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    int finalRadius = Math.max(v.getWidth(), v.getHeight());

                    SupportAnimator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, finalRadius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(1500);
                    animator.start();
                    return true;
                }
        }
        return false;
    }

    @Override
    public int getLayoutType(String suffix, String type) {
        if (!suffix.equals(LayoutHelper.FEED_LAYOUT)) {
            return super.getLayoutType(suffix, type);
        }
        if (feedLayout != 0) {
            return feedLayout;
        }
        Random rand;
        if (type.equals("5")) {
            rand = new Random();
            int randomLayout = rand.nextInt(2) + 1;
            type = type + "_" + randomLayout;
        } else if (type.equals("4")) {
            rand = new Random();
            int randomLayout = rand.nextInt(2) + 1;
            type = type + "_" + randomLayout;
        }
        feedLayout = super.getLayoutType(suffix, type);
        return feedLayout;
    }

    @Override
    public void saveParent(DMobileModelBase item) {
        this.setData(PARENT_KEY, item);
    }
}