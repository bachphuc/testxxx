package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.learn.mobile.model.ImageObject;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public class GlideImageAdapter extends ImageAdapterBase {
    @Override
    public void onDisplay() {
        Context ctx = (context != null ? context : (imageView != null ? imageView.getContext() : null));
        if (ctx == null) {
            return;
        }
        if (url == null) {
            return;
        }

        RequestListener glideListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (listener != null) {
                    listener.onCompleted(null);
                }
                return false;
            }
        };

        if (!isBitmap) {
            if (thumbnailUrl != null) {
                if (isResize) {
                    Glide.with(ctx)
                            .load(url)
                            .thumbnail(Glide.with(ctx)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                            .override(resizeWidth, resizeHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                } else {
                    Glide.with(ctx)
                            .load(url)
                            .thumbnail(Glide.with(imageView.getContext())
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }

            } else {
                if (isResize) {
                    Glide.with(ctx)
                            .load(url)
                            .override(resizeWidth, resizeHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(glideListener)
                            .into(imageView);
                } else {
                    Glide.with(ctx)
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(glideListener)
                            .into(imageView);
                }
            }
        } else {
            if (listener != null) {
                Glide.with(ctx)
                        .load(url)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(resizeWidth, resizeHeight) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                listener.onCompleted(bitmap);
                            }
                        });
            }
        }
    }

    @Override
    public void loadFlickrThumb(@Nullable ImageObject photo, @NonNull final ImageView image) {
        GlideHelper.loadFlickrThumb(photo, image);
    }

    @Override
    public void loadFlickrThumb(String imageUrl, String thumbnailUrl, @NonNull final ImageView imageView) {
        GlideHelper.loadFlickrThumb(imageUrl, thumbnailUrl, imageView);
    }
}
