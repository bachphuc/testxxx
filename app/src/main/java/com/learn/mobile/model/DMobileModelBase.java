package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SLike;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class DMobileModelBase {
    public String title;
    public String description;
    public int id;

    private HashMap<String, Object> _data = new HashMap<String, Object>();

    public void addData(String key, Object value) {
        _data.put(key, value);
    }

    public Object getData(String key) {
        return _data.get(key);
    }

    public ImageObject images;

    @SerializedName("item_type")
    public String itemType;

    @SerializedName("total_like")
    public int totalLike;

    @SerializedName("is_like")
    public boolean isLike;

    @SerializedName("total_comment")
    public int totalComment;

    public int getTotalComment() {
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

    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
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

    public void like(final ImageView imageView, final TextView textView) {
        SLike sLike = (SLike) DMobi.getService(SLike.class);
        DResponse.Complete complete = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                DMobileModelBase itemBase = (DMobileModelBase) o;
                if (status) {
                    totalLike = itemBase.totalLike;
                    isLike = itemBase.isLike;
                } else {

                }
                updateLikeView(imageView, textView);
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
        updateLikeView(imageView, textView);
    }

    public void updateLikeView(ImageView imageView, TextView textView) {
        if (imageView != null) {
            if (isLike()) {
                imageView.setSelected(true);
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.primary_icon_color), PorterDuff.Mode.SRC_IN);
            } else {
                imageView.setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.feed_icon_action_color), PorterDuff.Mode.MULTIPLY);
                imageView.setSelected(false);
            }
        }
        if (textView != null) {
            textView.setText(getTotalLike() + " " + DMobi.translate(getTotalLike() > 1 ? "likes" : "like"));
        }
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public String getEventType() {
        String sType = getItemType();
        int iId = getId();
        if (DUtils.isEmpty(sType) || DUtils.isEmpty(iId)) {
            return null;
        }

        return (Event.EVENT_OBJECT_UPDATE + "_" + sType + "_" + iId);
    }

    public Event.ModelAction getEventData() {
        return Event.getModelActionInstance(getItemType(), getId(), this);
    }

    public String getItemLink() {
        return DConfig.getBaseUrl();
    }

    public void share(Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getItemLink());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, DMobi.translate("Send To")));
    }

    public void showItemDetail(Context context) {
        return;
    }

    public String getLink() {
        return DConfig.getBaseUrl();
    }

    public static DMobileModelBase getModelByType(String itemType) {
        String type = itemType;
        type = DUtils.convertToClassName(type);
        String sClass = DConfig.BUNDLE_ID + "." + DConfig.MODEL_NAME + "." + type;
        Class c = null;
        DMobileModelBase dMobileModelBase;
        try {
            c = Class.forName(sClass);
            try {
                dMobileModelBase = (DMobileModelBase) c.newInstance();
                dMobileModelBase.itemType = itemType;
                return dMobileModelBase;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
