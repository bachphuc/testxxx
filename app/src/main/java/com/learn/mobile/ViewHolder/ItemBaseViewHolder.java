package com.learn.mobile.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.learn.mobile.adapter.RecyclerViewBaseAdapter;

/**
 * Created by 09520_000 on 9/2/2015.
 */

public class ItemBaseViewHolder extends RecyclerView.ViewHolder{
    protected View rootView;
    RecyclerViewBaseAdapter adapter;

    public ItemBaseViewHolder(View view){
        super(view);
        rootView = view;
    }

    public View findView(int viewId){
        View view = rootView.findViewById(viewId);
        return view;
    }

    public void attachAdapter(RecyclerViewBaseAdapter recyclerViewBaseAdapter){
        this.adapter = recyclerViewBaseAdapter;
    }

    public RecyclerViewBaseAdapter getAdapter(){
        return adapter;
    }
}
