package com.learn.turtorial1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.learn.turtorial1.library.dmobi.global.DConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class MainActivity extends ActionBarActivity  implements ArticleFragment.OnFragmentInteractionListener, LeftMenuFragment.OnLeftFragmentInteractionListener{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.imageDecoder(new NutraBaseImageDecoder(true));
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

        ArticleFragment articleFragment = new ArticleFragment();

        articleFragment.setArguments(getIntent().getExtras());

        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
        leftMenuFragment.setArguments(getIntent().getExtras());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, articleFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.left_drawer, leftMenuFragment).commit();

//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Login....");
//        progressDialog.setMessage("Wait some minute...");
//        progressDialog.show();

        // set application context config
        DConfig.setContext(getApplicationContext());

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void test(View view){
        /*TextView textView = (TextView)findViewById(R.id.tbname);
        Log.i("Buc vai", textView.getText().toString());
        Intent intent = new Intent(this, DisplayMessage.class);

        EditText editText = (EditText)findViewById(R.id.tbmessage);
        String text = editText.getText().toString();
        intent.putExtra("MESSAGE_TEXT",text);
        startActivity(intent);*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLeftFragmentInteraction(String id) {

    }
}
