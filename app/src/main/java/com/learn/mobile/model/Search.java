package com.learn.mobile.model;

import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.DAbstract.DAbstractSearch;

/**
 * Created by phuclb on 1/12/2016.
 */
public class Search extends DAbstractSearch {
    public Search() {

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
