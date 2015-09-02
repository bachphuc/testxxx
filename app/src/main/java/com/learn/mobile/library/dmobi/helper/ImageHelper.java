package com.learn.mobile.library.dmobi.helper;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.squareup.picasso.Picasso;

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

    public static void display(ImageView imageView, String imageUrl){
        Picasso.with(imageView.getContext()).load(imageUrl).into(imageView);
    }
}
