package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

public class User extends DAbstractUser {

    public User(){
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.user_item_layout);
    }
    @Override
    public String getTitle() {
        return (fullName != null ? fullName : "");
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView)itemBaseViewHolder.findView(R.id.img_photo);
        if(imageView != null){
            ImageHelper.display(imageView, images.getNormal().url);
            final User that = this;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    DMobi.pushData(UserProfileActivity.USER_PROFILE, that);

                    context.startActivity(intent);
                }
            });
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(getTitle());
    }
}