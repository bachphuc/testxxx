package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.learn.mobile.DMobiApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by BachPhuc on 4/10/2016.
 */
public class DFragmentBase extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroyEvent() {
    }

    @Override
    public void onDestroy() {
        onDestroyEvent();
        super.onDestroy();

        RefWatcher refWatcher = DMobiApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }
}
