package com.learn.mobile.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 09520_000 on 9/3/2015.
 */
public class DSquareImageView extends ImageView {
    public DSquareImageView(Context context) {
        super(context);
    }

    public DSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
