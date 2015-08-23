package com.learn.turtorial1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learn.turtorial1.customview.DSwipeRefreshLayout;
import com.learn.turtorial1.library.dmobi.DMobi;
import com.learn.turtorial1.library.dmobi.request.Dresponse;
import com.learn.turtorial1.model.Feed;
import com.learn.turtorial1.model.RequestResultObject;
import com.learn.turtorial1.service.SFeed;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewTest extends ActionBarActivity {
    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;
    int iPage = 0;
    int iMaxFeedId = 0;
    SFeed sFeed = null;

    Dresponse.Complete completeResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        sFeed = (SFeed) DMobi.getService(SFeed.class);

        feedAdapter = new FeedAdapter(sFeed.getData());
        recyclerView.setAdapter(feedAdapter);

        completeResponse = new Dresponse.Complete() {
            @Override
            public void onComplete(Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    List<Feed> feeds = (ArrayList<Feed>) o;
                    iPage++;
                    if (feeds.size() > 0) {
                        feedAdapter.notifyDataSetChanged();
                        updateMaxFeed();
                    } else {
                        dSwipeRefreshLayout.loadMoreLimit();
                    }
                }
            }
        };

        dSwipeRefreshLayout = (DSwipeRefreshLayout) findViewById(R.id.swiperefresh);
        dSwipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreFeed();
            }
        });

        dSwipeRefreshLayout.startLoad();
    }

    public void updateMaxFeed() {
        iMaxFeedId = feedAdapter.getMaxId();
    }

    public void refreshFeed() {
        SFeed feedService = (SFeed) DMobi.getService("SFeed");
        feedService.loadNewFeeds(this.completeResponse);
    }

    public void loadMoreFeed() {
        SFeed feedService = (SFeed) DMobi.getService("SFeed");
        feedService.loadMoreFeeds(this.completeResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recycler_view_test, menu);
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
}
