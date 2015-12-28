package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.UserProfileActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DCirclePaletteImageView;
import com.learn.mobile.customview.PaletteImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

public class User extends DAbstractUser implements View.OnClickListener, PaletteImageView.PaletteListener.OnPaletteListener {
    public static final String TAG = User.class.getSimpleName();

    public static final String PALETTE_AVATAR_COLOR = "PALETTE_AVATAR_COLOR";

    public User() {
        registerLayout(LayoutHelper.LIST_LAYOUT, R.layout.user_item_custom_layout);
    }

    @Override
    public String getTitle() {
        return (fullName != null ? fullName : "");
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null) {
            if (imageView instanceof PaletteImageView) {
                Object paletteColor = getData(PALETTE_AVATAR_COLOR);
                PaletteImageView paletteImageView = (PaletteImageView) imageView;
                if (paletteColor != null) {
                    paletteImageView.removeOnPaletteListerner();
                    LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.panel_user_item);
                    if (linearLayout != null) {
                        linearLayout.setBackgroundColor((int) paletteColor);
                    }
                } else {
                    paletteImageView.setOnPaletteListener(this);
                }
            }
            if (imageView instanceof DCirclePaletteImageView) {
                DCirclePaletteImageView dCirclePaletteImageView = (DCirclePaletteImageView) imageView;
                dCirclePaletteImageView.setBorderWidth(1);
                dCirclePaletteImageView.setBorderColor(ContextCompat.getColor(imageView.getContext(), R.color.image_border_color));
            }
            ImageHelper.display(imageView, images.getLarge().url);
            imageView.setOnClickListener(this);
        }
        imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_cover);
        if (imageView != null) {
            if (coverPhoto != null) {
                imageView.setOnClickListener(this);
                ImageHelper.display(imageView, coverPhoto.getLarge().url);
            }
        }
        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        textView.setText(getTitle());
    }

    @Override
    public void showItemDetail(Context context) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        DMobi.pushData(UserProfileActivity.USER_PROFILE, this);

        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cover:
            case R.id.img_photo:
                showItemDetail(v.getContext());
                break;
        }
    }

    @Override
    public void onChange(View view, int backgroundColor, int textColor) {
        int bgColor = ImageHelper.makeColorDarker(backgroundColor, 30);
        addData(PALETTE_AVATAR_COLOR, bgColor);
        View parentView = (View) view.getParent().getParent();
        if (parentView.getId() == R.id.panel_user_item) {

            parentView.setBackgroundColor(bgColor);
        } else {
            LinearLayout linearLayout = (LinearLayout) parentView.findViewById(R.id.user_panel_info);
            if (linearLayout != null) {
                linearLayout.setBackgroundColor(bgColor);
            }
        }
    }
}