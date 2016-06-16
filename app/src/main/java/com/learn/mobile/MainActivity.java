package com.learn.mobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.learn.mobile.activity.DActivityBasic;
import com.learn.mobile.activity.PostActivity;
import com.learn.mobile.adapter.AppViewPagerAdapter;
import com.learn.mobile.adapter.GlobalSearchAdapter;
import com.learn.mobile.fragment.DFragmentListener;
import com.learn.mobile.fragment.NewFeedsFragment;
import com.learn.mobile.fragment.SearchFragment;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.service.SUser;

public class MainActivity extends DActivityBasic implements LeftMenuFragment.OnLeftFragmentInteractionListener, NewFeedsFragment.OnFragmentInteractionListener, DFragmentListener.OnFragmentInteractionListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String QUERY_KEY = "QUERY_KEY";
    private String appBarTitle;
    ActionBar actionBar;

    private GlobalSearchAdapter globalSearchAdapter;

    private DrawerLayout drawerLayout;

    TabLayout tabLayout;
    ViewPager viewPager;
    AppViewPagerAdapter appViewPagerAdapter;

    AutoCompleteTextView searchEditText;
    SearchView searchView;
    boolean bCloseDrawerLayout = false;
    int viewPagerMoveIndex = -1;

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DMobi.log(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCustomView();

        // set global application context config
        DConfig.setContext(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        appBarTitle = getResources().getString(R.string.home);
        if (actionBar != null) {
            actionBar.setTitle(appBarTitle);
            actionBar.setHomeAsUpIndicator(ImageHelper.getIconDrawable(R.drawable.ic_menu_black_24dp));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        // AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        // layoutParams.setScrollFlags(0);
        // toolbar.setLayoutParams(layoutParams);
        appBarLayout.addOnOffsetChangedListener(this);

        // TODO init search service
        initSearch();

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

        // todo init socket chat
        initSocketChat();

        // TODO: 5/23/2016 get GCM device token
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void initSocketChat() {
        // SChat chatService = (SChat) DMobi.getService(SChat.class);
        // chatService.init();
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
            Intent intent = new Intent(this, com.learn.mobile.activity.LoginActivity.class);
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

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (viewPagerMoveIndex != -1) {
                    // viewPager.setCurrentItem(viewPagerMoveIndex);
                    viewPager.setCurrentItem(viewPagerMoveIndex, false);
                    viewPagerMoveIndex = -1;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

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
                if (tabLayout != null) {
                    if (tabLayout.getTabCount() > position) {
                        tabLayout.getTabAt(position).select();
                    }
                }
                appBarLayout.setExpanded(true);
                actionBar.setTitle(appBarTitle);

                DMobi.fireEvent(Event.EVENT_UPDATE_LEFT_MENU_SELECTED, position);
                DMobi.fireEvent(Event.EVENT_LIST_BASE_FRAGMENT_LOADED + "_" + position, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /* if (state == ViewPager.SCROLL_STATE_IDLE && bCloseDrawerLayout) {
                    closeDrawerLayout();
                } */
            }
        });
    }

    public void initTabBarLayoutWithTitle() {
        tabLayout = (TabLayout) findViewById(R.id.main_tab_bar);
        if (DConfig.HIDE_TAB_BAR) {
            tabLayout.setVisibility(View.GONE);
            return;
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayout.Tab tab;

        String[] tabs = getResources().getStringArray(R.array.mainTab);

        for (int i = 0; i < tabs.length; i++) {
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

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // show search icon if unlock this code
        // searchView.setIconifiedByDefault(false);
        // Do not iconify the widget; expand it by default

        // set full width suggestion search for search view
        // id of AutoCompleteTextView
        int searchEditTextId = R.id.search_src_text; // for AppCompat

        // get AutoCompleteTextView from SearchView
        searchEditText = (AutoCompleteTextView) searchView.findViewById(searchEditTextId);
        final View dropDownAnchor = searchView.findViewById(searchEditText.getDropDownAnchor());
        if (dropDownAnchor != null) {
            searchEditText.setThreshold(0);
            dropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {

                    // calculate width of DropdownView
                    int point[] = new int[2];
                    dropDownAnchor.getLocationOnScreen(point);
                    // x coordinate of DropDownView
                    // int dropDownPadding = point[0] + searchEditText.getDropDownHorizontalOffset();

                    Rect screenSize = new Rect();
                    getWindowManager().getDefaultDisplay().getRectSize(screenSize);
                    // screen width
                    int screenWidth = screenSize.width();

                    // set DropDownView width
                    // searchEditText.setDropDownWidth(screenWidth - dropDownPadding * 2);
                    searchEditText.setDropDownWidth(screenWidth);
                }
            });
        }

        searchView.setSuggestionsAdapter(globalSearchAdapter);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);

        return true;
    }

    public void initSearch() {
        globalSearchAdapter = new GlobalSearchAdapter(this, R.layout.suggestion_search_simple, null);
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        globalSearchAdapter.setData(sUser.getData());
    }

    /**
     * Assuming this activity was started with a new intent, process the incoming information and
     * react accordingly.
     *
     * @param intent
     */
    private void handleIntent(Intent intent) {
        // Special processing of the incoming intent only occurs if the if the action specified
        // by the intent is ACTION_SEARCH.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            String query = intent.getStringExtra(SearchManager.QUERY);

            // We need to create a bundle containing the query string to send along to the
            // LoaderManager, which will be handling querying the database and returning results.
            Bundle bundle = new Bundle();
            bundle.putString(QUERY_KEY, query);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void closeDrawerLayout() {
        drawerLayout.closeDrawers();
        bCloseDrawerLayout = false;
    }

    @Override
    public void onLeftFragmentInteraction(int id, String title) {
        drawerLayout.closeDrawers();
        appBarTitle = title;
        viewPagerMoveIndex = -1;
        switch (id) {
            case R.id.drawer_home:
                viewPagerMoveIndex = 0;
                break;
            case R.id.drawer_member:
                viewPagerMoveIndex = 1;
                break;
            case R.id.drawer_notification:
                viewPagerMoveIndex = 2;
                break;

            case R.id.drawer_photo:
                viewPagerMoveIndex = 3;
                break;
            case R.id.drawer_funny:
                viewPagerMoveIndex = 4;
                break;
            case R.id.drawer_search:
                viewPagerMoveIndex = 5;
                break;

            /* case R.id.drawer_chat:
                viewPagerMoveIndex = 5;
                break; */
        }
        if (viewPagerMoveIndex != -1) {
            bCloseDrawerLayout = true;
        }
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

    @Override
    public void onBackPressed() {
        if (isShowCustomViewer()) {
            hideImagePreview();
            return;
        }
        if (viewPager.getCurrentItem() != 0) {
            // back go home fragment
            viewPager.setCurrentItem(0);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        viewPager.setCurrentItem(4);
        SearchFragment searchFragment = (SearchFragment) appViewPagerAdapter.getFragmentItem(4);
        searchFragment.search(query);
        searchEditText.dismissDropDown();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DMobi.log(TAG, "onQueryTextChange: " + newText);
        int result = globalSearchAdapter.search(newText);
        if (result > 0 && DUtils.isEmpty(newText)) {
            DMobi.log(TAG, "showDropDown");
            searchEditText.showDropDown();
            OpenSearchSuggestionsList(searchView);
        }
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        DMobileModelBase item = globalSearchAdapter.getItemAtPosition(position);
        item.showItemDetail(this);
        return false;
    }

    private void OpenSearchSuggestionsList(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);

            if (child instanceof ViewGroup) {
                OpenSearchSuggestionsList((ViewGroup) child);
            } else if (child instanceof AutoCompleteTextView) {
                // Found the right child - show dropdown
                ((AutoCompleteTextView) child).showDropDown();
                break; // We're done
            }
        }
    }
}
