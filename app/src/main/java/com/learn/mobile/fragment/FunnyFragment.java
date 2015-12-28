package com.learn.mobile.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.service.SFunny;

/**
 * Created by 09520_000 on 12/28/2015.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FunnyFragment extends ListBaseFragment implements ObservableScrollView {
    public FunnyFragment() {
        setServiceClass(SFunny.class);
        setRefreshList(true);
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }
}
