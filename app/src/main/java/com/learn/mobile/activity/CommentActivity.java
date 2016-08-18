package com.learn.mobile.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.customview.com.faradaj.blurbehind.BlurBehind;
import com.learn.mobile.fragment.CommentFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.service.SComment;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    CommentFragment commentFragment;
    DMobileModelBase item;
    public static final String COMMENT_DETAIL_OBJECT = "COMMENT_DETAIL_OBJECT";
    EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        item = (DMobileModelBase) DMobi.getData(COMMENT_DETAIL_OBJECT);

        commentFragment = new CommentFragment();
        commentFragment.setParam("item_type", item.getItemType());
        commentFragment.setParam("item_id", item.getId());
        commentFragment.setScrollToBottomWhenLoadMoreFinish(true);

        ImageView button = (ImageView) findViewById(R.id.bt_comment);
        button.setOnClickListener(this);
        commentEditText = (EditText) findViewById(R.id.tb_comment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_comment_detail, commentFragment).commit();

        BlurBehind.getInstance()
                .withAlpha(150)
                .withBlurRadius(25)
                .setBackground(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                SComment s = (SComment) commentFragment.getService();
                String content = commentEditText.getText().toString();
                s.comment(item, content, new DResponse.Complete() {
                    @Override
                    public void onComplete(Boolean status, Object o) {
                        commentFragment.notifyDataSetChanged();
                        resetComment();
                        commentFragment.scrollToBottom(true);
                        if (status) {
                            item.totalComment++;
                            DMobi.fireEvent(item.getEventType(), item.getEventData());
                            DMobi.fireEvent(DEventType.EVENT_FEED_UPDATE_VIEW, null);
                        }
                    }
                });
                break;
        }
    }

    private void resetComment() {
        commentEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
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
}
