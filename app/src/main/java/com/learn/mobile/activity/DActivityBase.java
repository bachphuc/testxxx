package com.learn.mobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.learn.mobile.DMobiApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by BachPhuc on 4/10/2016.
 */
public class DActivityBase extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroyEvent() {

    }

    @Override
    protected void onDestroy() {
        onDestroyEvent();
        super.onDestroy();

        RefWatcher refWatcher = DMobiApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
