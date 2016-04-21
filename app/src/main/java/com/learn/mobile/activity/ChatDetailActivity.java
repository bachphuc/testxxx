package com.learn.mobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.fragment.ChatDetailFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.ChatMessage;
import com.learn.mobile.model.ChatUser;
import com.learn.mobile.service.SChat;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatDetailActivity extends UploadFileBase implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    public static final String CHAT_USER_DETAIL = "CHAT_USER_DETAIL";
    ChatDetailFragment chatDetailFragment;
    ChatUser chatUser;
    SChat chatService;
    TextView tbComment;
    protected AppBarLayout appBarLayout;
    protected int appBarOffsetTop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableSwipe = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        chatUser = (ChatUser) DMobi.getData(CHAT_USER_DETAIL);
        actionBar.setTitle("");
        ImageView imageView = (ImageView) findViewById(R.id.im_chat_user);
        if (imageView != null) {
            ImageHelper.display(imageView, chatUser.getImage());
        }

        TextView textView = (TextView) findViewById(R.id.tv_chat_title);
        textView.setText(chatUser.getTitle());

        chatDetailFragment = new ChatDetailFragment();
        chatDetailFragment.setData(chatUser.getMessages());
        chatDetailFragment.setChatUser(chatUser);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.feed_detail_comment_fragment, chatDetailFragment).commit();


        // todo process send message
        View view = findViewById(R.id.bt_comment);
        view.setOnClickListener(this);

        // capture or open gallery
        view = findViewById(R.id.bt_open_camera);
        view.setOnClickListener(this);
        view = findViewById(R.id.bt_open_library);
        view.setOnClickListener(this);


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
            case R.id.bt_open_camera:
                captureImage();
                break;
            case R.id.bt_open_library:
                openGallery();
                break;
        }
    }

    @Override
    public void addPhotoAttachment(Uri uri) {
        String filePath = DUtils.getRealPathFromURI(this, uri);
        if (filePath == null) {
            filePath = DUtils.createNewImageFileFromUrl(this, uri);
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.photo = filePath;
        chatMessage.is_processing = true;
        chatMessage.receiveUsername = chatUser.getTitle();
        chatMessage.attachment_type = "photo";

        JSONObject params = new JSONObject();
        try {
            params.put("is_processing", true);
            params.put("attachment_type", "photo");
            ChatMessage messageCreated = chatService.sendMessage(chatMessage, params);
            DMobi.log(TAG, "processing message key: " + chatMessage.getKey());
            uploadPhoto(filePath, messageCreated);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void uploadPhoto(String path, ChatMessage message) {
        final long messageKey = message.getKey();
        chatService.uploadPhoto(path, new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    if (o instanceof String[]) {
                        updateMessage(messageKey, o);
                        DMobi.log(TAG, "upload photo success, ");
                    }
                }
            }
        });
    }

    public void updateMessage(long messageKey, Object o) {
        DMobi.log(TAG, "updateMessage key: " + messageKey);
        ChatMessage message = chatUser.getProcessingMessage(messageKey);
        if (message != null) {
            message.is_complete = true;
            message.is_processing = false;
            chatDetailFragment.notifyDataSetChanged();
            JSONObject jsonObject = new JSONObject();
            if (o instanceof String[]) {
                String[] photos = (String[]) o;
                try {
                    jsonObject.put("photo", photos[0]);
                    jsonObject.put("attachment_type", "photo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            chatService.updateMessage(message, jsonObject);
        }
    }

    public void sendMessage() {
        String text = tbComment.getText().toString();
        if (DUtils.isEmpty(text)) {
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.message = text;
        chatMessage.receiveUsername = chatUser.getTitle();
        chatService.sendMessage(chatMessage);
        tbComment.setText("");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        appBarOffsetTop = verticalOffset;
        onAppBarLayoutOffsetChange(appBarLayout, verticalOffset);
    }

    protected void onAppBarLayoutOffsetChange(AppBarLayout appBarLayout, int i) {

    }

    public int getAppBarOffsetTop() {
        return appBarOffsetTop;
    }

    public int getAppBarHeight() {
        if (appBarLayout == null) {
            return 0;
        }
        return appBarLayout.getHeight();
    }
}
