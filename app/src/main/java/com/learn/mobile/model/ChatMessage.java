package com.learn.mobile.model;

import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

/**
 * Created by BachPhuc on 4/18/2016.
 */
public class ChatMessage extends DMobileModelBase {
    public String message;
    public String username;
    public String image;
    public boolean bMine = false;

    public boolean isMine() {
        return bMine;
    }

    @Override
    public String getTitle() {
        if (!DUtils.isEmpty(username)) {
            return username;
        }
        return super.getTitle();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null && image != null) {
            ImageHelper.display(imageView, image);
        }

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_content);
        if (textView != null) {
            textView.setText(getMessage());
        }
    }

    @Override
    public int getLayoutType(String suffix, String type) {
        if (suffix.equals(LayoutHelper.LIST_LAYOUT)) {
            type = (isMine() ? "right" : "left");
        }

        return super.getLayoutType(suffix, type);
    }
}
