package com.learn.mobile.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.ViewHolder.ItemBaseViewHolder;
import com.learn.mobile.activity.ChatDetailActivity;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BachPhuc on 4/18/2016.
 */
public class ChatUser extends DMobileModelBase implements View.OnClickListener {
    public static final String TAG = ChatUser.class.getSimpleName();
    public double online_time;
    public boolean online;
    public String username;
    public String image;

    public String getImage() {
        return image;
    }

    public List<DMobileModelBase> messages = new ArrayList<>();
    protected HashMap<String, ChatMessage> processingMessages = new HashMap<>();

    public void addProcessingMessage(ChatMessage message) {
        DMobi.log(TAG, "addProcessingMessage to user " + username);
        long key = message.bMine ? message.getKey() : message.getSenderKey();
        processingMessages.put("" + key, message);
    }

    public void removeProcessingMessage(long key) {
        if (processingMessages.containsKey(key)) {
            processingMessages.remove("" + key);
        }
    }

    public ChatMessage getProcessingMessage(long key) {
        DMobi.log(TAG, "getProcessingMessage from user " + username);
        if (!processingMessages.containsKey("" + key)) {
            DMobi.log(TAG, "getProcessingMessage: no message processing");
            return null;
        }
        return processingMessages.get("" + key);
    }

    public List<DMobileModelBase> getMessages() {
        return messages;
    }

    @Override
    public String getTitle() {
        if (username != null) {
            return username;
        }
        return super.getTitle();
    }

    @Override
    public void processViewHolder(RecyclerViewBaseAdapter adapter, ItemBaseViewHolder itemBaseViewHolder, int position) {
        super.processViewHolder(adapter, itemBaseViewHolder, position);
        ImageView imageView = (ImageView) itemBaseViewHolder.findView(R.id.img_photo);
        if (imageView != null && image != null) {
            ImageHelper.display(imageView, image);
        }

        TextView textView = (TextView) itemBaseViewHolder.findView(R.id.tv_title);
        if (textView != null) {
            textView.setText(getTitle());
        }

        LinearLayout linearLayout = (LinearLayout) itemBaseViewHolder.findView(R.id.panel_content);
        if (linearLayout != null) {
            linearLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.panel_content:
                DMobi.pushData(ChatDetailActivity.CHAT_USER_DETAIL, this);
                Context context = v.getContext();
                Intent intent = new Intent(context, ChatDetailActivity.class);
                context.startActivity(intent);
                break;
        }
    }
}
