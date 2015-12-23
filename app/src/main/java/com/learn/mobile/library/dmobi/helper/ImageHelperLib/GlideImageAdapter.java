package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public class GlideImageAdapter extends ImageAdapterBase {
    @Override
    public void onDisplay(Context context, String url, ImageView imageView, String thumbnailUrl) {
        if (thumbnailUrl != null) {
            if (isResize) {
                Glide.with(imageView.getContext())
                        .load(url)
                        .thumbnail(Glide.with(imageView.getContext())
                                .load(thumbnailUrl)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .override(resizeWidth, resizeHeight)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                Glide.with(imageView.getContext())
                        .load(url)
                        .thumbnail(Glide.with(imageView.getContext())
                                .load(thumbnailUrl)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }

        } else {
            if (isResize) {
                Glide.with(imageView.getContext())
                        .load(url)
                        .override(resizeWidth, resizeHeight)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                Glide.with(imageView.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }
    }
}
