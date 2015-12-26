package com.learn.mobile.library.dmobi.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.ImageHelperLib.ImageAdapterBase;
import com.learn.mobile.service.SBase;
import com.squareup.picasso.Picasso;

/**
 * Created by 09520_000 on 8/29/2015.
 */
public class ImageHelper {
    protected static ImageAdapterBase imageAdapterBase;

    public static Drawable getTintedDrawable(Context context, @DrawableRes int drawableResId, int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        drawable.setColorFilter(colorResId, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getTintedDrawable(@DrawableRes int drawableResId, int colorResId) {
        return ImageHelper.getTintedDrawable(DConfig.getContext(), drawableResId, colorResId);
    }

    public static Drawable getIconDrawable(Context context, @DrawableRes int drawableResId) {
        int color = ContextCompat.getColor(context, R.color.primary_icon_color);
        return ImageHelper.getTintedDrawable(context, drawableResId, color);
    }

    public static Drawable getIconDrawable(@DrawableRes int drawableResId) {
        return ImageHelper.getIconDrawable(DConfig.getContext(), drawableResId);
    }

    public static Drawable getIconDrawable(Drawable drawable, int color) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getIconDrawable(Drawable drawable) {
        int color = ContextCompat.getColor(DConfig.getContext(), R.color.primary_icon_color);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static ImageAdapterBase getAdapter() {
        if (imageAdapterBase != null) {
            return imageAdapterBase;
        }
        String sClass = DConfig.BUNDLE_ID + ".library.dmobi.helper.ImageHelperLib." + DConfig.IMAGE_ADAPTER;
        Class c = null;
        try {
            c = Class.forName(sClass);
            try {
                imageAdapterBase = (ImageAdapterBase) c.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Can not create instance of " + DConfig.IMAGE_ADAPTER);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Can not create instance of " + DConfig.IMAGE_ADAPTER);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can not find  " + DConfig.IMAGE_ADAPTER);
        }
        return imageAdapterBase;
    }

    public static void display(Context context, ImageView imageView, String imageUrl, String thumbnailUrl) {
        if (imageAdapterBase == null) {
            String sClass = DConfig.BUNDLE_ID + ".library.dmobi.helper.ImageHelperLib." + DConfig.IMAGE_ADAPTER;
            Class c = null;
            try {
                c = Class.forName(sClass);
                try {
                    imageAdapterBase = (ImageAdapterBase) c.newInstance();
                } catch (InstantiationException e) {
                    throw new IllegalArgumentException("Can not create instance of " + DConfig.IMAGE_ADAPTER);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Can not create instance of " + DConfig.IMAGE_ADAPTER);
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Can not find  " + DConfig.IMAGE_ADAPTER);
            }
        }
        if (imageAdapterBase != null) {
            imageAdapterBase.display(context, imageUrl, imageView, thumbnailUrl);
        }
    }

    public static void display(ImageView imageView, String imageUrl) {
        display(null, imageView, imageUrl, null);
    }

    public static void display(ImageView imageView, String imageUrl, String thumbnailUrl) {
        display(null, imageView, imageUrl, thumbnailUrl);
    }

    public static void setTint(ImageView imageView, int color) {
        if (imageView == null) {
            return;
        }
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        drawable = getIconDrawable(drawable, color);
        imageView.setImageDrawable(drawable);
    }

    public static int makeColorDarker(int color, int percent){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        if(hsv[1] < 100){
            hsv[1] *= (float)((percent + 100) / 100);
            if(hsv[1] > 100){
                hsv[1] = 100;
            }
        }

        color = Color.HSVToColor(hsv);
        return color;
    }

    public static int makeColorLighter(int color, int percent){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        if(hsv[1] < 100){
            hsv[1] *= (float)((100 - percent) / 100);
        }
        color = Color.HSVToColor(hsv);
        return color;
    }
}
