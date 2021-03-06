package com.learn.mobile.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.learn.mobile.R;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;

import java.util.HashMap;

/**
 * Created by 09520_000 on 9/2/2015.
 */

public class ItemBaseViewHolder extends RecyclerView.ViewHolder {
    protected View rootView;
    RecyclerViewBaseAdapter adapter;
    private HashMap<String, View> listViews;
    private int id;

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public ItemBaseViewHolder(View view) {
        super(view);
        listViews = new HashMap<>();
        rootView = view;
        initViews();
    }

    public View findView(int viewId) {
        View view = listViews.get(viewId + "");
        if (view != null) {
            return view;
        }
        view = rootView.findViewById(viewId);
        if (view != null) {
            listViews.put(viewId + "", view);
        }

        return view;
    }

    public void attachAdapter(RecyclerViewBaseAdapter recyclerViewBaseAdapter) {
        this.adapter = recyclerViewBaseAdapter;
    }

    public RecyclerViewBaseAdapter getAdapter() {
        return adapter;
    }

    protected void initViews() {
        if (rootView == null) {
            return;
        }
        findView(R.id.tv_title);
        findView(R.id.img_avatar);
        findView(R.id.tv_description);
        findView(R.id.img_main_image);
    }
}
