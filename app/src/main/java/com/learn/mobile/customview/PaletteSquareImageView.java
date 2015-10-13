package com.learn.mobile.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.learn.mobile.R;

/**
 * Created by 09520_000 on 10/13/2015.
 */
public class PaletteSquareImageView extends PaletteImageView {
    protected boolean enabledSquareImage = true;
    public PaletteSquareImageView(Context context) {
        super(context);
    }

    public PaletteSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaletteSquareImageView,
                0, 0);

        try {
            enabledSquareImage = a.getBoolean(R.styleable.PaletteSquareImageView_enableSquareImage, true);
        } finally {
            a.recycle();
        }
    }

    public PaletteSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!enabledSquareImage){
            return;
        }
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
