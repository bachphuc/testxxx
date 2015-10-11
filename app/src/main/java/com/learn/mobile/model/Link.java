package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.squareup.picasso.Picasso;

public class Link extends DAbstractLink implements View.OnClickListener {
    public Link() {
        registerLayout(LayoutHelper.FEED_LAYOUT, R.layout.feed_link_layout);
    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);
        ImageView imageView;
        if (!DUtils.isEmpty(image)) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.main_image);
            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(image).into(imageView);
            }
        }

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.link_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.link_description);
        if (textView != null) {
            String des = getDescription();
            if (DUtils.isEmpty(des)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(getDescription());
            }
        }

        LinearLayout linkAttachment = (LinearLayout) itemBaseViewHolder.findView(R.id.link_attachment);
        if (linkAttachment != null) {
            linkAttachment.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_attachment:
                Uri uriUrl = Uri.parse(link);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                Context context = v.getContext();
                context.startActivity(launchBrowser);
                break;
        }
    }
}