package com.learn.mobile.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.fragment.FunnyFragment;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.fragment.PhotoFragment;
import com.learn.mobile.fragment.SearchFragment;
import com.learn.mobile.fragment.UserFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 09520_000 on 8/30/2015.
 */
public class AppViewPagerAdapter extends FragmentPagerAdapter {
    private HashMap<String, Fragment> fragmentList = new HashMap<>();

    public AppViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        final int pos = position;
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new NewFeedsFragment();
                fragmentList.put(position + "", fragment);
                break;
            case 1:
                UserFragment userFragment = new UserFragment();
                userFragment.setFragmentIndex(position);
                fragmentList.put(position + "", userFragment);
                return userFragment;
            case 2:
                PhotoFragment frag = new PhotoFragment();
                frag.setFragmentIndex(position);
                fragmentList.put(position + "", frag);
                return frag;
            case 3:
                FunnyFragment funnyFragment = new FunnyFragment();
                funnyFragment.setFragmentIndex(position);
                fragmentList.put(position + "", funnyFragment);
                return funnyFragment;
            case 4:
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setFragmentIndex(position);
                fragmentList.put(position + "", searchFragment);
                return searchFragment;
            default:
                return null;
        }

        return fragment;
    }

    public Fragment getFragmentItem(int position) {
        return fragmentList.get(position + "");
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
}
