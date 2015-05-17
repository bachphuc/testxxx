package com.learn.turtorial1;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.learn.turtorial1.jsonx.Blog;
import com.learn.turtorial1.model.RequestResultObject;
import com.learn.turtorial1.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class GsonTurtorial extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson_turtorial);

        /*Button button = (Button)findViewById(R.id.button);
        EditText editText =(EditText)findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder().create();
                String sJson = "{\"blog_id\":4,\"blog_title\":\"chan vai\",\"category\":{\"category_id\":3,\"category_title\":\"buc ghe\"}}";
                Blog blog = gson.fromJson(sJson, Blog.class);
                Log.i("Gson turtorial", "36");

                String userStr = "{\"data\":[{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"}},{\"user_id\":\"2\",\"user_name\":\"profile-2\",\"full_name\":\"Nguyen Loc\",\"email\":\"loc@gmail.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/8390ecae5ac268ec3c8a4c2f6c418c67_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/8390ecae5ac268ec3c8a4c2f6c418c67_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/8390ecae5ac268ec3c8a4c2f6c418c67_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/8390ecae5ac268ec3c8a4c2f6c418c67.jpg\"}},{\"user_id\":\"3\",\"full_name\":\"test\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/3_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/3_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/3_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/3.jpg\"}}],\"complete\":1,\"status\":1}";

                //JsonParser parser = new JsonParser();
                //JsonArray jArray = parser.parse(userStr).getAsJsonArray();
                Log.i("Gson turtorial", "45");
                // ArrayList<User> users = gson.fromJson(userStr, new TypeToken<ArrayList<User>>() {}.getType());
                // RequestResultObject xxx = new RequestResultObject();
                RequestResultObject<User> xxx = new RequestResultObject<User>();
                RequestResultObject requestResultObject = gson.fromJson(userStr, xxx.getClass());
                Log.i("Gson turtorial", "42");
                Log.i("To gson", gson.toJson(requestResultObject));

                String blogstr = "{\"data\":[{\"blog_id\":\"1\",\"user_id\":\"1\",\"title\":\"Test Blog\",\"time_stamp\":\"1425703923\",\"time_update\":\"0\",\"is_approved\":\"1\",\"privacy\":\"0\",\"privacy_comment\":\"0\",\"post_status\":\"1\",\"total_comment\":\"0\",\"total_attachment\":\"0\",\"total_view\":\"0\",\"total_like\":\"0\",\"total_dislike\":\"0\",\"module_id\":\"blog\",\"item_id\":\"0\",\"text\":\"Test Blog\",\"text_parsed\":\"Test Blog\",\"user\":{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"},\"type_item\":\"user\",\"id_item\":\"1\",\"like\":{\"is_like\":false,\"total_like\":0}},\"description\":\"Test Blog\"},{\"blog_id\":\"2\",\"user_id\":\"1\",\"title\":\"chan vat va\",\"time_stamp\":\"1426960230\",\"time_update\":\"0\",\"is_approved\":\"1\",\"privacy\":\"0\",\"privacy_comment\":\"0\",\"post_status\":\"1\",\"total_comment\":\"0\",\"total_attachment\":\"0\",\"total_view\":\"0\",\"total_like\":\"0\",\"total_dislike\":\"0\",\"module_id\":\"blog\",\"item_id\":\"0\",\"text\":\"that la buc minh\",\"text_parsed\":\"that la buc minh\",\"user\":{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"},\"type_item\":\"user\",\"id_item\":\"1\",\"like\":{\"is_like\":false,\"total_like\":0}},\"description\":\"that la buc minh\"},{\"blog_id\":\"3\",\"user_id\":\"1\",\"title\":\"test blog\",\"time_stamp\":\"1427047345\",\"time_update\":\"0\",\"is_approved\":\"1\",\"privacy\":\"0\",\"privacy_comment\":\"0\",\"post_status\":\"1\",\"total_comment\":\"0\",\"total_attachment\":\"0\",\"total_view\":\"0\",\"total_like\":\"0\",\"total_dislike\":\"0\",\"module_id\":\"blog\",\"item_id\":\"0\",\"text\":\"sdf sdaf asdfads fds  sfsadfsd fdsffdshkflhskfjdshkfhs dkfjkfkjsdfksdfksdfjkds kjfksjd fkjsdfkjsdkfjksjfsdf dsf sdfdsf sdff\",\"text_parsed\":\"sdf sdaf asdfads fds  sfsadfsd fdsffdshkflhskfjdshkfhs dkfjkfkjsdfksdfksdfjkds kjfksjd fkjsdfkjsdkfjksjfsdf dsf sdfdsf sdff\",\"user\":{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"},\"type_item\":\"user\",\"id_item\":\"1\",\"like\":{\"is_like\":false,\"total_like\":0}},\"description\":\"sdf sdaf asdfads fds  sfsadfsd fdsffdshkflhskfjdshkfhs dkfjkfkjsdfksdfksdfjkds kjfksjd fkjsdfkjsdkfjksjfsdf dsf sdfdsf sdff\"},{\"blog_id\":\"4\",\"user_id\":\"1\",\"title\":\"fd saf sdfsdf sdfd fsd fsdf slf sd fsdlkfslkdfk\",\"time_stamp\":\"1427896258\",\"time_update\":\"0\",\"is_approved\":\"1\",\"privacy\":\"0\",\"privacy_comment\":\"0\",\"post_status\":\"1\",\"total_comment\":\"0\",\"total_attachment\":\"0\",\"total_view\":\"0\",\"total_like\":\"0\",\"total_dislike\":\"0\",\"module_id\":\"blog\",\"item_id\":\"0\",\"text\":\"lksd fls jlfkjlakfkljaflkslkfklsdflksdflksdlkfs fsd fsdfkldskjf lsdf sf sf lsdkfjlkds fjklsd fsdfsd fsd fdsf sdfsd\",\"text_parsed\":\"lksd fls jlfkjlakfkljaflkslkfklsdflksdflksdlkfs fsd fsdfkldskjf lsdf sf sf lsdkfjlkds fjklsd fsdfsd fsd fdsf sdfsd\",\"user\":{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"},\"type_item\":\"user\",\"id_item\":\"1\",\"like\":{\"is_like\":false,\"total_like\":0}},\"description\":\"lksd fls jlfkjlakfkljaflkslkfklsdflksdflksdlkfs fsd fsdfkldskjf lsdf sf sf lsdkfjlkds fjklsd fsdfsd fsd fdsf sdfsd\"},{\"blog_id\":\"5\",\"user_id\":\"1\",\"title\":\"tes ttest\",\"time_stamp\":\"1428853075\",\"time_update\":\"0\",\"is_approved\":\"1\",\"privacy\":\"0\",\"privacy_comment\":\"0\",\"post_status\":\"1\",\"total_comment\":\"0\",\"total_attachment\":\"0\",\"total_view\":\"0\",\"total_like\":\"0\",\"total_dislike\":\"0\",\"module_id\":\"blog\",\"item_id\":\"0\",\"text\":\"sf asdf sd fsdf\",\"text_parsed\":\"sf asdf sd fsdf\",\"user\":{\"user_id\":\"1\",\"user_name\":\"phuclb\",\"full_name\":\"phuclb\",\"email\":\"phuclb@localhost.com\",\"images\":{\"avatar\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_50_square.jpg\",\"normal\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_120.jpg\",\"medium\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2_200.jpg\",\"full\":\"http:\\/\\/phpfox\\/phpfox.3.7.7\\/file\\/pic\\/user\\/2015\\/03\\/944fa1c2241239f9acff9573bb48f0a2.jpg\"},\"type_item\":\"user\",\"id_item\":\"1\",\"like\":{\"is_like\":false,\"total_like\":0}},\"description\":\"sf asdf sd fsdf\"}],\"complete\":1,\"status\":1}";
                RequestResultObject<Blog> yyy = new RequestResultObject<Blog>();
                RequestResultObject blogs = gson.fromJson(blogstr, yyy.getClass());
                Log.i("Gson turtorial", "42");
                Log.i("To gson", gson.toJson(blogs));
            }
        });

        Button requestbt = (Button)findViewById(R.id.btrequest);

        final TextView tvRespond = (TextView)findViewById(R.id.tbrespond);
        requestbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=user.getItems";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Request", s);
                        tvRespond.setText(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("Respond", "Network error");
                        tvRespond.setText("Network error");
                    }
                });

                reqestQueue.add(stringRequest);
            }
        });*/

        final ListView listView= (ListView) findViewById(android.R.id.list);

        RequestQueue reqestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://dmobi.pe.hu/module/dmobile/api.php?token=b3cff55d83b4367ade5413&api=user.getItems";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Request", s);
                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext());

                Gson gson = new GsonBuilder().create();
                RequestResultObject<User> userRequestResultObject = new RequestResultObject<User>();
                RequestResultObject respond = gson.fromJson(s, userRequestResultObject.getClass());
                imageAdapter.setData(respond.data);
                listView.setAdapter(imageAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Respond", "Network error");

            }
        });
        // ((ListView) listView).setAdapter(new ImageAdapter(getApplicationContext()));
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ImageAdapter extends BaseAdapter {
        protected  List<User> users;
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

            holder.text.setText("Item " + (position + 1));
            Log.i("Index", "" + position);
            Log.i("Size", "" + getCount());
            String url = users.get(position).images.full;
            ImageLoader.getInstance().displayImage(url, holder.image, options, animateFirstListener);

            return view;
        }

        public void setData(List<User> data){
            users = data;
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
