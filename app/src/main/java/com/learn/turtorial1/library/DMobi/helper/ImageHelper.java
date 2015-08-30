package com.learn.turtorial1.library.dmobi.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.learn.turtorial1.R;
import com.learn.turtorial1.library.dmobi.global.DConfig;

/**
 * Created by 09520_000 on 8/29/2015.
 */
public class ImageHelper {
    public static Drawable getTintedDrawable(Context context, @DrawableRes int drawableResId, int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        drawable.setColorFilter(colorResId, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static Drawable getTintedDrawable( @DrawableRes int drawableResId, int colorResId) {
        return ImageHelper.getTintedDrawable(DConfig.getContext(), drawableResId, colorResId);
    }

    public static Drawable getIconDrawable(Context context, @DrawableRes int drawableResId){
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
}