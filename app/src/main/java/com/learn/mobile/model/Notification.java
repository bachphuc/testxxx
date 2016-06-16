package com.learn.mobile.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.DAbstract.DAbstractNotification;
import com.learn.mobile.service.SNotification;

/**
 * Created by BachPhuc on 5/28/2016.
 */
public class Notification extends DAbstractNotification implements View.OnClickListener {
    public static final String TAG = Notification.class.getSimpleName();
    @Override
    public void processViewHolder(final RecyclerViewBaseAdapter adapter, final ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            if (user.getImages() != null) {
                if (user.getImages().getAvatar() != null) {
                    ImageHelper.display(imageView, user.getImages().getAvatar().url);
                }
            }
            imageView.setOnClickListener(this);
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(user.getTitle());
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_content);
        if (textView != null) {
            textView.setText(content);
        }

        SpannableStringBuilder resultBuilder = new SpannableStringBuilder();
        SpannableString item = new SpannableString(user.getTitle());
        final int color = ContextCompat.getColor(textView.getContext(), R.color.primary_color);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(color);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View textView) {
                Context context = textView.getContext();
                showUserDetail(context);
            }
        };

        View view = itemBaseViewHolder.findView(R.id.bt_delete_item);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SNotification sNotification = (SNotification) adapter.getService();
                int position = itemBaseViewHolder.getAdapterPosition();
                DMobi.log(TAG, "delete notification id: " + getId() + ", position: " + position);
                markAsRead(sNotification);
                adapter.remove(position);
            }
        });

        item.setSpan(span, 0, user.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        resultBuilder.append(item);

        item = new SpannableString(content);
        item.setSpan(new AbsoluteSizeSpan(13, true), 0, item.length(), 0);
        resultBuilder.append(item);

        textView.setText(resultBuilder, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void showUserDetail(Context context) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        DMobi.pushData(UserProfileActivity.USER_PROFILE, user);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        showUserDetail(v.getContext());
    }

    public void markAsRead(SNotification service) {
        service.markAsRead(this);
    }
}
