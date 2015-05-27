package com.learn.turtorial1;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RecyclerViewTest extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<ContactInfo> list = new ArrayList<ContactInfo>();
        for(int i = 0; i< 10000; i++){
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.name = "test " + i;
            contactInfo.surname = "surname " + i;
            contactInfo.email = "test" +i + "@gmail.com";
            contactInfo.feedTitle = "chan vai te " + i;
            contactInfo.image = "http://thewowstyle.com/wp-content/uploads/2015/03/Beautiful-girl-wallpaper.jpg";
            contactInfo.type = 3;
            list.add(contactInfo);
        }
        recyclerView.setAdapter(new ContactAdapter(list));
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
