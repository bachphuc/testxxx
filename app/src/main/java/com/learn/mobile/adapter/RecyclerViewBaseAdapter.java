package com.learn.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.model.DmobileModelBase;
import com.learn.mobile.model.Feed;

import java.util.List;

/**
 * Created by 09520_000 on 9/2/2015.
 */

public class RecyclerViewBaseAdapter extends RecyclerView.Adapter<ItemBaseViewHolder> {
    private List<DmobileModelBase> data;

    public RecyclerViewBaseAdapter(List<DmobileModelBase> data) {
        this.data = data;
    }

    @Override
    public ItemBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        int position = viewType;
        DmobileModelBase item = data.get(position);

        itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(item.getLayout(), viewGroup, false);

        return new ItemBaseViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ItemBaseViewHolder ItemBaseViewHolder, int position) {
        DmobileModelBase item = data.get(position);
        item.processViewHolder(ItemBaseViewHolder);
    }

    public void prependData(List<DmobileModelBase> item) {
        data.addAll(0, item);
    }

    public void appendData(List<Feed> data) {
        data.addAll(data);
    }

    public int getMaxId() {
        if (data.size() == 0) {
            return 0;
        }
        DmobileModelBase item = data.get(0);
        return item.getId();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
