package com.learn.mobile.model;

import android.text.Layout;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

import java.util.HashMap;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class DMobileModelBase {
    public String title;
    public String description;
    public int id;
    public String item_type;
    public ImageObject images;

    public HashMap<String, Object>  layouts = new HashMap<String, Object>();

    public DMobileModelBase() {
        registerLayout(LayoutHelper.FEED_LAYOUT, R.layout.feed_basic_layout);
    }

    public void registerLayout(String suffix, int layout) {
        int id = LayoutHelper.registerLayout(this.getClass(), suffix, layout);
        if(layouts.containsKey(id + "")){
            return;
        }
        layouts.put(suffix, id);
    }

    public int getLayoutType(String suffix){
        if(layouts.containsKey(suffix)){
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
        return (item_type != null ? item_type : "");
    }

    public ImageObject getImages() {
        return images;
    }

    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
    }

    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
    }
}
