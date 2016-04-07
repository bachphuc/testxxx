package com.learn.mobile.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.learn.mobile.R;

/**
 * Created by BachPhuc on 4/8/2016.
 */
public class DRatioLinearLayout extends LinearLayout {
    protected float ratio = 1f;

    public DRatioLinearLayout(Context context) {
        super(context);
    }

    public DRatioLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DRatioLinearLayout,
                0, 0);

        try {
            ratio = a.getFloat(R.styleable.DRatioLinearLayout_linear_ratio, 1f);
        } finally {
            a.recycle();
        }
    }

    public DRatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratio == 0) {
            return;
        } else {
            int width = getMeasuredWidth();
            int height = (int) ((float) width / ratio);
            setMeasuredDimension(width, height);
        }
    }
}

