package com.learn.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.model.DMobileModelBase;

import java.util.List;

/**
 * Created by 09520_000 on 9/14/2015.
 */
public class FeedAdapter extends RecyclerViewBaseAdapter {
    public FeedAdapter(List<DMobileModelBase> data) {
        super(data);
    }

    @Override
    public ItemBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        int position = viewType;
        DMobileModelBase item = data.get(position);

        itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(item.getFeedLayout(), viewGroup, false);

        ItemBaseViewHolder itemBaseViewHolder = new ItemBaseViewHolder(itemView);
        itemBaseViewHolder.attachAdapter(this);
        return itemBaseViewHolder;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ItemBaseViewHolder ItemBaseViewHolder, int position) {
        DMobileModelBase item = data.get(position);
        item.processFeedViewHolder(ItemBaseViewHolder, position);
    }
}
