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
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.chromecustomtab.CustomTabActivityHelper;
import com.learn.mobile.customview.chromecustomtab.WebviewFallback;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

public class Funny extends DAbstractFunny implements View.OnClickListener {
    public Funny() {
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.funny_item_layout);
    }

    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, final int position) {
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(user.getTitle());
        }
        ImageView imageView;
        if (user.images != null) {
            imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_avatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.images.getAvatar().url);
            }
        }

        imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_main_image);
        if (imageView != null) {
            ImageHelper.display(imageView, image);
            imageView.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_main_image:
                boolean showWithBrowser = false;
                Context context = v.getContext();
                if (showWithBrowser) {
                    if (context instanceof Activity) {
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                        CustomTabActivityHelper.openCustomTab(
                                (Activity) context, customTabsIntent, Uri.parse(getLink()), new WebviewFallback());
                    }
                } else {
                    DMobi.pushData(FunnyViewer.FUNNY_DETAIL, this);
                    Intent intent = new Intent(context, FunnyViewer.class);
                    context.startActivity(intent);
                }
                break;
        }
    }

    @Override
    public String getLink() {
        return (DUtils.isEmpty(link) ? DConfig.getBaseUrl() : link);
    }
}