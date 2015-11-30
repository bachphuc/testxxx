package com.learn.mobile.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;

/**
 * Created by 09520_000 on 10/13/2015.
 */
public class PaletteImageView extends ImageView {
    public static final String TAG = PaletteImageView.class.getSimpleName();
    protected PaletteListener.OnPaletteListener onPaletteListener;

    protected boolean enabledPalette = true;

    public void setOnPaletteListener(PaletteListener.OnPaletteListener onPaletteListener) {
        this.onPaletteListener = onPaletteListener;
    }

    public PaletteImageView(Context context) {
        super(context);
    }

    public PaletteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PaletteImageView,
                0, 0);

        try {
            enabledPalette = a.getBoolean(R.styleable.PaletteImageView_enablePalette, true);
        } finally {
            a.recycle();
        }
    }

    public PaletteImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (!enabledPalette) {
            return;
        }
        Drawable d = getDrawable();
        if (d != null) {
            if (d instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                if (bitmap != null) {
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrant =
                                    palette.getVibrantSwatch();
                            if (vibrant != null) {
                                onPaletteListenerChange(vibrant.getRgb(), vibrant.getTitleTextColor());
                            }
                        }
                    });
                }
            }
        }
    }

    private void onPaletteListenerChange(int backgroundColor, int textColor) {
        if (onPaletteListener != null) {
            onPaletteListener.onChange(this, backgroundColor, textColor);
        }
    }

    public static class PaletteListener {
        public interface OnPaletteListener {
            void onChange(View view, int backgroundColor, int textColor);
        }
    }
}
