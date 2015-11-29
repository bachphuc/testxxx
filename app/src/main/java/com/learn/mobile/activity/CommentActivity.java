package com.learn.mobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.customview.com.faradaj.blurbehind.BlurBehind;
import com.learn.mobile.fragment.CommentFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
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
                            DMobi.fireEvent(Event.EVENT_FEED_UPDATE_VIEW, null);
                        }
                    }
                });
                break;
        }
    }

    private void resetComment() {
        commentEditText.setText("");
    }
}
