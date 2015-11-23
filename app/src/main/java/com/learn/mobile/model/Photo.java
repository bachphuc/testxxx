package com.learn.mobile.model;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.PhotoDetailActivity;
import com.learn.mobile.activity.PhotoViewerActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.customview.PaletteImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

public class Photo extends DAbstractPhoto implements View.OnClickListener {
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
                float ratio = (float) images.large.height / (float) images.large.width;
                imageView.setScale(ratio);
                ImageHelper.display(imageView, images.large.url);
            }
        }

    }

    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, final int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            if (imageView instanceof PaletteImageView && false) {
                PaletteImageView paletteImageView = (PaletteImageView) imageView;
                paletteImageView.setOnPaletteListener(new PaletteImageView.PaletteListener.OnPaletteListener() {
                    @Override
                    public void onChange(View view, int backgroundColor, int textColor) {
                        View parentView = (View) view.getParent();
                        TextView tv = (TextView) parentView.findViewById(R.id.tv_title);
                        int newBackgroundColor = Color.argb(150, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor));
                        tv.setBackgroundColor(newBackgroundColor);
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

            ImageHelper.display(imageView, images.big.url);
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(getTitle());
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
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.img_photo:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    View animateView = v;

                    int cx = x;
                    int cy = y;
                    int finalRadius = Math.max(v.getWidth(), v.getHeight());

                    SupportAnimator animator = ViewAnimationUtils.createCircularReveal(animateView, cx, cy, 0, finalRadius);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(1500);
                    animator.start();
                    return true;
                }
        }
        return false;
    }
}