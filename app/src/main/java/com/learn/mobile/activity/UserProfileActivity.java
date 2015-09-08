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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.learn.mobile.FeedAdapter;
import com.learn.mobile.R;
import com.learn.mobile.fragment.NewfeedFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.Dresponse;
import com.learn.mobile.model.Feed;
import com.learn.mobile.service.SFeed;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements NewfeedFragment.OnFragmentInteractionListener,  AppBarLayout.OnOffsetChangedListener {

    public static final String PROFILE_TITLE = "PROFILE_TITLE";
    public static final String PROFILE_AVATAR = "PROFILE_AVATAR";
    private NewfeedFragment profileFeedFragment;
    AppBarLayout appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        final String title = intent.getStringExtra(PROFILE_TITLE);
        final String avatar = intent.getStringExtra(PROFILE_AVATAR);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        ImageView imageView = (ImageView) findViewById(R.id.img_avatar);
        ImageHelper.display(imageView, avatar);

        initViewPager();
    }

    // initialize view pager
    private void initViewPager() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                profileFeedFragment = new NewfeedFragment();
                return profileFeedFragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    RecyclerView recyclerView;
    FeedAdapter feedAdapter;
    SFeed sFeed;
    Dresponse.Complete completeResponse;
    private void initRecycleView(){
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        sFeed = (SFeed) DMobi.getService(SFeed.class);

        feedAdapter = new FeedAdapter(sFeed.getFeedData());
        recyclerView.setAdapter(feedAdapter);

        completeResponse = new com.learn.mobile.library.dmobi.request.Dresponse.Complete() {
            @Override
            public void onComplete(Object o) {
                if (o != null) {
                    List<Feed> feeds = (ArrayList<Feed>) o;
                    if (feeds.size() > 0) {
                        Log.i("User profile", "Bind data to recycler view");
                        feedAdapter.notifyDataSetChanged();
                    }
                }
            }
        };

        sFeed.loadMoreFeeds(this.completeResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        if(profileFeedFragment != null){
            profileFeedFragment.setEnableRefresh(i == 0);
        }
    }
}
