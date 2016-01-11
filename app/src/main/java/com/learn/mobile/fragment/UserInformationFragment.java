package com.learn.mobile.fragment;


import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.model.User;

public class UserInformationFragment extends Fragment implements ObservableScrollView {

    User user;
    NestedScrollView nestedScrollView;
    View view;
    FrameLayout frameLayout;

    public UserInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_information, container, false);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
        frameLayout.removeAllViews();
        frameLayout.addView(LayoutInflater.from(getContext()).inflate(R.layout.item_header_spacing, null));
        updateView();
        return view;
    }

    public void setUser(User u) {
        user = u;
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return nestedScrollView;
        }
        return null;
    }

    public void updateView() {
        if (user != null && view != null) {
            TextView textView = (TextView) view.findViewById(R.id.tv_fullName);
            if (textView != null) {
                textView.setText(user.fullName);
            }
            textView = (TextView) view.findViewById(R.id.tv_username);
            if (textView != null) {
                textView.setText(user.userName);
            }
            textView = (TextView) view.findViewById(R.id.tv_email);
            if (textView != null) {
                textView.setText(user.email);
            }
        }
    }
}
