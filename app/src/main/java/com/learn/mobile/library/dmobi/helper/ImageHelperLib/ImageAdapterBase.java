package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public class ImageAdapterBase {

    protected boolean isResize = false;
    protected int resizeWidth = 0;
    protected int resizeHeight = 0;

    public ImageAdapterBase() {

    }

    public ImageAdapterBase resize(int w, int h) {
        isResize = true;
        resizeWidth = w;
        resizeHeight = h;
        return this;
    }

    protected void reset() {
        isResize = false;
    }

    public void display(Context context, String url, ImageView imageView, String thumbnailUrl) {
        onDisplay(context, url, imageView, thumbnailUrl);
        reset();
    }

    public void display(ImageView imageView, String url) {
        display(null, url, imageView, null);
    }

    public void onDisplay(Context context, String url, ImageView imageView, String thumbnailUrl) {

    }
}
