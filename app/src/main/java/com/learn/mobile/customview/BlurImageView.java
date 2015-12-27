package com.learn.mobile.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.learn.mobile.library.dmobi.helper.Blur;

/**
 * Created by 09520_000 on 10/11/2015.
 */
public class BlurImageView extends ImageView {
    protected boolean ready = false;
    protected BlurBehindAndExecuteTask blurBehindAndExecuteTask;
    protected OnBlurCompleteListener onBlurCompleteListener;

    protected int radius = 20;

    public BlurImageView(Context context) {
        super(context);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTask();
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTask();
        onSetImageDrawable();
    }

    public void setRadius(int i) {
        radius = i;
    }

    public void initTask() {
        if (blurBehindAndExecuteTask != null) {
            return;
        }
        onBlurCompleteListener = new OnBlurCompleteListener() {
            @Override
            public void onBlurComplete() {
                ready = true;
            }
        };
        blurBehindAndExecuteTask = new BlurBehindAndExecuteTask(this, onBlurCompleteListener);
        blurBehindAndExecuteTask.setContext(this.getContext());
        blurBehindAndExecuteTask.setRadius(radius);
    }

    public void onSetImageDrawable() {
        initTask();
        if (blurBehindAndExecuteTask == null) {
            return;
        }
        if (ready) {
            ready = false;
            return;
        }
        Drawable d = getDrawable();
        if (d != null) {
            Bitmap bitmap;
            if (d instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) d).getBitmap();
            } else {
                this.buildDrawingCache();
                bitmap = this.getDrawingCache();
            }
            if (bitmap != null) {
                blurBehindAndExecuteTask.setBitmap(bitmap);
                blurBehindAndExecuteTask.setRadius(radius);
                blurBehindAndExecuteTask.execute();
            }
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        onSetImageDrawable();
    }

    private class BlurBehindAndExecuteTask extends AsyncTask<Void, Void, Void> {
        private BlurImageView imageView;
        Bitmap bitmap;
        Context context;
        private OnBlurCompleteListener onBlurCompleteListener;

        private View decorView;
        private Bitmap image;
        private int radius = 24;
        Bitmap newBitmap = null;

        public BlurBehindAndExecuteTask(BlurImageView imageView, OnBlurCompleteListener onBlurCompleteListener) {
            this.imageView = imageView;
            this.onBlurCompleteListener = onBlurCompleteListener;
        }

        public Bitmap getNewBitmap() {
            return newBitmap;
        }

        public void setContext(Context c) {
            this.context = c;
        }

        public void setRadius(int i) {
            if (i < 1 || i > 24) {
                return;
            }
            radius = i;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            newBitmap = Blur.fastBlur(context, bitmap, radius);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onBlurCompleteListener.onBlurComplete();
            imageView.setImageBitmap(newBitmap);
        }
    }

    interface OnBlurCompleteListener {
        public void onBlurComplete();
    }
}
