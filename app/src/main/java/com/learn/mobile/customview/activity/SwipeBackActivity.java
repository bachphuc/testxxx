package com.learn.mobile.customview.activity;

/**
 * Created by 09520_000 on 12/26/2015.
 */

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.learn.mobile.R;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackLayout.SwipeBackListener {

    private SwipeBackLayout swipeBackLayout;
    private ImageView ivShadow;

    protected boolean enableSwipe = true;

    @Override
    public void setContentView(int layoutResID) {
        if (!enableSwipe) {
            super.setContentView(layoutResID);
            return;
        }
        super.setContentView(getContainer());
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        swipeBackLayout.addView(view);
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setOnSwipeBackListener(this);
        ivShadow = new ImageView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivShadow.setBackgroundColor(ContextCompat.getColor(this, R.color.background_drag_activity));
        } else {
            ivShadow.setBackgroundColor(getResources().getColor(R.color.background_drag_activity));
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(swipeBackLayout);
        return container;
    }

    public void setShadowBackgroundColor(int color) {
        if (!enableSwipe) {
            return;
        }
        if (ivShadow == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivShadow.setBackgroundColor(ContextCompat.getColor(this, color));
        } else {
            ivShadow.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        swipeBackLayout.setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        ivShadow.setAlpha(1 - fractionScreen);
    }

}