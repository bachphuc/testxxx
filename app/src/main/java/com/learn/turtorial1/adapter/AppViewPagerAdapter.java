package com.learn.turtorial1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.turtorial1.R;
import com.learn.turtorial1.fragment.NewfeedFragment;

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
        if (position == 0) {
            fragment = new NewfeedFragment();
        } else {
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
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
}
