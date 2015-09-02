package com.learn.mobile.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by phuclb on 6/9/2015.
 */
public class DFeedImageView extends ImageView {
    private float mScale = 1f;

    public DFeedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DFeedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DFeedImageView(Context context, float scale) {
        super(context);
        mScale = scale;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int)(width * mScale));
    }
}
