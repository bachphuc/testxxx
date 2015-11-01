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
    public void display(Context context, String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
