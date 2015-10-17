package com.learn.mobile.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.learn.mobile.R;
import com.learn.mobile.adapter.PhotoSliderViewPagerAdapter;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String PHOTO_SLIDER_DATA = "PHOTO_SLIDER_DATA";
    public static final String PHOTO_POSITION = "PHOTO_POSITION";
    protected List<Photo> photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_photo_slider);
        PhotoSliderViewPagerAdapter adapter = new PhotoSliderViewPagerAdapter(getSupportFragmentManager());
        Object object = DMobi.getData(PHOTO_SLIDER_DATA);

        Intent intent = getIntent();
        int position = intent.getIntExtra(PHOTO_POSITION, 0);
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
}
