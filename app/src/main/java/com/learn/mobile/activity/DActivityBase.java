package com.learn.mobile.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;

/**
 * Created by 09520_000 on 9/23/2015.
 */
public class DActivityBase extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final String TAG = DActivityBase.class.getSimpleName();
    protected AppBarLayout appBarLayout;
    protected int appBarOffsetTop = 0;

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        appBarOffsetTop = i;
        onAppBarLayoutOffsetChange(appBarLayout, i);
    }

    protected void onAppBarLayoutOffsetChange(AppBarLayout appBarLayout, int i){

    }

    public int getAppBarOffsetTop(){
        return appBarOffsetTop;
    }

    public int getAppBarHeight(){
        if(appBarLayout == null){
            return 0;
        }
        return appBarLayout.getHeight();
    }
}
