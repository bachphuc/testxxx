package com.learn.mobile.activity;

import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.learn.mobile.LeftMenuFragment;
import com.learn.mobile.R;
import com.learn.mobile.fragment.CommentFragment;
import com.learn.mobile.fragment.DFragmentListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.Feed;
import com.learn.mobile.service.SComment;

public class FeedDetailActivity extends AppCompatActivity implements DFragmentListener.OnFragmentInteractionListener {
    public static final String FEED_DETAIL = "FEED_DETAIL";

    Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        feed = (Feed) DMobi.getData(FEED_DETAIL);
        DMobileModelBase item = feed.getAttachment();
        CommentFragment commentFragment = new CommentFragment();
        SComment sComment = (SComment) DMobi.getService(SComment.class);
        sComment.clearRequestParams();
        // TODO Set param default for request
        commentFragment.setParam("item_type", item.getItemType());
        commentFragment.setParam("item_id", item.getId());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.feed_detail_comment_fragment, commentFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
