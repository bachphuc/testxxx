package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public abstract class ImageAdapterBase {
    public ImageAdapterBase(){

    }

    public abstract void display(Context context, String url, ImageView imageView, String thumbnailUrl);
}
