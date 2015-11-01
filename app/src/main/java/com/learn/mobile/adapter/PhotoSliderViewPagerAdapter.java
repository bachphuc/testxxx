package com.learn.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.learn.mobile.fragment.PhotoViewFragment;
import com.learn.mobile.model.Photo;

import java.util.List;

/**
 * Created by 09520_000 on 10/17/2015.
 */
public class PhotoSliderViewPagerAdapter extends FragmentStatePagerAdapter {
    protected List<Photo> data;
    ViewPager viewPager;
    public void setViewPager(ViewPager v){
        viewPager = v;
    }

    public PhotoSliderViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void setData(List<Photo> data) {
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        final int pos = position;
        PhotoViewFragment fragment = new PhotoViewFragment();
        if(viewPager != null){
            fragment.setViewPager(viewPager);
        }
        Photo item = data.get(position);
        fragment.setPhoto(item);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
