package com.learn.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.model.DMobileModelBase;

import java.util.List;

/**
 * Created by 09520_000 on 9/2/2015.
 */
public class RecyclerViewBaseAdapter extends RecyclerView.Adapter<ItemBaseViewHolder> {
    protected List<DMobileModelBase> data;

    public RecyclerViewBaseAdapter(List<DMobileModelBase> data) {
        this.data = data;
    }

    @Override
    public ItemBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        int position = viewType;
        DMobileModelBase item = data.get(position);

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
        DMobileModelBase item = data.get(position);
        item.processViewHolder(ItemBaseViewHolder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
