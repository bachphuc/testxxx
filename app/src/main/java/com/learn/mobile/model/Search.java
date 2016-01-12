package com.learn.mobile.model;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DCirclePaletteImageView;
import com.learn.mobile.customview.PaletteImageView;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

/**
 * Created by phuclb on 1/12/2016.
 */
public class Search extends DAbstractSearch {
    public Search() {
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.user_item_layout);
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            if (getImages() != null) {
                if (getImages().getAvatar() != null) {
                    ImageHelper.display(imageView, getImages().getAvatar().url);
                }
            }
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(getTitle());
        }
    }
}
