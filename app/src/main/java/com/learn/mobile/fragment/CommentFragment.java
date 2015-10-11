package com.learn.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.service.SComment;

/**
 * Created by 09520_000 on 10/4/2015.
 */
public class CommentFragment extends ListBaseFragment {
    public CommentFragment() {
        setServiceClass(SComment.class);
        setAutoLoadData(true);
        setRefreshList(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if(linearLayoutManager != null){
            linearLayoutManager.setStackFromEnd(true);
        }

        return view;
    }
}
