package com.learn.mobile.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

public class Funny extends DAbstractFunny {
    public Funny() {
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.funny_item_layout);
    }

    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, final int position) {
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tvTitle);
        if (textView != null) {
            textView.setText(user.getTitle());
        }
        ImageView imageView;
        if (user.images != null) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.imageViewAvatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.getAvatar().url);
            }
        }

        imageView = (ImageView) itemBaseViewHolder.findView(R.id.main_image);
        if (imageView != null) {
            ImageHelper.display(imageView, image);
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_funny_title);
        if (getTitle() != null) {
            textView.setText(getTitle());
        } else {
            textView.setVisibility(View.GONE);
        }

        LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.feed_content);
        if (DUtils.isEmpty(getDescription())) {
            linearLayout.setVisibility(View.GONE);
        } else {
            textView = (TextView) itemBaseViewHolder.findView(R.id.tvDescription);
            textView.setText(getDescription());
        }
    }
}