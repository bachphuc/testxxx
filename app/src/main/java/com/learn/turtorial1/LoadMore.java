package com.learn.turtorial1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.learn.turtorial1.model.RequestListObjectResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class LoadMore extends ActionBarActivity {
    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        recyclerView = (RecyclerView)findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(new ArrayList<Feed>());
        recyclerView.setAdapter(feedAdapter);

        dSwipeRefreshLayout = (DSwipeRefreshLayout)findViewById(R.id.swiperefresh);
        dSwipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        dSwipeRefreshLayout.startLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_more, menu);
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

    public void getData() {
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=feed.gets&args[android]=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dSwipeRefreshLayout.setRefreshing(false);
                Log.i("DRequest", s);

                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestListObjectResponse<Feed>>() {
                }.getType();

                RequestListObjectResponse<Feed> respond = gson.fromJson(s, type);
                Log.i("DRequest", "Complete");
                if(respond != null) {
                    feedAdapter.prependData(respond.data);
                    feedAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
            }
        });

        reqestQueue.add(stringRequest);
    }

    public void loadMoreData() {
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=feed.gets&args[android]=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("RequestRecyclerTest", s);
                dSwipeRefreshLayout.stopLoadMore();
                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestListObjectResponse<Feed>>() {
                }.getType();

                RequestListObjectResponse<Feed> respond = gson.fromJson(s, type);
                Log.i("DRequest", "Complete");
                if(respond != null) {
                    feedAdapter.appendData(respond.data);
                    feedAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
            }
        });

        reqestQueue.add(stringRequest);
    }
}
