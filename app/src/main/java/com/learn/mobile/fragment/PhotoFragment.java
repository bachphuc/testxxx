package com.learn.mobile.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.service.SPhoto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PhotoFragment extends ListBaseFragment implements ObservableScrollView {
    public PhotoFragment(){
        setServiceClass(SPhoto.class);
        setGirdLayout(true);
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }
}
