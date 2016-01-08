package com.learn.mobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;

/**
 * Created by phuclb on 1/8/2016.
 */
public class GlobalSearchAdapter extends ResourceCursorAdapter {
    public GlobalSearchAdapter(Context context, int layout, Cursor c) {
        super(context, layout, c);
    }

    public GlobalSearchAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    public GlobalSearchAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
