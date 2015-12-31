package com.learn.mobile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.Funny;
import com.learn.mobile.model.ImageItem;

public class FunnyViewer extends AppCompatActivity {
    public static final String FUNNY_DETAIL = "FUNNY_DETAIL";
    Funny funny;
    TextView textView;
    DFeedImageView dFeedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_viewer);

        textView = (TextView) findViewById(R.id.tv_funny_title);
        dFeedImageView = (DFeedImageView) findViewById(R.id.img_funny);

        funny = (Funny) DMobi.getData(FUNNY_DETAIL);
        if (funny != null) {
            textView.setText(funny.getTitle());
            ImageItem imageItem = funny.getImages().getFull();
            float ratio = (float) imageItem.height / (float) imageItem.width;
            dFeedImageView.setScale(ratio);
            ImageHelper.display(dFeedImageView, funny.getImages().getFull().url);
        }
    }
}
