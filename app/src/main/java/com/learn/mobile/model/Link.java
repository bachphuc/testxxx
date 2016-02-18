package com.learn.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.FunnyViewer;
import com.learn.mobile.customview.chromecustomtab.CustomTabActivityHelper;
import com.learn.mobile.customview.chromecustomtab.WebviewFallback;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;
import com.squareup.picasso.Picasso;

public class Link extends DAbstractLink implements View.OnClickListener {
    public Link() {

    }

    @Override
    public void processFeedViewHolder(ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processFeedViewHolder(itemBaseViewHolder, position);
        ImageView imageView;
        if (!DUtils.isEmpty(image)) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_main_image);
            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(image).into(imageView);
            }
        }

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.link_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_link_description);
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
                Context context = v.getContext();

                boolean showWithCustomTab = true;
                if (showWithCustomTab) {
                    if (context instanceof Activity) {
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                        CustomTabActivityHelper.openCustomTab(
                                (Activity) context, customTabsIntent, uriUrl, new WebviewFallback());
                    }
                } else {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    context.startActivity(launchBrowser);
                }
                break;
        }
    }
}