package com.learn.mobile.adapter;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.Feed;
import com.learn.mobile.service.SFeed;

import java.util.List;

/**
 * Created by 09520_000 on 9/14/2015.
 */
public class FeedAdapter extends RecyclerViewBaseAdapter {
    private static final String TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter(List<DMobileModelBase> data) {
        super(data);
        layoutSuffix = LayoutHelper.LIST_LAYOUT;
    }

    @Override
    public ItemBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        int layout = LayoutHelper.getLayout(viewType);
        try {
            DMobi.log(TAG, "LayoutInflater " + layout + ", viewType: " + viewType);
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(layout, viewGroup, false);
        } catch (InflateException e) {
            DMobi.log(TAG, "catch layout: " + layout + ", viewType: " + viewType + ", error: " + e.getMessage());
            layout = LayoutHelper.getLayout(LayoutHelper.FEED_DEFAULT_LAYOUT);
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(layout, viewGroup, false);
        }

        ItemBaseViewHolder itemBaseViewHolder = new ItemBaseViewHolder(itemView);
        itemBaseViewHolder.attachAdapter(this);
        return itemBaseViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Feed item = (Feed) data.get(position);
        return item.getFeedLayoutType();
    }

    public void delete(int position) {
        DMobileModelBase item = data.get(position);
        if (item == null) {
            DMobi.log(TAG, DMobi.translate("Can not delete this feed because it's null."));
            return;
        }
        SFeed sFeed = (SFeed) DMobi.getService(SFeed.class);
        sFeed.delete(item.getId());
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        DMobileModelBase item = data.get(position);
        item.processFeedViewHolder(itemBaseViewHolder, position);
    }
}
