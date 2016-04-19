package com.learn.mobile.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.squareup.picasso.Picasso;

public class Blog extends DAbstractBlog {

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_blog_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_blog_description);
        if (textView != null) {
            String des = getDescription();
            if (DUtils.isEmpty(des)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(getDescription());
            }
        }
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_main_image);
        if (getImages() != null) {
            imageView.setVisibility(View.VISIBLE);
            ImageHelper.display(imageView, getImages().getLarge());
        } else {
            imageView.setVisibility(View.GONE);
        }

    }
}