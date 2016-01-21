package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

/**
 * Created by 09520_000 on 1/22/2016.
 */

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.learn.mobile.model.ImageObject;

public class GlideHelper {
    private static final ViewPropertyAnimation.Animator ANIMATOR =
            new ViewPropertyAnimation.Animator() {
                @Override
                public void animate(View view) {
                    view.setAlpha(0f);
                    view.animate().alpha(1f);
                }
            };


    public static void loadResource(@DrawableRes int drawableId, @NonNull ImageView image) {
        Glide.with(image.getContext())
                .load(drawableId)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(new GlideDrawableImageViewTarget(image));
    }

    public static void loadFlickrThumb(@Nullable ImageObject photo, @NonNull final ImageView image) {
        Glide.with(image.getContext())
                .load(photo == null ? null : photo.getLarge().url)
                .dontAnimate()
                .thumbnail(Glide.with(image.getContext())
                        .load(photo == null ? null : photo.getAvatar().url)
                        .animate(ANIMATOR)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .into(new GlideDrawableTarget(image));
    }

    public static void loadFlickrThumb(String imageUrl, String thumbnailUrl, @NonNull final ImageView image) {
        Glide.with(image.getContext())
                .load(imageUrl)
                .dontAnimate()
                .thumbnail(Glide.with(image.getContext())
                        .load(thumbnailUrl)
                        .animate(ANIMATOR)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .into(new GlideDrawableTarget(image));
    }

    public static void loadFlickrFull(@NonNull ImageObject photo,
                                      @NonNull final ImageView image,
                                      @Nullable final ImageLoadingListener listener) {

        final String photoUrl = photo.getFull() != null ? photo.getLarge().url : null;

        Glide.with(image.getContext())
                .load(photoUrl)
                .dontAnimate()
                .placeholder(image.getDrawable())
                .thumbnail(Glide.with(image.getContext())
                        .load(photo.getAvatar().url)
                        .animate(ANIMATOR)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new GlideDrawableListener() {
                    @Override
                    public void onSuccess(String url) {
                        if (url.equals(photoUrl)) {
                            if (listener != null) listener.onLoaded();
                        }
                    }

                    @Override
                    public void onFail(String url) {
                        if (listener != null) listener.onFailed();
                    }
                })
                .into(new GlideDrawableTarget(image));
    }


    public interface ImageLoadingListener {
        void onLoaded();

        void onFailed();
    }

}

