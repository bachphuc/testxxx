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
import com.learn.turtorial1.model.Feed;
import com.learn.turtorial1.model.RequestResultObject;

import java.lang.reflect.Type;


public class RecyclerViewTest extends ActionBarActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        recyclerView = (RecyclerView)findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*List<ContactInfo> list = new ArrayList<ContactInfo>();
        for(int i = 0; i< 10000; i++){
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.name = "test " + i;
            contactInfo.surname = "surname " + i;
            contactInfo.email = "test" +i + "@gmail.com";
            contactInfo.feedTitle = "chan vai te " + i;
            contactInfo.image = "http://thewowstyle.com/wp-content/uploads/2015/03/Beautiful-girl-wallpaper.jpg";
            contactInfo.type = 3;
            list.add(contactInfo);
        }*/

        // recyclerView.setAdapter(new FeedAdapter(list));

        getData();
    }

    public void getData() {
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=feed.gets&args[android]=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Request", s);

                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestResultObject<Feed>>() {
                }.getType();

                RequestResultObject<Feed> respond = gson.fromJson(s, type);
                Log.i("Request", "Complete");
                if(respond != null) {
                    recyclerView.setAdapter(new FeedAdapter(respond.data));
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
