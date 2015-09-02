package com.learn.mobile.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 09520_000 on 9/2/2015.
 */

public class ItemBaseViewHolder extends RecyclerView.ViewHolder{
    protected View rootView;

    public ItemBaseViewHolder(View view){
        super(view);
        rootView = view;
    }

    public View findView(int viewId){
        View view = rootView.findViewById(viewId);
        return view;
    }
}
