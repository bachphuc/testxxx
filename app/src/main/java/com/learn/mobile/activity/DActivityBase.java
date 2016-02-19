package com.learn.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

/**
 * Created by 09520_000 on 9/23/2015.
 */
public class DActivityBase extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DActivityBase.class.getSimpleName();
    protected AppBarLayout appBarLayout;
    protected int appBarOffsetTop = 0;
    protected FrameLayout customViewerLayout;
    protected ImageView customImageViewer;

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        appBarOffsetTop = i;
        onAppBarLayoutOffsetChange(appBarLayout, i);
    }

    protected void onAppBarLayoutOffsetChange(AppBarLayout appBarLayout, int i) {

    }

    public int getAppBarOffsetTop() {
        return appBarOffsetTop;
    }

    public int getAppBarHeight() {
        if (appBarLayout == null) {
            return 0;
        }
        return appBarLayout.getHeight();
    }

    public void showImagePreview(String imageUrl) {
        if (customViewerLayout != null && customImageViewer != null) {
            DMobi.log(TAG, "showImagePreview");
            customViewerLayout.setVisibility(View.VISIBLE);
            ImageHelper.display(customImageViewer, imageUrl);
            customViewerLayout.bringToFront();
        }
    }

    public void hideImagePreview() {
        if (customViewerLayout != null && customImageViewer != null) {
            customViewerLayout.setVisibility(View.GONE);
        }
    }

    public void initCustomView() {
        customViewerLayout = (FrameLayout) findViewById(R.id.custom_viewer);
        customViewerLayout.bringToFront();

        customImageViewer = (ImageView) findViewById(R.id.custom_image_viewer);

        Button closeCustomViewer = (Button)findViewById(R.id.bt_close_custom_viewer);
        closeCustomViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImagePreview();
            }
        });
    }
}
