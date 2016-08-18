package com.learn.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.learn.mobile.fragment.ChatFragment;
import com.learn.mobile.fragment.DChatListFragment;
import com.learn.mobile.fragment.DFragmentBase;
import com.learn.mobile.fragment.FunnyFragment;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.fragment.NotificationFragment;
import com.learn.mobile.fragment.PhotoFragment;
import com.learn.mobile.fragment.SearchFragment;
import com.learn.mobile.fragment.UserFragment;
import com.learn.mobile.model.Notification;

import java.util.HashMap;

/**
 * Created by 09520_000 on 8/30/2015.
 */
public class AppViewPagerAdapter extends FragmentPagerAdapter {
    public static final int NEW_FEEDS_FRAGMENT_INDEX = 0;
    public static final int USER_FRAGMENT_INDEX = 1;
    public static final int NOTIFICATION_FRAGMENT_INDEX = 2;
    public static final int PHOTO_FRAGMENT_INDEX = 3;
    public static final int FUNNY_FRAGMENT_INDEX = 4;
    public static final int SEARCH_FRAGMENT_INDEX = 5;

    private HashMap<String, DFragmentBase> fragmentList = new HashMap<>();
    public boolean bSmoothScroll = false;

    public AppViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case NEW_FEEDS_FRAGMENT_INDEX:
                NewFeedsFragment newFeedFragment = new NewFeedsFragment();
                if (bSmoothScroll) {
                    newFeedFragment.setHasAppBar(true);
                } else {
                    newFeedFragment.setHasAppBar(false);
                }

                fragmentList.put(position + "", newFeedFragment);
                return newFeedFragment;
            case USER_FRAGMENT_INDEX:
                UserFragment userFragment = new UserFragment();
                userFragment.setFragmentIndex(position);
                fragmentList.put(position + "", userFragment);
                if (bSmoothScroll) {
                    userFragment.setHasAppBar(true);
                }

                return userFragment;
            case NOTIFICATION_FRAGMENT_INDEX:
                NotificationFragment notificationFragment = new NotificationFragment();
                notificationFragment.setFragmentIndex(position);
                fragmentList.put(position + "", notificationFragment);
                if (bSmoothScroll) {
                    notificationFragment.setHasAppBar(true);
                }
                return notificationFragment;
            case PHOTO_FRAGMENT_INDEX:
                PhotoFragment frag = new PhotoFragment();
                frag.setFragmentIndex(position);
                fragmentList.put(position + "", frag);
                if (bSmoothScroll) {
                    frag.setHasAppBar(true);
                }
                return frag;
            case FUNNY_FRAGMENT_INDEX:
                FunnyFragment funnyFragment = new FunnyFragment();
                funnyFragment.setFragmentIndex(position);
                fragmentList.put(position + "", funnyFragment);
                if (bSmoothScroll) {
                    funnyFragment.setHasAppBar(true);
                }
                return funnyFragment;
            case SEARCH_FRAGMENT_INDEX:
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setFragmentIndex(position);
                fragmentList.put(position + "", searchFragment);
                if (bSmoothScroll) {
                    searchFragment.setHasAppBar(true);
                }
                return searchFragment;
            default:
                return null;
        }
    }

    public Fragment getFragmentItem(int position) {
        return fragmentList.get(position + "");
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DFragmentBase dFragmentBase = (DFragmentBase) getFragmentItem(position);
        if (dFragmentBase != null) {
            return dFragmentBase.getFragmentTitle();
        }
        return null;
    }
}
