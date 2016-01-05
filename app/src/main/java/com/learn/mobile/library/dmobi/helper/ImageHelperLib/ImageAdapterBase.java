package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by 09520_000 on 10/31/2015.
 */
public class ImageAdapterBase {
    protected String url;
    protected String thumbnailUrl;

    protected boolean isResize = false;
    protected int resizeWidth = 0;
    protected int resizeHeight = 0;

    protected Context context;

    protected boolean isBitmap = false;

    protected ImageView imageView;

    protected ImageBitmapLoadedListener listener;

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
        imageView = null;
        listener = null;
        thumbnailUrl = null;
        context = null;
    }

    public ImageAdapterBase load(String url) {
        this.url = url;
        return this;
    }

    public ImageAdapterBase with(Context context) {
        this.context = context;
        return this;
    }

    public ImageAdapterBase asBitmap() {
        isBitmap = true;
        return this;
    }

    public ImageAdapterBase thumbnail(String url) {
        this.thumbnailUrl = url;
        return this;
    }

    public ImageAdapterBase into(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public ImageAdapterBase callback(ImageBitmapLoadedListener listener) {
        this.listener = listener;
        return this;
    }

    public void display(Context context, String url, ImageView imageView, String thumbnailUrl, ImageBitmapLoadedListener listener) {
        with(context);
        load(url);
        into(imageView);
        callback(listener);
        thumbnail(thumbnailUrl);
        onDisplay();
    }

    public void display(Context context, String url, ImageView imageView, String thumbnailUrl) {
        display(context, url, imageView, thumbnailUrl, listener);
    }

    public void display(ImageView imageView, String url, String thumbnailUrl) {
        display(context, url, imageView, thumbnailUrl, listener);
    }

    public void display(ImageView imageView, String url) {
        display(context, url, imageView, thumbnailUrl, listener);
    }

    public void display(ImageView imageView) {
        display(context, url, imageView, thumbnailUrl, listener);
    }

    public void display() {
        display(context, url, imageView, thumbnailUrl, listener);
    }

    public void onDisplay() {

    }

    public interface ImageBitmapLoadedListener {
        public void onCompleted(Bitmap bitmap);
    }
}
