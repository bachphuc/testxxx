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
import com.learn.turtorial1.model.Feed;
import com.learn.turtorial1.model.RequestResultObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class RecyclerViewTest extends ActionBarActivity {
    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;
    int iPage = 0;
    int iMaxFeedId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(new ArrayList<Feed>());
        recyclerView.setAdapter(feedAdapter);

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
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=feed.gets&args[android]=1&args[sAction]=loadnew&args[iMaxFeedId]=" + iMaxFeedId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dSwipeRefreshLayout.setRefreshing(false);
                Log.i("Request", s);

                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestResultObject<Feed>>() {
                }.getType();

                RequestResultObject<Feed> respond = gson.fromJson(s, type);
                Log.i("Request", "Complete");
                if (respond != null) {
                    feedAdapter.prependData(respond.data);
                    feedAdapter.notifyDataSetChanged();
                    updateMaxFeed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
                dSwipeRefreshLayout.setRefreshing(false);
                Toast toast = Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        reqestQueue.add(stringRequest);
    }

    public void loadMoreFeed() {
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=feed.gets&args[android]=1&args[iPage]=" + iPage;
        Log.i("Send request", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Request data", s);
                dSwipeRefreshLayout.stopLoadMore();
                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestResultObject<Feed>>() {
                }.getType();

                RequestResultObject<Feed> respond = gson.fromJson(s, type);
                Log.i("Request", "Complete");
                if (respond != null) {
                    iPage++;
                    if (respond.data.size() > 0) {
                        feedAdapter.appendData(respond.data);
                        feedAdapter.notifyDataSetChanged();
                        updateMaxFeed();
                    } else {
                        dSwipeRefreshLayout.loadMoreLimit();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
                dSwipeRefreshLayout.stopLoadMore();
                Toast toast = Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        reqestQueue.add(stringRequest);
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
