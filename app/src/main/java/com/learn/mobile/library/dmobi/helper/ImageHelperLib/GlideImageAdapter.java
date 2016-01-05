package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

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
                            .into(imageView);
                } else {
                    Glide.with(ctx)
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
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
}
