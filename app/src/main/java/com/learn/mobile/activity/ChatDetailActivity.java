package com.learn.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.fragment.ChatDetailFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.model.ChatMessage;
import com.learn.mobile.model.ChatUser;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.service.SChat;

import java.util.List;

public class ChatDetailActivity extends DActivityBasic implements View.OnClickListener {
    public static final String CHAT_USER_DETAIL = "CHAT_USER_DETAIL";
    ChatDetailFragment chatDetailFragment;
    ChatUser chatUser;
    SChat chatService;
    TextView tbComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        chatUser = (ChatUser) DMobi.getData(CHAT_USER_DETAIL);
        actionBar.setTitle(chatUser.getTitle());

        chatDetailFragment = new ChatDetailFragment();
        chatDetailFragment.setData(chatUser.getMessages());
        chatDetailFragment.setChatUser(chatUser);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.feed_detail_comment_fragment, chatDetailFragment).commit();


        // todo process send message
        ImageView imageView = (ImageView) findViewById(R.id.bt_comment);
        imageView.setOnClickListener(this);

        tbComment = (TextView) findViewById(R.id.tb_comment);
        chatService = (SChat) DMobi.getService(SChat.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar photo clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                sendMessage();
                break;
        }
    }

    public void sendMessage() {
        String text = tbComment.getText().toString();
        if (DUtils.isEmpty(text)) {
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.message = text;
        chatMessage.username = chatUser.getTitle();
        chatService.sendMessage(chatMessage);
        tbComment.setText("");
    }
}
