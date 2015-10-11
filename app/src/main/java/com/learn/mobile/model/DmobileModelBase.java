package com.learn.mobile.model;

import android.util.Log;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SLike;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class DMobileModelBase {
    public String title;
    public String description;
    public int id;

    public ImageObject images;

    @SerializedName("item_type")
    public String itemType;

    @SerializedName("total_like")
    public int totalLike;

    @SerializedName("is_like")
    public boolean isLike;

    @SerializedName("total_comment")
    public int totalComment;

    public int getTotalComment(){
        return totalComment;
    }

    public HashMap<String, Object> layouts = new HashMap<String, Object>();

    public DMobileModelBase() {
        registerLayout(LayoutHelper.FEED_LAYOUT, R.layout.feed_basic_layout);
    }

    public void registerLayout(String suffix, int layout) {
        int id = LayoutHelper.registerLayout(this.getClass(), suffix, layout);
        if (layouts.containsKey(id + "")) {
            return;
        }
        layouts.put(suffix, id);
    }

    public int getLayoutType(String suffix) {
        if (layouts.containsKey(suffix)) {
            return (int) layouts.get(suffix);
        }
        throw new IllegalArgumentException("Layout " + suffix + " not register yet.");
    }

    public String getTitle() {
        return (title != null ? title : "");
    }

    public String getDescription() {
        return (description != null ? description : "");
    }

    public int getId() {
        return id;
    }

    public String getItemType() {
        return (itemType != null ? itemType : "");
    }

    public ImageObject getImages() {
        return images;
    }

    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
    }

    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
    }

    public boolean isLike() {
        return isLike;
    }

    public void setIsLike(boolean b) {
        isLike = b;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void like() {
        like(null);
    }

    public void like(final DResponse.Complete callback) {
        SLike sLike = (SLike) DMobi.getService(SLike.class);
        DResponse.Complete complete = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                DMobileModelBase itemBase = (DMobileModelBase) o;
                if (status) {
                    totalLike = itemBase.totalLike;
                    isLike = itemBase.isLike;
                }
                if (callback != null) {
                    callback.onComplete(status, itemBase);
                }
            }
        };

        if (isLike) {
            sLike.removeLike(itemType, id, complete);
            totalLike--;
        } else {
            sLike.like(itemType, id, complete);
            totalLike++;
        }
        isLike = !isLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }
}
