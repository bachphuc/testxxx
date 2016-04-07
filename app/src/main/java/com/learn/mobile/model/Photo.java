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
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.DActivityBase;
import com.learn.mobile.activity.PhotoDetailActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.customview.PaletteImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class Photo extends DAbstractPhoto implements View.OnClickListener {
    public Photo() {

    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);
        final DFeedImageView imageView;
        if (images != null) {
            imageView = (DFeedImageView) itemBaseViewHolder.findView(R.id.img_main_image);
            if (imageView != null) {
                imageView.setOnClickListener(this);
                float ratio = (float) images.getExtraLarge().height / (float) images.getExtraLarge().width;
                imageView.setScale(ratio);
                ImageHelper.display(imageView, images.getExtraLarge().url);
            }
        }

    }

    protected void displayPhotoAttachmentItem(ItemBaseViewHolder itemBaseViewHolder, DAttachment dAttachment, int position, int layout) {
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(layout);
        DMobileModelBase attachmentItem = dAttachment.getItem(0);
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
    }

    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, final int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            boolean usePallet = false;
            if (imageView instanceof PaletteImageView && usePallet) {
                PaletteImageView paletteImageView = (PaletteImageView) imageView;
                paletteImageView.setOnPaletteListener(new PaletteImageView.PaletteListener.OnPaletteListener() {
                    @Override
                    public void onChange(View view, int backgroundColor, int textColor) {
                        View parentView = (View) view.getParent();
                        TextView tv = (TextView) parentView.findViewById(R.id.tv_title);
                        int bgColor = ImageHelper.makeColorDarker(backgroundColor, 30);
                        tv.setBackgroundColor(bgColor);
                        tv.setTextColor(textColor);
                    }
                });
            }

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
                    if (context instanceof DActivityBase) {
                        DMobi.log("Photo Model", "setOnLongClickListener");
                        DActivityBase dActivityBase = (DActivityBase) context;
                        dActivityBase.showImagePreview(getImages().getLarge().url);
                        return true;
                    }
                    return false;
                }
            });

            if (images != null && images.getLarge() != null) {
                ImageHelper.getGlobalAdapter().loadFlickrThumb(images, imageView);
            }
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(getTitle());
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
            case R.id.img_photo:
                Context context = v.getContext();
                DMobi.pushData(PhotoDetailActivity.PHOTO_SLIDER_DATA, this);
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                context.startActivity(intent);
                break;
            case R.id.img_main_image:
                showItemDetail(v.getContext());
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
}