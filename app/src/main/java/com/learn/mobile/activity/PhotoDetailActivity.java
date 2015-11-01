package com.learn.mobile.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.learn.mobile.R;
import com.learn.mobile.adapter.PhotoSliderViewPagerAdapter;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String TAG = PhotoDetailActivity.class.getSimpleName();
    public static final String PHOTO_SLIDER_DATA = "PHOTO_SLIDER_DATA";
    public static final String PHOTO_POSITION = "PHOTO_POSITION";
    protected List<Photo> photos;
    PhotoSliderViewPagerAdapter adapter;
    ViewPager viewPager;
    String eventNotifyDataSetChangedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        viewPager = (ViewPager) findViewById(R.id.vp_photo_slider);
        adapter = new PhotoSliderViewPagerAdapter(getSupportFragmentManager());
        adapter.setViewPager(viewPager);
        Object object = DMobi.getData(PHOTO_SLIDER_DATA);

        Intent intent = getIntent();
        int position = intent.getIntExtra(PHOTO_POSITION, 0);

        eventNotifyDataSetChangedKey = intent.getStringExtra(RecyclerViewBaseAdapter.RECYCLER_VIEW_NOTIFY_DATA_CHANGE);
        if(eventNotifyDataSetChangedKey != null){
            DMobi.resetEvent(eventNotifyDataSetChangedKey);
            DMobi.log(TAG, "Register event in photo detail");
            DMobi.registerEvent(eventNotifyDataSetChangedKey, new Event.Action() {
                @Override
                public void fireAction(String eventType, Object o) {
                    DMobi.log(TAG, "Notify data set change in slider");
                    adapter.notifyDataSetChanged();
                }
            });
        }
        if(object != null){
            if(object instanceof Photo){
                Photo photo = (Photo) object;
                photos = new ArrayList<>();
                photos.add(photo);
            }
            else{
                photos = (List<Photo>)object;
            }
        }
        if(photos != null) {
            adapter.setData(photos);
        }
        viewPager.setAdapter(adapter);
        if(photos != null){
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_action_menu, menu);
        return true;
    }
}
