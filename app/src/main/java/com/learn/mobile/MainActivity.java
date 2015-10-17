package com.learn.mobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.learn.mobile.activity.DActivityBase;
import com.learn.mobile.activity.PostActivity;
import com.learn.mobile.adapter.AppViewPagerAdapter;
import com.learn.mobile.fragment.DFragmentListener;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.ImageHelper;

public class MainActivity extends DActivityBase implements LeftMenuFragment.OnLeftFragmentInteractionListener, NewFeedsFragment.OnFragmentInteractionListener, DFragmentListener.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;

    TabLayout tabLayout;
    ViewPager viewPager;
    AppViewPagerAdapter appViewPagerAdapter;

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set global application context config
        DConfig.setContext(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(ImageHelper.getIconDrawable(R.drawable.ic_menu_black_24dp));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(this);

        // TODO init left navigation menu
        initNavigationMenu();
        // TODO initialize view pager
        initViewPager();
        // TODO init tab layout
        initTabBarLayoutWithTitle();

        showLoginActivity();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.bt_show_post_activity);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostActivity();
            }
        });
    }

    // TODO Show post activity
    public void showPostActivity() {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
        // TODO Start a activity with animate
        // overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
    }

    public void showLoginActivity() {
        if (!DMobi.isUser()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    // TODO Init Navigation Menu
    private void initNavigationMenu() {
        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
        leftMenuFragment.setArguments(getIntent().getExtras());

        // Set full height for left navigation
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setFitsSystemWindows(true);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.left_drawer, leftMenuFragment).commit();
    }

    // TODO initialize view pager
    private void initViewPager() {
        appViewPagerAdapter = new AppViewPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(appViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                DMobi.fireEvent(Event.EVENT_LIST_BASE_FRAGMENT_LOADED + "_" + position, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initTabBarLayoutWithTitle() {
        tabLayout = (TabLayout) findViewById(R.id.main_tab_bar);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayout.Tab tab;

        String[] tabs =  {"Home", "Photo", "Member", "Message", "Notification"};
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

    public void initTabBarLayout() {
        tabLayout = (TabLayout) findViewById(R.id.main_tab_bar);
        TabLayout.Tab tab;

        Drawable mDrawable = ImageHelper.getIconDrawable(R.drawable.ic_web_black_24dp);
        tab = tabLayout.newTab();
        tab.setIcon(mDrawable);
        tabLayout.addTab(tab);

        mDrawable = ImageHelper.getIconDrawable(R.drawable.ic_insert_photo_white_24dp);
        tab = tabLayout.newTab();
        tab.setIcon(mDrawable);
        tabLayout.addTab(tab);

        mDrawable = ImageHelper.getIconDrawable(R.drawable.ic_supervisor_account_black_24dp);
        tab = tabLayout.newTab();
        tab.setIcon(mDrawable);
        tabLayout.addTab(tab);

        mDrawable = ImageHelper.getIconDrawable(R.drawable.ic_chat_bubble_outline_black_24dp);
        tab = tabLayout.newTab();
        tab.setIcon(mDrawable);
        tabLayout.addTab(tab);

        mDrawable = ImageHelper.getIconDrawable(R.drawable.ic_notifications_none_black_24dp);
        tab = tabLayout.newTab();
        tab.setIcon(mDrawable);
        tabLayout.addTab(tab);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do nothing, just override to prevent error when rotate device
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable = ImageHelper.getIconDrawable(drawable);
                menuItem.setIcon(drawable);
            }
        }

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLeftFragmentInteraction(String id) {

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

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.bt_capture_image:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onAppBarLayoutOffsetChange(AppBarLayout appBarLayout, int i) {
        super.onAppBarLayoutOffsetChange(appBarLayout, i);
    }
}
