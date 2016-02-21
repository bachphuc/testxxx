package com.learn.mobile.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.customview.BlurringView;
import com.learn.mobile.customview.DMaterialProgressDrawable;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

import cimi.com.easeinterpolator.EaseBackInOutInterpolator;
import cimi.com.easeinterpolator.EaseBackOutInterpolator;

/**
 * Created by 09520_000 on 9/23/2015.
 */
public class DActivityBase extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DActivityBase.class.getSimpleName();
    protected AppBarLayout appBarLayout;
    protected int appBarOffsetTop = 0;
    protected View customViewerLayout;
    protected ImageView customImageViewer;
    protected View customViewerContent;
    protected DMaterialProgressDrawable imageViewerLoading;
    protected boolean bShowCustomViewer = false;
    protected BlurringView mBlurringView;

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
            customViewerLayout.setAlpha(0f);
            customViewerLayout.bringToFront();
            customImageViewer.setImageDrawable(imageViewerLoading);
            imageViewerLoading.start();
            bShowCustomViewer = true;

            mBlurringView.invalidate();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (customViewerContent.getAnimation() != null) {
                        customViewerContent.getAnimation().start();
                    } else {
                        float scaleFrom = 0.2f, scaleTo = 1;
                        ScaleAnimation scaleAnimation = new ScaleAnimation(scaleFrom, scaleTo, scaleFrom, scaleTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) {
                            protected void applyTransformation(float interpolatedTime, Transformation t) {
                                super.applyTransformation(interpolatedTime, t);
                                customViewerLayout.setAlpha(interpolatedTime);
                            }
                        };
                        Interpolator interpolator = new EaseBackOutInterpolator();
                        scaleAnimation.setInterpolator(interpolator);
                        scaleAnimation.setDuration(200);

                        customViewerContent.setAnimation(scaleAnimation);
                        customViewerContent.startAnimation(scaleAnimation);
                        findViewById(android.R.id.content).invalidate();
                    }
                }
            });
        }
    }

    public boolean isShowCustomViewer(){
        return bShowCustomViewer;
    }

    public void hideImagePreview() {
        if (customViewerLayout != null && customImageViewer != null) {
            customViewerLayout.setVisibility(View.GONE);
            imageViewerLoading.stop();
            bShowCustomViewer = false;
        }
    }

    public void initCustomView() {
        customViewerLayout = findViewById(R.id.custom_viewer);
        customViewerLayout.bringToFront();

        customImageViewer = (ImageView) findViewById(R.id.custom_image_viewer);
        imageViewerLoading = new DMaterialProgressDrawable(this, customImageViewer);
        imageViewerLoading.setStrokeWidth(1);
        imageViewerLoading.setBackgroundColor(0xFFFAFAFA);
        imageViewerLoading.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);
        customImageViewer.setImageDrawable(imageViewerLoading);
        View closeCustomViewer = findViewById(R.id.bt_close_custom_viewer);
        closeCustomViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImagePreview();
            }
        });

        customViewerContent = findViewById(R.id.custom_viewer_content);

        mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
        View blurredView = findViewById(R.id.content);

        // Give the blurring view a reference to the blurred view.
        mBlurringView.setBlurredView(blurredView);
    }
}
