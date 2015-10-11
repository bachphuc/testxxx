package com.learn.mobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.fragment.CommentFragment;
import com.learn.mobile.fragment.DFragmentListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.Feed;
import com.learn.mobile.service.SComment;

public class FeedDetailActivity extends DActivityBase implements DFragmentListener.OnFragmentInteractionListener, View.OnClickListener {
    public static final String FEED_DETAIL = "FEED_DETAIL";
    EditText commentEditText;
    Feed feed;
    SComment sComment;
    CommentFragment commentFragment;
    DMobileModelBase item;

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

        // TODO Get item feed detail
        feed = (Feed) DMobi.getData(FEED_DETAIL);
        item = feed.getAttachment();
        commentFragment = new CommentFragment();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                SComment s = getCommentService();
                String content = commentEditText.getText().toString();
                s.comment(item, content, new DResponse.Complete() {
                    @Override
                    public void onComplete(Boolean status, Object o) {
                        commentFragment.notifyDataSetChanged();
                        resetComment();
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }
}
