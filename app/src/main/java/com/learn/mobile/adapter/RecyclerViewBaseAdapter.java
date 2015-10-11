package com.learn.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.learn.mobile.model.DMobileModelBase;

import java.util.List;

/**
 * Created by 09520_000 on 9/2/2015.
 */
public class RecyclerViewBaseAdapter extends RecyclerView.Adapter<ItemBaseViewHolder> {
    protected List<DMobileModelBase> data;

    protected String layoutSuffix = LayoutHelper.LIST_LAYOUT;

    public RecyclerViewBaseAdapter(List<DMobileModelBase> data) {
        this.data = data;
    }

    @Override
    public ItemBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        int layout = LayoutHelper.getLayout(viewType);
        itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(layout, viewGroup, false);

        return new ItemBaseViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        DMobileModelBase item = data.get(position);
        return item.getLayoutType(layoutSuffix);
    }

    @Override
    public void onBindViewHolder(ItemBaseViewHolder ItemBaseViewHolder, int position) {
        DMobileModelBase item = data.get(position);
        item.processViewHolder(ItemBaseViewHolder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
