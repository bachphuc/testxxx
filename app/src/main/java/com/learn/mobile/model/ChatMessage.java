package com.learn.mobile.model;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.LayoutHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BachPhuc on 4/18/2016.
 */
public class ChatMessage extends DMobileModelBase {
    public long key;

    public long getKey() {
        return key;
    }

    public long senderKey;

    public long getSenderKey() {
        return senderKey;
    }

    public String message;
    public ChatUser sender;
    public ChatUser receiver;
    public String senderUsername;
    public boolean is_complete = false;
    public boolean is_update = false;
    public boolean is_processing = false;
    public String attachment_type;

    public String getSenderUsername() {
        return senderUsername;
    }

    public String receiveUsername;

    public String getReceiveUsername() {
        return receiveUsername;
    }

    public String senderImage;

    public String getSenderImage() {
        return senderImage;
    }

    public String photo;
    public boolean bMine = false;

    public boolean isMine() {
        return bMine;
    }

    @Override
    public String getTitle() {
        if (!DUtils.isEmpty(senderUsername)) {
            return senderUsername;
        }
        return super.getTitle();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null && senderImage != null) {
            ImageHelper.display(imageView, senderImage);
        }
        ImageView statusImageView = (ImageView) itemBaseViewHolder.findView(R.id.img_chat_photo_status);
        imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_chat_photo);
        if (imageView != null && attachment_type != null && attachment_type.equals("photo")) {
            imageView.setVisibility(View.VISIBLE);
            if (photo != null) {
                ImageHelper.display(imageView, photo);
            }

            if (statusImageView != null) {
                statusImageView.setVisibility(View.VISIBLE);
                if (is_complete) {
                    statusImageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_check_circle_black_24dp));
                }
            }
        } else {
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }

            if (statusImageView != null) {
                statusImageView.setVisibility(View.GONE);
            }
        }

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(sender.title);
        }

        textView = (TextView) itemBaseViewHolder.findView(R.id.tv_content);
        if (textView != null) {
            if (DUtils.isEmpty(getMessage())) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getMessage());
            }
        }
    }

    @Override
    public int getLayoutType(String suffix, String type) {
        if (suffix.equals(LayoutHelper.LIST_LAYOUT)) {
            type = (isMine() ? "right" : "left");
        }

        return super.getLayoutType(suffix, type);
    }

    public void merge(ChatMessage chatMessage) {
        this.photo = chatMessage.photo;
        this.is_update = chatMessage.is_update;
        this.is_complete = chatMessage.is_complete;
        this.message = chatMessage.message;
        this.is_processing = chatMessage.is_processing;
        this.attachment_type = chatMessage.attachment_type;
    }

    public void cloneFromJSONObject(JSONObject data) {
        try {
            if (data.has("message")) {
                this.message = data.getString("message");
            }
            if (data.has("is_processing")) {
                this.is_processing = DUtils.getBoolean(data.getBoolean("is_processing"));
            }
            if (data.has("is_update")) {
                this.is_update = DUtils.getBoolean(data.get("is_update"));
            }
            if (data.has("is_complete")) {
                this.is_complete = DUtils.getBoolean(data.get("is_complete"));
            }
            if (data.has("photo")) {
                this.photo = data.getString("photo");
            }
            if (data.has("attachment_type")) {
                this.attachment_type = data.getString("attachment_type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
