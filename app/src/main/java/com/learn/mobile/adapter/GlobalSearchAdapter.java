package com.learn.mobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.global.Constant;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.DMobileModelBase;

import java.util.List;

/**
 * Created by phuclb on 1/8/2016.
 */
public class GlobalSearchAdapter extends ResourceCursorAdapter {
    protected List<DMobileModelBase> data;
    public static final String ITEM_TYPE = "ITEM_TYPE";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String IMAGE = "IMAGE";

    public static final int COL_ITEM_TYPE = 1;
    public static final int COL_ITEM_ID = 2;
    public static final int COL_TITLE = 3;
    public static final int COL_DESCRIPTION = 4;
    public static final int COL_IMAGE = 5;
    private String[] fields = new String[]{
            BaseColumns._ID,
            ITEM_TYPE,
            ITEM_ID,
            TITLE,
            DESCRIPTION,
            IMAGE
    };

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
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(cursor.getString(COL_TITLE));
        }
        textView = (TextView) view.findViewById(R.id.tv_description);
        if (textView != null) {
            textView.setText(cursor.getString(COL_DESCRIPTION));
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.img_photo);
        if (imageView != null) {
            ImageHelper.display(imageView, cursor.getString(COL_IMAGE));
        }
    }

    public void setData(List<DMobileModelBase> list) {
        data = list;
    }

    public List<DMobileModelBase> getData() {
        return data;
    }

    public int search(String query) {
        final MatrixCursor c = new MatrixCursor(fields);
        String image;
        for (int i = 0; i < data.size(); i++) {
            DMobileModelBase item = data.get(i);
            if (item.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                image = (item.getImages() != null ? (item.getImages().getAvatar() != null ? item.getImages().getAvatar().url : null) : null);
                c.addRow(new Object[]{
                        i,
                        item.getItemType(),
                        item.getId(),
                        item.getTitle(),
                        item.getDescription(),
                        image
                });
            }
            if (i >= Constant.MAX_SUGGESTION_LIMIT) {
                break;
            }
        }
        DMobi.log("search", c.getCount() + "");
        this.changeCursor(c);
        return c.getCount();
    }

    public DMobileModelBase getItemAtPosition(int position) {
        Cursor cursor = (Cursor) getItem(position);
        DMobileModelBase dMobileModelBase = DMobileModelBase.getModelByType(cursor.getString(COL_ITEM_TYPE));
        dMobileModelBase.id = cursor.getInt(COL_ITEM_ID);
        dMobileModelBase.title = cursor.getString(COL_TITLE);
        dMobileModelBase.description = cursor.getString(COL_DESCRIPTION);
        return dMobileModelBase;
    }
}
