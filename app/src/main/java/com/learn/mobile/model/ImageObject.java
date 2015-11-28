package com.learn.mobile.model;

import android.media.Image;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class ImageObject {
    public ImageItem avatar = null;
    public ImageItem normal = null;
    public ImageItem medium = null;
    public ImageItem big = null;
    public ImageItem large = null;
    public ImageItem extraLarge = null;
    public ImageItem full = null;

    public ImageItem getAvatar() {
        if(avatar != null){
            return avatar;
        }
        if(avatar == null && normal == null && medium == null && big == null && large == null && extraLarge == null && full == null){
            return null;
        }
        return getFull();
    }

    public ImageItem getNormal() {
        if (normal != null) {
            return normal;
        }
        return getAvatar();
    }

    public ImageItem getMedium() {
        if (medium != null) {
            return medium;
        }
        return getNormal();
    }

    public ImageItem getBig() {
        if (big != null) {
            return big;
        }
        return getMedium();
    }

    public ImageItem getLarge() {
        if (large != null) {
            return large;
        }
        return getBig();
    }

    public ImageItem getExtraLarge() {
        if (extraLarge != null) {
            return extraLarge;
        }
        return getLarge();
    }

    public ImageItem getFull() {
        if (full != null) {
            return full;
        }

        return getExtraLarge();
    }
}
