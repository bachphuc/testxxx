package com.learn.mobile.fragment;

import android.support.v4.app.Fragment;

import com.learn.mobile.service.SPhoto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends ListBaseFragment {
    public PhotoFragment(){
        setServiceClass(SPhoto.class);
        setbGirdLayout(true);
    }
}
