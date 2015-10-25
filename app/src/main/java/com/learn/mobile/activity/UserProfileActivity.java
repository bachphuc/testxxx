package com.learn.mobile.activity;

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.customview.ControllableAppBarLayout;
import com.learn.mobile.fragment.BlankFragment;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.User;

public class UserProfileActivity extends DActivityBase implements NewFeedsFragment.OnFragmentInteractionListener, AppBarLayout.OnOffsetChangedListener {

    public static final String USER_PROFILE = "USER_PROFILE";
    private NewFeedsFragment profileFeedFragment;
    private User user;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    ViewPager viewPager;

    String[] tabs =  {"Wall", "Info", "Photo"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        user = (User) DMobi.getData(USER_PROFILE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(user.getTitle());
        getSupportActionBar().setTitle(user.getTitle());
        toolbar.bringToFront();
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getTitle());
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        initViewPager();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.bt_post);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllableAppBarLayout controllableAppBarLayout = (ControllableAppBarLayout) appBarLayout;
                controllableAppBarLayout.collapseToolbar(true);
            }
        });

        updateView();

        initTabBarLayoutWithTitle();
    }

    public void initTabBarLayoutWithTitle() {
        tabLayout = (TabLayout) findViewById(R.id.main_tab_bar);
        TabLayout.Tab tab;

        for(int i = 0; i < tabs.length; i++){
            tab = tabLayout.newTab();
            tab.setText(DMobi.translate(tabs[i]));
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // TODO initialize view pager
    private void initViewPager() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    profileFeedFragment = new NewFeedsFragment();
                    profileFeedFragment.setUser(user);
                    return profileFeedFragment;
                }
                return new BlankFragment();
            }

            @Override
            public int getCount() {
                return tabs.length;
            }
        };
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar photo clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (profileFeedFragment != null) {
            profileFeedFragment.setEnableRefresh(i == 0);
        }
    }

    public void setUser(User user) {
        this.user = user;
        updateView();
    }

    public void updateView() {
        if (user == null) {
            return;
        }

        if (user.coverPhoto != null) {
            DMobi.log("cover", user.coverPhoto.full.url);
            ImageView imageView = (ImageView) findViewById(R.id.img_cover);
            ImageHelper.display(imageView, user.coverPhoto.full.url);
        }

        ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        if (user.images != null) {
            ImageHelper.display(imgAvatar, user.images.medium.url);
        }
    }
}
