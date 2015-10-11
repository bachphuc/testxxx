package com.learn.mobile.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.learn.mobile.library.dmobi.helper.Blur;

/**
 * Created by 09520_000 on 10/11/2015.
 */
public class BlurImageView extends ImageView {
    public BlurImageView(Context context) {
        super(context);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if(drawable != null){
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            Bitmap newImg = Blur.fastBlur(getContext(), bitmap, 12);
            Drawable d = new BitmapDrawable(getResources(), newImg);
            super.setImageDrawable(d);
            return;
        }

        super.setImageDrawable(drawable);
    }

}
