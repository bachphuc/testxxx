package com.learn.mobile.customview.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by BachPhuc on 4/11/2016.
 */
public class FixedScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    public static final String TAG = FixedScrollingViewBehavior.class.getSimpleName();

    public FixedScrollingViewBehavior() {
    }

    public FixedScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        if (child.getLayoutParams().height == -1) {
            List dependencies = parent.getDependencies(child);
            if (dependencies.isEmpty()) {
                return false;
            }

            AppBarLayout appBar = findFirstAppBarLayout(dependencies);
            if (appBar != null && ViewCompat.isLaidOut(appBar)) {
                if (ViewCompat.getFitsSystemWindows(appBar)) {
                    ViewCompat.setFitsSystemWindows(child, true);
                }

                int scrollRange = appBar.getTotalScrollRange();
                int height = parent.getHeight() - appBar.getMeasuredHeight() + Math.min(scrollRange, parent.getHeight() - heightUsed);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
        }
        return false;
    }

    private static AppBarLayout findFirstAppBarLayout(List<View> views) {
        int i = 0;

        for (int z = views.size(); i < z; ++i) {
            View view = views.get(i);
            if (view instanceof AppBarLayout) {
                return (AppBarLayout) view;
            }
        }

        return null;
    }
}