package com.learn.mobile.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.learn.mobile.R;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.customview.LargeRecyclerImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DecodeRunnable;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.Funny;
import com.learn.mobile.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class FunnyViewer extends AppCompatActivity implements DecodeRunnable.DecodeRunnableListener {
    public static final String TAG = FunnyViewer.class.getSimpleName();
    public static final String FUNNY_DETAIL = "FUNNY_DETAIL";

    Funny funny;
    TextView textView;
    DFeedImageView dFeedImageView;
    LargeRecyclerImageView multiImageViewer;
    ScrollView singleImageViewer;
    ProgressBar processBar;
    List<ImageItem> imageLists = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_viewer);

        textView = (TextView) findViewById(R.id.tv_funny_title);
        dFeedImageView = (DFeedImageView) findViewById(R.id.img_funny);
        multiImageViewer = (LargeRecyclerImageView) findViewById(R.id.funny_viewer_multi);
        singleImageViewer = (ScrollView) findViewById(R.id.funny_viewer_singer);
        processBar = (ProgressBar) findViewById(R.id.funny_loader);

        funny = (Funny) DMobi.getData(FUNNY_DETAIL);
        multiImageViewer.setImageList(imageLists);
        if (funny != null) {
            textView.setText(funny.getTitle());
            final ImageItem imageItem = funny.getImages().getFull();
            final Context context = this;

            if (imageItem.height < 3000) {
                multiImageViewer.setVisibility(View.GONE);
                float ratio = (float) imageItem.height / (float) imageItem.width;
                dFeedImageView.setScale(ratio);
                ImageHelper.display(dFeedImageView, imageItem.url);
                processBar.setVisibility(View.GONE);
            } else {
                singleImageViewer.setVisibility(View.GONE);

                if (DecodeRunnable.getImageListFromCache(imageItem.name) != null) {
                    imageLists = DecodeRunnable.getImageListFromCache(imageItem.name);
                    multiImageViewer.setImageList(imageLists);
                    multiImageViewer.notifyDataSetChanged();
                    processBar.setVisibility(View.GONE);
                } else {
                    Glide.with(context)
                            .load(imageItem.url)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(imageItem.width, imageItem.height) {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    onBitmapResourceReady(bitmap);
                                }
                            });
                }
            }
        }
    }

    public void onBitmapResourceReady(final Bitmap bitmap) {
        ImageItem imageItem = funny.getImages().getFull();
        DecodeRunnable decodeRunnable = new DecodeRunnable();
        decodeRunnable.setBitmap(bitmap);
        decodeRunnable.setImageItem(imageItem);
        decodeRunnable.setExternalCacheDir(getExternalCacheDir().getPath());
        decodeRunnable.addDecodeListener(this);

        new Thread(decodeRunnable).start();
    }

    @Override
    public void onComplete(final List<ImageItem> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                processBar.setVisibility(View.GONE);
                multiImageViewer.setImageList(list);
                multiImageViewer.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onUpdate(final List<ImageItem> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                multiImageViewer.setImageList(list);
                multiImageViewer.notifyDataSetChanged();
            }
        });
    }
}
