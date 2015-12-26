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
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.fragment.PhotoFragment;
import com.learn.mobile.fragment.UserFragment;

/**
 * Created by 09520_000 on 8/30/2015.
 */
public class AppViewPagerAdapter extends FragmentPagerAdapter {
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
                break;
            case 1:
                PhotoFragment frag = new PhotoFragment();
                frag.setFragmentIndex(position);
                return frag;
            case 2:
                UserFragment userFragment = new UserFragment();
                userFragment.setFragmentIndex(position);
                return userFragment;
            default:
                fragment = new Fragment() {
                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                             Bundle savedInstanceState) {
                        View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
                        Bundle args = getArguments();
                        ((TextView) rootView.findViewById(R.id.viewpage_title)).setText("View page:" + pos);
                        return rootView;
                    }
                };
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
}
