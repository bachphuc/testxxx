package com.learn.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.customview.LargeRecyclerImageView;
import com.learn.mobile.customview.chromecustomtab.CustomTabActivityHelper;
import com.learn.mobile.customview.chromecustomtab.WebviewFallback;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.DecodeRunnable;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.ImageHelperLib.ImageAdapterBase;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

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
                    ImageHelper
                            .getAdapter()
                            .with(this)
                            .load(imageItem.url)
                            .asBitmap()
                            .resize(imageItem.width, imageItem.height)
                            .callback(new ImageAdapterBase.ImageBitmapLoadedListener() {
                                @Override
                                public void onCompleted(Bitmap bitmap) {
                                    DMobi.log(TAG, "onCompleted");
                                    onBitmapResourceReady(bitmap);
                                }
                            }).display();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_funny_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar photo clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mn_open_in_browser:
                Uri uriUrl = Uri.parse(funny.getLink());
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                CustomTabActivityHelper.openCustomTab(
                        this, customTabsIntent, uriUrl, new WebviewFallback());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBitmapResourceReady(final Bitmap bitmap) {
        DMobi.log(TAG, "onBitmapResourceReady");
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
