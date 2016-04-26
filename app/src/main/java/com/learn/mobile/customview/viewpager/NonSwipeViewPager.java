package com.learn.mobile.customview.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by BachPhuc on 4/25/2016.
 */
public class NonSwipeViewPager extends ViewPager {
    protected boolean bDisableSwipe = true;

    public void setDisableSwipe(boolean b) {
        bDisableSwipe = b;
    }

    public NonSwipeViewPager(Context context) {
        super(context);
    }

    public NonSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (bDisableSwipe) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (bDisableSwipe) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}