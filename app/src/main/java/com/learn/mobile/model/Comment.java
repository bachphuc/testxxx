package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

/**
 * Created by 09520_000 on 8/9/2015.
 */
public class Comment extends DAbstractComment implements View.OnClickListener {
    public Comment() {
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.comment_item_layout);
    }

    @Override
    public void processViewHolder(ItemBaseViewHolder itemBaseViewHolder) {
        super.processViewHolder(itemBaseViewHolder);

        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            ImageHelper.display(imageView, user.images.normal.url);

            imageView.setOnClickListener(this);
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(user.getTitle());

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_content);
        textView.setText(text);

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_time);
        textView.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_photo:
                Context context = v.getContext();
                Intent intent = new Intent(context, UserProfileActivity.class);
                DMobi.pushData(UserProfileActivity.USER_PROFILE, user);

                context.startActivity(intent);
                break;
        }

    }
}
