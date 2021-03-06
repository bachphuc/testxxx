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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.adapter.ViewPagerAdapter;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.fragment.PhotoFragment;
import com.learn.mobile.fragment.UserInformationFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends DActivityBasic implements NewFeedsFragment.OnFragmentInteractionListener, AppBarLayout.OnOffsetChangedListener {
    public static final String TAG = UserProfileActivity.class.getSimpleName();
    public static final String USER_PROFILE = "USER_PROFILE";
    private NewFeedsFragment profileFeedFragment;
    PhotoFragment photoFragment;
    UserInformationFragment userInformationFragment;
    private User user;
    private Toolbar toolbar;

    private TabLayout tabLayout;
    ViewPager viewPager;

    ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);
        DMobi.log(TAG, "onCreate");
        user = (User) DMobi.getData(USER_PROFILE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(user.getTitle());
        getSupportActionBar().setTitle(user.getTitle());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbar.setTitleEnabled(false);

        TextView textView = (TextView) findViewById(R.id.tv_title);
        if (textView != null) {
            textView.setText(user.getTitle());
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DMobi.log(TAG, "before onRestoreInstanceState");
        adapter.onRestoreInstanceState(savedInstanceState);
        DMobi.log(TAG, "before create NewFeedsFragment");
        if (savedInstanceState != null) {
            profileFeedFragment = (NewFeedsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);
            userInformationFragment = (UserInformationFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 1);
            photoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 2);
        } else {
            profileFeedFragment = new NewFeedsFragment();
            userInformationFragment = new UserInformationFragment();
            photoFragment = new PhotoFragment();
        }

        // profile feed fragment
        if (profileFeedFragment == null) {
            profileFeedFragment = new NewFeedsFragment();
        }
        DMobi.log(TAG, "profileFeedFragment " + profileFeedFragment._id + " " + user.getId());
        profileFeedFragment.setUser(user);
        adapter.addFragment(profileFeedFragment, "Wall");

        // user information fragment
        if (userInformationFragment == null) {
            userInformationFragment = new UserInformationFragment();
        }
        userInformationFragment.setUser(user);
        adapter.addFragment(userInformationFragment, "Info");

        // photo fragment
        if (photoFragment == null) {
            photoFragment = new PhotoFragment();
        }
        photoFragment.setUser(user);
        photoFragment.setHasAppBar(true);
        photoFragment.setRefreshList(true);
        adapter.addFragment(photoFragment, "Photo");

        // set viewpager adapter
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        SUser sUser = (SUser) DMobi.getService(SUser.class);
        sUser.get(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    user = (User) o;
                    updateView();
                }
            }
        }, user.getId());
        updateView();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        adapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_update_avatar:
                Intent intent = new Intent(this, UploadAvatarActivity.class);
                this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
            DMobi.log("cover", user.coverPhoto.getFull().url);
            ImageView imageView = (ImageView) findViewById(R.id.img_cover);
            ImageHelper.display(imageView, user.coverPhoto.getFull().url);
        }

        ImageView imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        if (imgAvatar != null && user.images != null) {
            if (imgAvatar instanceof CircleImageView) {
                CircleImageView circleImageView = (CircleImageView) imgAvatar;
                circleImageView.setBorderWidth(1);
                circleImageView.setBorderColor(ContextCompat.getColor(this, R.color.image_border_color));
            }
            ImageHelper.display(imgAvatar, user.images.getMedium().url);
        }

        userInformationFragment.setUser(user);
        userInformationFragment.updateView();
    }

    @Override
    protected void onDestroy() {
        DMobi.removeData(USER_PROFILE);
        super.onDestroy();
    }
}
