package com.learn.mobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.fragment.CommentFragment;
import com.learn.mobile.fragment.DFragmentListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.Feed;
import com.learn.mobile.service.SComment;

public class FeedDetailActivity extends DActivityBasic implements DFragmentListener.OnFragmentInteractionListener, View.OnClickListener, Event.Action {
    public static final String FEED_DETAIL = "FEED_DETAIL";
    EditText commentEditText;
    Feed feed;
    SComment sComment;
    CommentFragment commentFragment;
    DMobileModelBase item;
    boolean isCommentProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        // TODO Get photo feed detail
        feed = (Feed) DMobi.getData(FEED_DETAIL);
        item = feed.getAttachment();
        commentFragment = new CommentFragment();
        commentFragment.setScrollToBottomWhenLoadMoreFinish(true);

        sComment = (SComment) DMobi.getInstance(SComment.class);
        sComment.addHeader(feed);
        commentFragment.setService(sComment);

        // TODO Set param default for request
        commentFragment.setParam("item_type", item.getItemType());
        commentFragment.setParam("item_id", item.getId());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.feed_detail_comment_fragment, commentFragment).commit();

        ImageView button = (ImageView) findViewById(R.id.bt_comment);
        button.setOnClickListener(this);

        commentEditText = (EditText) findViewById(R.id.tb_comment);
        commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                DMobi.log("FeedDetailActivity", "onEditorAction action edit text " + actionId);
                return false;
            }
        });

        commentEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                DMobi.log("FeedDetailActivity", "setOnKeyListener action edit text " + keyCode);
                return false;
            }
        });

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(s.length() - 1) == '\n') {
                    s.delete(s.length() - 1, s.length());
                    DMobi.log("FeedDetailActivity", "afterTextChanged Enter was pressed");
                    onComment();
                }
            }
        });

        DMobi.registerEvent(DEventType.EVENT_FEED_UPDATE_VIEW, this);
    }

    @Override
    public void onDestroyEvent() {
        DMobi.destroyEvent(DEventType.EVENT_FEED_UPDATE_VIEW);
        super.onDestroyEvent();
    }

    @Override
    protected void onDestroy() {
        DMobi.removeData(FEED_DETAIL);
        super.onDestroy();
    }

    private SComment getCommentService() {
        if (sComment != null) {
            return sComment;
        }
        sComment = (SComment) commentFragment.getService();
        return sComment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void resetComment() {
        commentEditText.setText("");
    }

    public void onComment() {
        if (isCommentProcessing) {
            DMobi.showToast(this, "Please wait a moment.");
            return;
        }

        SComment s = getCommentService();
        String content = commentEditText.getText().toString();
        content = content.trim();
        if (DUtils.isEmpty(content)) {
            DMobi.showToast(this, "Add some text to comment.");
            return;
        }
        isCommentProcessing = true;
        s.comment(item, content, new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                isCommentProcessing = false;
                resetComment();
                commentFragment.scrollToBottom(true);
                DMobi.fireEvent(DEventType.EVENT_FEED_UPDATE_VIEW, null);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                onComment();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_action, menu);
        return true;
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
    public void fireAction(String eventType, Object o) {
        switch (eventType) {
            case DEventType.EVENT_FEED_UPDATE_VIEW:
                commentFragment.notifyDataSetChanged();
                break;
        }
    }
}
