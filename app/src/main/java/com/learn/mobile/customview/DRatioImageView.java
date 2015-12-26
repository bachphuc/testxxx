package com.learn.mobile.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.learn.mobile.R;

/**
 * Created by 09520_000 on 9/3/2015.
 */
public class DRatioImageView extends ImageView {
    protected float ratio = 0;

    public DRatioImageView(Context context) {
        super(context);
    }

    public DRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaletteSquareImageView,
                0, 0);

        try {
            ratio = a.getFloat(R.styleable.DRatioImageView_ratio, 0f);
        } finally {
            a.recycle();
        }
    }

    public DRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
