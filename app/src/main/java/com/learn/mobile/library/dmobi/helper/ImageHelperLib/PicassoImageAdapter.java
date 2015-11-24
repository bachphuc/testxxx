package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public class PicassoImageAdapter extends ImageAdapterBase {
    @Override
    public void display(Context context, String url, ImageView imageView, String thumbnailUrl) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }
}
