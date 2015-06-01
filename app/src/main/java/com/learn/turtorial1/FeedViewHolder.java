package com.learn.turtorial1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 09520_000 on 5/11/2015.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder{
    protected View rootView;

    public FeedViewHolder(View view){
        super(view);
        rootView = view;
    }

    public View findView(int viewId){
        View view = rootView.findViewById(viewId);
        return view;
    }
}
