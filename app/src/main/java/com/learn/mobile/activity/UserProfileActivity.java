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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.adapter.ViewPagerRunnableAdapter;
import com.learn.mobile.fragment.DummyRecyclerViewFragment;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.User;

import me.henrytao.smoothappbarlayout.PagerAdapter;

public class UserProfileActivity extends AppCompatActivity implements NewFeedsFragment.OnFragmentInteractionListener, AppBarLayout.OnOffsetChangedListener {

    public static final String USER_PROFILE = "USER_PROFILE";
    private NewFeedsFragment profileFeedFragment;
    private User user;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    ViewPager viewPager;

    String[] tabs = {"Wall", "Info", "Photo"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_v1);
        Intent intent = getIntent();

        user = (User) DMobi.getData(USER_PROFILE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbar.setTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        ViewPagerRunnableAdapter adapter = new ViewPagerRunnableAdapter(getSupportFragmentManager());
        adapter.addFragment(DummyRecyclerViewFragment.newInstance("Cat", 100, R.layout.item_header_spacing), "Cat");
        adapter.addFragment(DummyRecyclerViewFragment.newInstance("Dog", 100, R.layout.item_header_spacing), "Dog");
        adapter.addFragment(DummyRecyclerViewFragment.newInstance("Mouse", 100, R.layout.item_header_spacing), "Mouse");
        adapter.addFragment(DummyRecyclerViewFragment.newInstance("Chicken", 5, R.layout.item_header_spacing), "Chicken");
        adapter.addFragment(DummyRecyclerViewFragment.newInstance("Bird", 100, R.layout.item_header_spacing), "Bird");

        if (adapter instanceof PagerAdapter) {
            viewPager.setAdapter(adapter);
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
        if (imgAvatar != null && user.images != null) {
            ImageHelper.display(imgAvatar, user.images.medium.url);
        }
    }
}
