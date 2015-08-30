package com.learn.turtorial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.learn.turtorial1.model.RequestListObjectResponse;
import com.learn.turtorial1.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class GsonTurtorial extends ActionBarActivity {
    DSwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    ImageAdapter imageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson_turtorial);

        listView = (ListView) findViewById(android.R.id.list);

        imageAdapter = new ImageAdapter(getApplicationContext());
        listView.setAdapter(imageAdapter);

        swipeRefreshLayout = (DSwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        swipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        swipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        swipeRefreshLayout.startLoad();
    }

    public void getData() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=user.gets";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("DRequest", s);

                Gson gson = new GsonBuilder().create();
                RequestListObjectResponse<User> userRequestResultObject = new RequestListObjectResponse<User>();

                Type type = new TypeToken<RequestListObjectResponse<User>>() {
                }.getType();

                RequestListObjectResponse<User> respond = gson.fromJson(s, type);
                respond.data.toArray();

                imageAdapter.prependData(respond.data);
                imageAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        reqestQueue.add(stringRequest);
    }

    private int MAX_LIMIT = 10;
    private int CURRENT_REQUEST = 0;
    public void loadMoreData() {
        CURRENT_REQUEST++;
        if(CURRENT_REQUEST > MAX_LIMIT){
            swipeRefreshLayout.loadMoreLimit();
            return;
        }
        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=user.gets";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                swipeRefreshLayout.stopLoadMore();
                Log.i("DRequest", s);
                Gson gson = new GsonBuilder().create();

                Type type = new TypeToken<RequestListObjectResponse<User>>() {
                }.getType();

                RequestListObjectResponse<User> respond = gson.fromJson(s, type);

                imageAdapter.appendData(respond.data);
                imageAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");
                swipeRefreshLayout.stopLoadMore();
            }
        });

        reqestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gson_turtorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_refresh) {
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    swipeRefreshLayout.setPadding(0, 0, 0, (int) (100 * interpolatedTime));
                }
            };
            a.setDuration(200);
            swipeRefreshLayout.startAnimation(a);
        } else if (id == R.id.menu_showbackdrop) {
            swipeRefreshLayout.showBackDrop();
        } else if (id == R.id.menu_hidebackdrop) {
            swipeRefreshLayout.hideBackDrop();
        } else if (id == R.id.menu_animatemargin) {
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    listView.setPadding(0, 0, 0, (int) (interpolatedTime * 100));
                }
            };

            a.setDuration(200);
            listView.startAnimation(a);
        } else if (id == R.id.menu_reveranimatemargin) {
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int iBottom = 100 - (int) (interpolatedTime * 100);
                    listView.setPadding(0, 0, 0, iBottom);
                }
            };
            a.setDuration(200);
            listView.startAnimation(a);
        }
        else if(id == R.id.menu_stoploadmore){
            swipeRefreshLayout.stopLoadMore();
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ImageAdapter extends BaseAdapter {
        protected List<User> users = new ArrayList<User>();
        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(20)).build();
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.image = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            User user = users.get(position);
            holder.text.setText(user.fullName);

            String url = users.get(position).images.full.url;
            ImageLoader.getInstance().displayImage(url, holder.image, options, animateFirstListener);

            return view;
        }

        public void setData(List<User> data) {
            users = data;
        }

        public void appendData(List<User> data) {
            users.addAll(data);
        }

        public void prependData(List<User> data) {
            users.addAll(0, data);
        }
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
